package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import model.Anggota;
import model.AnggotaAktif;
import model.Dokumen;
import model.Peminjaman;

/**
 * Controller CRUD untuk transaksi peminjaman dokumen perpustakaan.
 * Tabel: peminjaman.
 * Alur proses: ValidasiPeminjaman → INSERT peminjaman → UPDATE stok buku.
 * @author [nama kamu]
 */
public class ManagerPeminjaman {

    // ===================== Atribut =====================

    private List<Peminjaman> daftarPeminjaman;
    private Connection       connection;
    private ValidasiPeminjaman validasi;

    // ===================== Constructor =====================

    public ManagerPeminjaman(Connection connection) {
        this.connection       = connection;
        this.daftarPeminjaman = new ArrayList<>();
        this.validasi         = new ValidasiPeminjaman(connection);
    }

    // ===================== Proses Peminjaman =====================

    /**
     * Proses lengkap peminjaman dokumen oleh anggota.
     * Langkah:
     *   1. Validasi anggota + stok + kuota.
     *   2. Generate ID peminjaman.
     *   3. INSERT ke tabel peminjaman (transaksi).
     *   4. UPDATE stok buku jika dokumen adalah Buku.
     *
     * @param anggota   objek Anggota yang meminjam
     * @param dokumen   objek Dokumen yang dipinjam
     */
    public void prosesKeminjaman(Anggota anggota, Dokumen dokumen) {
        if (!validasi.isEligible(anggota, dokumen)) {
            System.out.println("[ManagerPeminjaman] Peminjaman ditolak. Syarat tidak terpenuhi.");
            return;
        }

        String idBaru = generateIdPeminjaman();
        LocalDate today = LocalDate.now();
        Peminjaman p = new Peminjaman(idBaru, anggota, dokumen, today);

        boolean berhasil = simpanPeminjamanDB(p);

        if (berhasil) {
            // Kurangi stok buku jika Buku (Majalah tidak punya stok)
            updateStokKoleksi(dokumen.getIdDokumen());
            daftarPeminjaman.add(p);
            System.out.println("[ManagerPeminjaman] Peminjaman berhasil: " + idBaru);
            p.tampilkanDetail();
        } else {
            System.out.println("[ManagerPeminjaman] Peminjaman gagal disimpan ke database.");
        }
    }

    /**
     * Kurangi stok buku sebesar 1 setelah peminjaman.
     * Diabaikan otomatis jika id_dokumen adalah Majalah (tidak ada baris di tabel buku).
     */
    public void updateStokKoleksi(String idDokumen) {
        String sql = "UPDATE buku SET stok = stok - 1 WHERE id_dokumen = ? AND stok > 0";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, idDokumen);
            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("[ManagerPeminjaman] Stok buku diperbarui untuk: " + idDokumen);
            }
        } catch (SQLException e) {
            System.err.println("[ManagerPeminjaman] updateStokKoleksi error: " + e.getMessage());
        }
    }

    // ===================== READ =====================

    /**
     * Cari Peminjaman berdasarkan id.
     * Return null jika tidak ditemukan.
     */
    public Peminjaman cariById(String idPeminjaman) {
        String sql = "SELECT id_peminjaman, id_anggota, id_dokumen, "
                   + "tanggal_pinjam, tanggal_jatuh_tempo, status "
                   + "FROM peminjaman WHERE id_peminjaman = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, idPeminjaman);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("[ManagerPeminjaman] cariById error: " + e.getMessage());
        }
        return null;
    }

    /**
     * Ambil semua peminjaman dengan status 'AKTIF'.
     */
    public List<Peminjaman> getPeminjamanAktif() {
        return getPeminjamanByStatus("AKTIF");
    }

    /**
     * Ambil semua peminjaman (semua status) dari DB.
     */
    public List<Peminjaman> getDaftarPeminjaman() {
        return getPeminjamanByStatus(null);
    }

    private List<Peminjaman> getPeminjamanByStatus(String status) {
        List<Peminjaman> list = new ArrayList<>();
        String sql = "SELECT id_peminjaman, id_anggota, id_dokumen, "
                   + "tanggal_pinjam, tanggal_jatuh_tempo, status "
                   + "FROM peminjaman"
                   + (status != null ? " WHERE status = ?" : "");
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            if (status != null) ps.setString(1, status);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("[ManagerPeminjaman] getPeminjaman error: " + e.getMessage());
        }
        return list;
    }

    /**
     * Ambil semua peminjaman milik anggota tertentu (untuk Riwayat Member).
     */
    public List<Peminjaman> getPeminjamanByAnggota(String idAnggota) {
        List<Peminjaman> list = new ArrayList<>();
        String sql = "SELECT id_peminjaman, id_anggota, id_dokumen, "
                   + "tanggal_pinjam, tanggal_jatuh_tempo, status "
                   + "FROM peminjaman WHERE id_anggota = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, idAnggota);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("[ManagerPeminjaman] getPeminjamanByAnggota error: " + e.getMessage());
        }
        return list;
    }

    // ===================== Tampil =====================

    /**
     * Cetak semua peminjaman (dari cache in-memory daftarPeminjaman).
     */
    public void tampilkanSemua() {
        if (daftarPeminjaman.isEmpty()) {
            System.out.println("[ManagerPeminjaman] Belum ada peminjaman.");
            return;
        }
        System.out.println("=== Daftar Peminjaman ===");
        for (Peminjaman p : daftarPeminjaman) {
            p.tampilkanDetail();
            System.out.println("------------------------");
        }
    }

    // ===================== Helper =====================

    /** Simpan satu peminjaman ke DB dalam satu transaksi. */
    private boolean simpanPeminjamanDB(Peminjaman p) {
        String sql = "INSERT INTO peminjaman "
                   + "(id_peminjaman, id_anggota, id_dokumen, tanggal_pinjam, tanggal_jatuh_tempo, status) "
                   + "VALUES (?, ?, ?, ?, ?, ?)";
        try {
            connection.setAutoCommit(false);
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setString(1, p.getIdPeminjaman());
                ps.setString(2, p.getPeminjam().getIdAnggota());
                ps.setString(3, p.getDokumenDipinjam().getIdDokumen());
                ps.setDate(4, java.sql.Date.valueOf(p.getTanggalPinjam()));
                ps.setDate(5, java.sql.Date.valueOf(p.getTanggalJatuhTempo()));
                ps.setString(6, p.getStatus());
                ps.executeUpdate();
            }
            connection.commit();
            return true;
        } catch (SQLException e) {
            try { connection.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            System.err.println("[ManagerPeminjaman] simpanPeminjamanDB error: " + e.getMessage());
            return false;
        } finally {
            try { connection.setAutoCommit(true); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    /**
     * Map satu baris ResultSet ke objek Peminjaman.
     * Anggota dan Dokumen hanya diisi id-nya (shell object) untuk efisiensi.
     * Jika butuh data lengkap, join query atau hydrate setelah mapping.
     */
    private Peminjaman mapRow(ResultSet rs) throws SQLException {
        // Shell anggota — hanya ID (lengkapi dengan ManagerAnggota jika perlu)
        AnggotaAktif shellAnggota = new AnggotaAktif();
        shellAnggota.setIdAnggota(rs.getString("id_anggota"));

        // Shell dokumen — hanya ID
        model.Buku shellDokumen = new model.Buku();
        shellDokumen.setIdDokumen(rs.getString("id_dokumen"));

        Peminjaman p = new Peminjaman();
        p.setIdPeminjaman(rs.getString("id_peminjaman"));
        p.setPeminjam(shellAnggota);
        p.setDokumenDipinjam(shellDokumen);
        p.setTanggalPinjam(rs.getDate("tanggal_pinjam").toLocalDate());
        p.setTanggalJatuhTempo(rs.getDate("tanggal_jatuh_tempo").toLocalDate());
        p.setStatus(rs.getString("status"));
        return p;
    }

    /**
     * Generate ID peminjaman format: PJM-yyyyMMdd-XXX (auto-increment suffix).
     * Contoh: PJM-20260608-001
     */
    private String generateIdPeminjaman() {
        String tanggal = LocalDate.now().toString().replace("-", "");
        String prefix  = "PJM-" + tanggal + "-";
        String sql = "SELECT COUNT(*) FROM peminjaman WHERE id_peminjaman LIKE ?";
        int urutan = 1;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, prefix + "%");
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) urutan = rs.getInt(1) + 1;
            }
        } catch (SQLException e) {
            System.err.println("[ManagerPeminjaman] generateId error: " + e.getMessage());
        }
        return String.format("%s%03d", prefix, urutan);
    }

    // ===================== Getter / Setter =====================

    public List<Peminjaman>    getDaftarPeminjamanCache()             { return daftarPeminjaman; }
    public ValidasiPeminjaman  getValidasi()                         { return validasi; }
    public void                setValidasi(ValidasiPeminjaman v)     { this.validasi = v; }
}
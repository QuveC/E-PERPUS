package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import model.Pengembalian;
import model.Peminjaman;

/**
 * Controller CRUD untuk transaksi pengembalian dokumen perpustakaan.
 * Tabel: pengembalian.
 * Alur proses: cari Peminjaman aktif → hitung denda → UPDATE status → INSERT pengembalian.
 * @author Ahmad
 */
public class ManagerPengembalian {

    // ===================== Atribut =====================

    private List<Pengembalian> daftarPengembalian;
    private Connection         connection;
    private HitungDenda        hitungDenda;
    private ManagerPeminjaman  managerPeminjaman;

    // Tarif denda default: Rp 1.000 per hari
    private static final double TARIF_DEFAULT = 1000.0;

    // ===================== Constructor =====================

    public ManagerPengembalian(Connection connection) {
        this.connection        = connection;
        this.daftarPengembalian = new ArrayList<>();
        this.hitungDenda       = new HitungDenda(TARIF_DEFAULT);
        this.managerPeminjaman = new ManagerPeminjaman(connection);
    }

    // ===================== Proses Pengembalian =====================

    /**
     * Proses lengkap pengembalian dokumen.
     * Langkah:
     *   1. Cari data peminjaman aktif dari DB.
     *   2. Hitung denda keterlambatan.
     *   3. UPDATE status peminjaman → 'DIKEMBALIKAN'.
     *   4. INSERT record pengembalian ke DB.
     *
     * @param idPeminjaman        ID peminjaman yang dikembalikan
     * @param tanggalKembaliAktual Tanggal aktual pengembalian
     */
    public void prosesPengembalian(String idPeminjaman, LocalDate tanggalKembaliAktual) {
        // 1. Ambil data peminjaman dari DB via ManagerPeminjaman
        Peminjaman peminjaman = managerPeminjaman.cariById(idPeminjaman);

        if (peminjaman == null) {
            System.out.println("[ManagerPengembalian] Peminjaman ID " + idPeminjaman + " tidak ditemukan.");
            return;
        }

        if ("DIKEMBALIKAN".equals(peminjaman.getStatus())) {
            System.out.println("[ManagerPengembalian] Peminjaman ID " + idPeminjaman + " sudah dikembalikan.");
            return;
        }

        // 2. Hitung denda
        double totalDenda = hitungDenda.hitungDenda(
                peminjaman.getTglJatuhTempo(),
                tanggalKembaliAktual
        );

        // 3. Buat objek pengembalian
        String idBaru = generateIdPengembalian();
        Pengembalian pengembalian = new Pengembalian(idBaru, peminjaman, tanggalKembaliAktual, totalDenda);

        // 4. Simpan ke DB (update status + insert pengembalian dalam satu transaksi)
        boolean berhasil = simpanPengembalianDB(pengembalian);

        if (berhasil) {
            peminjaman.setStatus("DIKEMBALIKAN");
            daftarPengembalian.add(pengembalian);
            System.out.println("[ManagerPengembalian] Pengembalian berhasil: " + idBaru);
            pengembalian.tampilkanDetail();
        } else {
            System.out.println("[ManagerPengembalian] Pengembalian gagal disimpan ke database.");
        }
    }

    // ===================== READ =====================

    /**
     * Cari data pengembalian berdasarkan ID pengembalian.
     *
     * @param id ID pengembalian
     * @return objek Pengembalian jika ditemukan, null jika tidak
     */
    public Pengembalian cariById(String id) {
        String sql = "SELECT id_pengembalian, id_peminjaman, tanggal_kembali_aktual, jumlah_denda "
                   + "FROM pengembalian WHERE id_pengembalian = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        } catch (SQLException e) {
            System.err.println("[ManagerPengembalian] cariById error: " + e.getMessage());
        }
        return null;
    }

    /**
     * Ambil semua data pengembalian dari DB.
     *
     * @return List semua Pengembalian
     */
    public List<Pengembalian> getDaftarPengembalian() {
        List<Pengembalian> list = new ArrayList<>();
        String sql = "SELECT id_pengembalian, id_peminjaman, tanggal_kembali_aktual, jumlah_denda "
                   + "FROM pengembalian";
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            System.err.println("[ManagerPengembalian] getDaftarPengembalian error: " + e.getMessage());
        }
        return list;
    }

    // ===================== Tampil =====================

    /**
     * Cetak semua pengembalian dari cache in-memory.
     */
    public void tampilkanSemua() {
        if (daftarPengembalian.isEmpty()) {
            System.out.println("[ManagerPengembalian] Belum ada data pengembalian.");
            return;
        }
        System.out.println("=== Daftar Pengembalian ===");
        for (Pengembalian p : daftarPengembalian) {
            p.tampilkanDetail();
            System.out.println("--------------------------");
        }
    }

    // ===================== Helper =====================

    /**
     * Simpan pengembalian dan update status peminjaman dalam satu transaksi DB.
     */
    private boolean simpanPengembalianDB(Pengembalian p) {
        String sqlInsert = "INSERT INTO pengembalian "
                         + "(id_pengembalian, id_peminjaman, tanggal_kembali_aktual, jumlah_denda) "
                         + "VALUES (?, ?, ?, ?)";
        String sqlUpdate = "UPDATE peminjaman SET status = 'DIKEMBALIKAN' WHERE id_peminjaman = ?";

        try {
            connection.setAutoCommit(false);

            // INSERT pengembalian
            try (PreparedStatement psInsert = connection.prepareStatement(sqlInsert)) {
                psInsert.setString(1, p.getIdPengembalian());
                psInsert.setString(2, p.getPeminjaman().getIdPeminjaman());
                psInsert.setDate(3, java.sql.Date.valueOf(p.getTanggalKembaliAktual()));
                psInsert.setDouble(4, p.getJumlahDenda());
                psInsert.executeUpdate();
            }

            // UPDATE status peminjaman
            try (PreparedStatement psUpdate = connection.prepareStatement(sqlUpdate)) {
                psUpdate.setString(1, p.getPeminjaman().getIdPeminjaman());
                psUpdate.executeUpdate();
            }

            // RESTORE stock of book (if it is a book)
            String sqlRestoreStock = "UPDATE buku SET stok = stok + 1 WHERE id_dokumen = ?";
            try (PreparedStatement psRestore = connection.prepareStatement(sqlRestoreStock)) {
                psRestore.setString(1, p.getPeminjaman().getDokumenDipinjam().getIdDokumen());
                psRestore.executeUpdate();
            }

            connection.commit();
            return true;

        } catch (SQLException e) {
            try { connection.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            System.err.println("[ManagerPengembalian] simpanPengembalianDB error: " + e.getMessage());
            return false;
        } finally {
            try { connection.setAutoCommit(true); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    /**
     * Map satu baris ResultSet ke objek Pengembalian.
     * Peminjaman hanya diisi id-nya (shell object).
     */
    private Pengembalian mapRow(ResultSet rs) throws SQLException {
        Peminjaman shellPeminjaman = new Peminjaman();
        shellPeminjaman.setIdPeminjaman(rs.getString("id_peminjaman"));

        Pengembalian p = new Pengembalian();
        p.setIdPengembalian(rs.getString("id_pengembalian"));
        p.setPeminjaman(shellPeminjaman);
        p.setTanggalKembaliAktual(rs.getDate("tanggal_kembali_aktual").toLocalDate());
        p.setJumlahDenda(rs.getDouble("jumlah_denda"));
        return p;
    }

    /**
     * Generate ID pengembalian format: PGB-yyyyMMdd-XXX.
     * Contoh: PGB-20260608-001
     */
    private String generateIdPengembalian() {
        String tanggal = LocalDate.now().toString().replace("-", "");
        String prefix  = "PGB-" + tanggal + "-";
        String sql = "SELECT COUNT(*) FROM pengembalian WHERE id_pengembalian LIKE ?";
        int urutan = 1;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, prefix + "%");
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) urutan = rs.getInt(1) + 1;
            }
        } catch (SQLException e) {
            System.err.println("[ManagerPengembalian] generateId error: " + e.getMessage());
        }
        return String.format("%s%03d", prefix, urutan);
    }

    // ===================== Getter & Setter =====================

    public List<Pengembalian> getDaftarPengembalianCache()              { return daftarPengembalian; }
    public HitungDenda        getHitungDenda()                          { return hitungDenda; }
    public void               setHitungDenda(HitungDenda hitungDenda)  { this.hitungDenda = hitungDenda; }
    public ManagerPeminjaman  getManagerPeminjaman()                    { return managerPeminjaman; }
    public void               setManagerPeminjaman(ManagerPeminjaman m) { this.managerPeminjaman = m; }
}
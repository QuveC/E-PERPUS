package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.Anggota;
import model.AnggotaAktif;
import model.Dokumen;

/**
 * Mengurus semua validasi sebelum transaksi peminjaman diproses.
 * Pengecekan meliputi:
 *   1. Stok dokumen tersedia (> 0) — cek DB atau objek in-memory.
 *   2. Status anggota aktif (bukan AnggotaNonAktif).
 *   3. Anggota belum melampaui batas maksimal pinjam (batasMaxPinjam).
 *
 * Dapat dipakai dengan Connection (mode DB) maupun tanpa (mode in-memory).
 * @author [nama kamu]
 */
public class ValidasiPeminjaman {

    // ===================== Atribut =====================

    private int     limitPinjam;  // batas default jika tidak ada di objek anggota
    private boolean statusValid;  // hasil validasi terakhir

    private Connection connection; // opsional — null jika mode in-memory

    // ===================== Constructor =====================

    /** Mode DB: ambil data stok & jumlah pinjam aktif dari database. */
    public ValidasiPeminjaman(Connection connection) {
        this.connection  = connection;
        this.limitPinjam = 3; // fallback default
        this.statusValid = false;
    }

    /** Mode in-memory: validasi hanya dari objek yang diteruskan. */
    public ValidasiPeminjaman() {
        this.limitPinjam = 3;
        this.statusValid = false;
    }

    // ===================== Metode Validasi Utama =====================

    /**
     * Validasi gabungan: stok dokumen + status anggota + kuota pinjam.
     * Return true hanya jika ketiga syarat terpenuhi.
     * Hasil disimpan di statusValid untuk dicek kembali jika perlu.
     */
    public boolean isEligible(Anggota anggota, Dokumen dokumen) {
        statusValid = false;

        if (anggota == null || dokumen == null) {
            System.err.println("[ValidasiPeminjaman] Anggota atau Dokumen null.");
            return false;
        }

        if (!cekStatusAnggota(anggota.getIdAnggota())) {
            System.out.println("[ValidasiPeminjaman] Gagal: anggota tidak aktif — " + anggota.getNama());
            return false;
        }

        if (!cekStok(dokumen)) {
            System.out.println("[ValidasiPeminjaman] Gagal: stok habis — " + dokumen.getJudul());
            return false;
        }

        // Tentukan batas pinjam dari objek AnggotaAktif jika tersedia
        int batas = limitPinjam;
        if (anggota instanceof AnggotaAktif) {
            batas = ((AnggotaAktif) anggota).getBatasMaxPinjam();
        }

        if (!cekKuotaPinjam(anggota.getIdAnggota(), batas)) {
            System.out.println("[ValidasiPeminjaman] Gagal: kuota pinjam penuh — " + anggota.getNama());
            return false;
        }

        statusValid = true;
        return true;
    }

    // ===================== Pengecekan Individual =====================

    /**
     * Cek stok dokumen > 0.
     * Mode DB: query tabel buku (kolom stok).
     * Mode in-memory: tidak bisa dicek dari objek Dokumen abstrak,
     *                 sehingga selalu return true (delegate ke DB).
     */
    public boolean cekStok(Dokumen dokumen) {
        if (connection != null) {
            return cekStokDB(dokumen.getIdDokumen());
        }
        // Fallback in-memory: anggap tersedia (validasi stok butuh DB)
        System.out.println("[ValidasiPeminjaman] Mode in-memory: stok diasumsikan tersedia.");
        return true;
    }

    /**
     * Cek status anggota: harus 'AKTIF' di tabel anggota.
     * Mode DB: query langsung.
     * Mode in-memory: cek dari tipe objek (AnggotaAktif / AnggotaNonAktif).
     */
    public boolean cekStatusAnggota(String idAnggota) {
        if (connection != null) {
            return cekStatusAnggotaDB(idAnggota);
        }
        // Mode in-memory: tidak bisa cek hanya dari ID, return true sebagai default
        System.out.println("[ValidasiPeminjaman] Mode in-memory: gunakan isEligible(Anggota, Dokumen) untuk cek tipe.");
        return true;
    }

    /**
     * Cek jumlah peminjaman aktif anggota < batas.
     * Mode DB: hitung dari tabel peminjaman WHERE status='AKTIF'.
     * Mode in-memory: tidak bisa dihitung, return true sebagai fallback.
     */
    private boolean cekKuotaPinjam(String idAnggota, int batas) {
        if (connection != null) {
            return cekKuotaPinjamDB(idAnggota, batas);
        }
        System.out.println("[ValidasiPeminjaman] Mode in-memory: kuota pinjam tidak diperiksa.");
        return true;
    }

    // ===================== Versi DB =====================

    private boolean cekStokDB(String idDokumen) {
        String sql = "SELECT stok FROM buku WHERE id_dokumen = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, idDokumen);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("stok") > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("[ValidasiPeminjaman] cekStok error: " + e.getMessage());
        }
        // Majalah tidak punya stok — dianggap selalu tersedia
        return true;
    }

    private boolean cekStatusAnggotaDB(String idAnggota) {
        String sql = "SELECT tipe_anggota FROM anggota WHERE id_anggota = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, idAnggota);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return "AKTIF".equalsIgnoreCase(rs.getString("tipe_anggota"));
                }
            }
        } catch (SQLException e) {
            System.err.println("[ValidasiPeminjaman] cekStatusAnggota error: " + e.getMessage());
        }
        return false;
    }

    private boolean cekKuotaPinjamDB(String idAnggota, int batas) {
        String sql = "SELECT COUNT(*) FROM peminjaman WHERE id_anggota = ? AND status = 'AKTIF'";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, idAnggota);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) < batas;
                }
            }
        } catch (SQLException e) {
            System.err.println("[ValidasiPeminjaman] cekKuotaPinjam error: " + e.getMessage());
        }
        return false;
    }

    // ===================== Getter & Setter =====================

    public int     getLimitPinjam()              { return limitPinjam; }
    public void    setLimitPinjam(int limit)     { this.limitPinjam = limit; }

    public boolean isStatusValid()               { return statusValid; }
}
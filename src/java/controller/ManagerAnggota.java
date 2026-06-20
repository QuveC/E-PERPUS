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
import model.AnggotaNonAktif;

/**
 * Controller CRUD untuk keanggotaan perpustakaan.
 * Tabel: anggota, anggota_aktif, anggota_nonaktif.
 * Butuh: MySQL Connector/J di Libraries NetBeans + class util/DBConnection.java.
 * @author [nama kamu]
 */
public class ManagerAnggota {

    private Connection connection;

    public ManagerAnggota(Connection connection) {
        this.connection = connection;
    }

    // ==================== TAMBAH ====================

    /** Tambah anggota aktif baru. INSERT ke tabel anggota + anggota_aktif (transaksi). */
    public boolean tambahAnggotaAktif(AnggotaAktif anggota) {
        String sqlAnggota = "INSERT INTO anggota (id_anggota, nama, email, no_telepon, alamat, role, tipe_anggota) "
                          + "VALUES (?, ?, ?, ?, ?, ?, 'AKTIF')";
        String sqlAktif   = "INSERT INTO anggota_aktif (id_anggota, tanggal_bergabung, batas_max_pinjam) "
                          + "VALUES (?, ?, ?)";
        try {
            connection.setAutoCommit(false);
            try (PreparedStatement ps = connection.prepareStatement(sqlAnggota)) {
                ps.setString(1, anggota.getIdAnggota());
                ps.setString(2, anggota.getNama());
                ps.setString(3, anggota.getEmail());
                ps.setString(4, anggota.getNoTelepon());
                ps.setString(5, anggota.getAlamat());
                ps.setString(6, anggota.getRole());
                ps.executeUpdate();
            }
            try (PreparedStatement ps = connection.prepareStatement(sqlAktif)) {
                ps.setString(1, anggota.getIdAnggota());
                ps.setDate(2, java.sql.Date.valueOf(anggota.getTanggalBergabung()));
                ps.setInt(3, anggota.getBatasMaxPinjam());
                ps.executeUpdate();
            }
            connection.commit();
            return true;
        } catch (SQLException e) {
            try { connection.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            System.err.println("[ManagerAnggota] tambahAnggotaAktif gagal: " + e.getMessage());
            return false;
        } finally {
            try { connection.setAutoCommit(true); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    /** Tambah anggota non-aktif. INSERT ke tabel anggota + anggota_nonaktif (transaksi). */
    public boolean tambahAnggotaNonAktif(AnggotaNonAktif anggota) {
        String sqlAnggota  = "INSERT INTO anggota (id_anggota, nama, email, no_telepon, alamat, role, tipe_anggota) "
                           + "VALUES (?, ?, ?, ?, ?, ?, 'NON_AKTIF')";
        String sqlNonAktif = "INSERT INTO anggota_nonaktif (id_anggota, tanggal_nonaktif, alasan_nonaktif) "
                           + "VALUES (?, ?, ?)";
        try {
            connection.setAutoCommit(false);
            try (PreparedStatement ps = connection.prepareStatement(sqlAnggota)) {
                ps.setString(1, anggota.getIdAnggota());
                ps.setString(2, anggota.getNama());
                ps.setString(3, anggota.getEmail());
                ps.setString(4, anggota.getNoTelepon());
                ps.setString(5, anggota.getAlamat());
                ps.setString(6, anggota.getRole());
                ps.executeUpdate();
            }
            try (PreparedStatement ps = connection.prepareStatement(sqlNonAktif)) {
                ps.setString(1, anggota.getIdAnggota());
                ps.setDate(2, java.sql.Date.valueOf(anggota.getTanggalNonaktif()));
                ps.setString(3, anggota.getAlasanNonaktif());
                ps.executeUpdate();
            }
            connection.commit();
            return true;
        } catch (SQLException e) {
            try { connection.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            System.err.println("[ManagerAnggota] tambahAnggotaNonAktif gagal: " + e.getMessage());
            return false;
        } finally {
            try { connection.setAutoCommit(true); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    // ==================== READ ====================

    /** Ambil semua anggota aktif. JOIN tabel anggota + anggota_aktif. */
    public List<AnggotaAktif> getAllAnggotaAktif() {
        List<AnggotaAktif> list = new ArrayList<>();
        String sql = "SELECT a.id_anggota, a.nama, a.email, a.no_telepon, a.alamat, a.role, "
                   + "aa.tanggal_bergabung, aa.batas_max_pinjam "
                   + "FROM anggota a INNER JOIN anggota_aktif aa ON a.id_anggota = aa.id_anggota";
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new AnggotaAktif(
                    rs.getString("id_anggota"),
                    rs.getString("nama"),
                    rs.getString("email"),
                    rs.getString("no_telepon"),
                    rs.getString("alamat"),
                    rs.getString("role"),
                    rs.getDate("tanggal_bergabung").toLocalDate(),
                    rs.getInt("batas_max_pinjam")
                ));
            }
        } catch (SQLException e) {
            System.err.println("[ManagerAnggota] getAllAnggotaAktif gagal: " + e.getMessage());
        }
        return list;
    }

    /** Ambil semua anggota non-aktif. JOIN tabel anggota + anggota_nonaktif. */
    public List<AnggotaNonAktif> getAllAnggotaNonAktif() {
        List<AnggotaNonAktif> list = new ArrayList<>();
        String sql = "SELECT a.id_anggota, a.nama, a.email, a.no_telepon, a.alamat, a.role, "
                   + "an.tanggal_nonaktif, an.alasan_nonaktif "
                   + "FROM anggota a INNER JOIN anggota_nonaktif an ON a.id_anggota = an.id_anggota";
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new AnggotaNonAktif(
                    rs.getString("id_anggota"),
                    rs.getString("nama"),
                    rs.getString("email"),
                    rs.getString("no_telepon"),
                    rs.getString("alamat"),
                    rs.getString("role"),
                    rs.getDate("tanggal_nonaktif").toLocalDate(),
                    rs.getString("alasan_nonaktif")
                ));
            }
        } catch (SQLException e) {
            System.err.println("[ManagerAnggota] getAllAnggotaNonAktif gagal: " + e.getMessage());
        }
        return list;
    }

    /** Cari anggota aktif berdasarkan id_anggota. Return null jika tidak ada. */
    public AnggotaAktif getAnggotaAktifById(String idAnggota) {
        String sql = "SELECT a.id_anggota, a.nama, a.email, a.no_telepon, a.alamat, a.role, "
                   + "aa.tanggal_bergabung, aa.batas_max_pinjam "
                   + "FROM anggota a INNER JOIN anggota_aktif aa ON a.id_anggota = aa.id_anggota "
                   + "WHERE a.id_anggota = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, idAnggota);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new AnggotaAktif(
                        rs.getString("id_anggota"),
                        rs.getString("nama"),
                        rs.getString("email"),
                        rs.getString("no_telepon"),
                        rs.getString("alamat"),
                        rs.getString("role"),
                        rs.getDate("tanggal_bergabung").toLocalDate(),
                        rs.getInt("batas_max_pinjam")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("[ManagerAnggota] getAnggotaAktifById gagal: " + e.getMessage());
        }
        return null;
    }

    /** Ambil semua anggota (aktif + non-aktif) sekaligus. */
    public List<Anggota> getAllAnggota() {
        List<Anggota> list = new ArrayList<>();
        list.addAll(getAllAnggotaAktif());
        list.addAll(getAllAnggotaNonAktif());
        return list;
    }

    // ==================== UPDATE ====================

    /** Update data umum anggota. UPDATE tabel anggota. */
    public boolean updateAnggota(Anggota anggota) {
        String sql = "UPDATE anggota SET nama=?, email=?, no_telepon=?, alamat=?, role=? "
                   + "WHERE id_anggota=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, anggota.getNama());
            ps.setString(2, anggota.getEmail());
            ps.setString(3, anggota.getNoTelepon());
            ps.setString(4, anggota.getAlamat());
            ps.setString(5, anggota.getRole());
            ps.setString(6, anggota.getIdAnggota());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[ManagerAnggota] updateAnggota gagal: " + e.getMessage());
            return false;
        }
    }

    /**
     * Nonaktifkan anggota aktif menjadi non-aktif (transaksi).
     * - UPDATE tabel anggota (tipe_anggota → NON_AKTIF)
     * - DELETE dari anggota_aktif
     * - INSERT ke anggota_nonaktif
     */
    public boolean nonaktifkanAnggota(String idAnggota, String alasan) {
        String sqlUpdateTipe   = "UPDATE anggota SET tipe_anggota='NON_AKTIF' WHERE id_anggota=?";
        String sqlDeleteAktif  = "DELETE FROM anggota_aktif WHERE id_anggota=?";
        String sqlInsertNonAktif = "INSERT INTO anggota_nonaktif (id_anggota, tanggal_nonaktif, alasan_nonaktif) "
                                 + "VALUES (?, ?, ?)";
        try {
            connection.setAutoCommit(false);
            try (PreparedStatement ps = connection.prepareStatement(sqlUpdateTipe)) {
                ps.setString(1, idAnggota);
                ps.executeUpdate();
            }
            try (PreparedStatement ps = connection.prepareStatement(sqlDeleteAktif)) {
                ps.setString(1, idAnggota);
                ps.executeUpdate();
            }
            try (PreparedStatement ps = connection.prepareStatement(sqlInsertNonAktif)) {
                ps.setString(1, idAnggota);
                ps.setDate(2, java.sql.Date.valueOf(LocalDate.now()));
                ps.setString(3, alasan);
                ps.executeUpdate();
            }
            connection.commit();
            return true;
        } catch (SQLException e) {
            try { connection.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            System.err.println("[ManagerAnggota] nonaktifkanAnggota gagal: " + e.getMessage());
            return false;
        } finally {
            try { connection.setAutoCommit(true); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    /**
     * Reaktivasi anggota non-aktif kembali menjadi aktif (transaksi).
     * - UPDATE tabel anggota (tipe_anggota → AKTIF)
     * - DELETE dari anggota_nonaktif
     * - INSERT ke anggota_aktif
     */
    public boolean reaktivasiAnggota(String idAnggota) {
        String sqlUpdateTipe     = "UPDATE anggota SET tipe_anggota='AKTIF' WHERE id_anggota=?";
        String sqlDeleteNonAktif = "DELETE FROM anggota_nonaktif WHERE id_anggota=?";
        String sqlInsertAktif    = "INSERT INTO anggota_aktif (id_anggota, tanggal_bergabung, batas_max_pinjam) "
                                 + "VALUES (?, ?, 3)";
        try {
            connection.setAutoCommit(false);
            try (PreparedStatement ps = connection.prepareStatement(sqlUpdateTipe)) {
                ps.setString(1, idAnggota);
                ps.executeUpdate();
            }
            try (PreparedStatement ps = connection.prepareStatement(sqlDeleteNonAktif)) {
                ps.setString(1, idAnggota);
                ps.executeUpdate();
            }
            try (PreparedStatement ps = connection.prepareStatement(sqlInsertAktif)) {
                ps.setString(1, idAnggota);
                ps.setDate(2, java.sql.Date.valueOf(LocalDate.now()));
                ps.executeUpdate();
            }
            connection.commit();
            return true;
        } catch (SQLException e) {
            try { connection.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            System.err.println("[ManagerAnggota] reaktivasiAnggota gagal: " + e.getMessage());
            return false;
        } finally {
            try { connection.setAutoCommit(true); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    // ==================== DELETE ====================

    /** Hapus anggota. DELETE dari tabel anggota (CASCADE otomatis ke anggota_aktif / anggota_nonaktif). */
    public boolean hapusAnggota(String idAnggota) {
        String sql = "DELETE FROM anggota WHERE id_anggota=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, idAnggota);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[ManagerAnggota] hapusAnggota gagal: " + e.getMessage());
            return false;
        }
    }

    // ==================== UTILITAS ====================

    /** Cek apakah id_anggota sudah ada di database. */
    public boolean isIdAnggotaExist(String idAnggota) {
        String sql = "SELECT COUNT(*) FROM anggota WHERE id_anggota=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, idAnggota);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("[ManagerAnggota] isIdAnggotaExist gagal: " + e.getMessage());
        }
        return false;
    }

    /** Cek apakah email sudah dipakai anggota lain. */
    public boolean isEmailExist(String email) {
        String sql = "SELECT COUNT(*) FROM anggota WHERE email=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("[ManagerAnggota] isEmailExist gagal: " + e.getMessage());
        }
        return false;
    }
}

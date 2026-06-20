package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Buku;
import model.Dokumen;
import model.Majalah;

/**
 * Controller CRUD untuk koleksi dokumen (Buku & Majalah).
 * Tabel: dokumen, buku, majalah.
 * Butuh: MySQL Connector/J di Libraries NetBeans + class util/DBConnection.java.
 * @author Farel
 */
public class ManagerKoleksi {

    private Connection connection;

    public ManagerKoleksi(Connection connection) {
        this.connection = connection;
    }

    // ==================== BUKU ====================

    /** Tambah buku baru. INSERT ke tabel dokumen + buku (transaksi). */
    public boolean tambahBuku(Buku buku) {
        String sqlDokumen = "INSERT INTO dokumen (id_dokumen, judul, pengarang, tahun_terbit, tipe_dokumen) "
                          + "VALUES (?, ?, ?, ?, 'BUKU')";
        String sqlBuku    = "INSERT INTO buku (id_dokumen, isbn, penerbit, jumlah_halaman, stok, lokasi_rak) "
                          + "VALUES (?, ?, ?, ?, ?, ?)";
        try {
            connection.setAutoCommit(false);
            try (PreparedStatement psDokumen = connection.prepareStatement(sqlDokumen)) {
                psDokumen.setString(1, buku.getIdDokumen());
                psDokumen.setString(2, buku.getJudul());
                psDokumen.setString(3, buku.getPengarang());
                psDokumen.setInt(4, buku.getTahunTerbit());
                psDokumen.executeUpdate();
            }
            try (PreparedStatement psBuku = connection.prepareStatement(sqlBuku)) {
                psBuku.setString(1, buku.getIdDokumen());
                psBuku.setString(2, buku.getIsbn());
                psBuku.setString(3, buku.getPenerbit());
                psBuku.setInt(4, buku.getJumlahHalaman());
                psBuku.setInt(5, buku.getStok());
                psBuku.setString(6, buku.getLokasiRak());
                psBuku.executeUpdate();
            }
            connection.commit();
            return true;
        } catch (SQLException e) {
            try { connection.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            System.err.println("[ManagerKoleksi] tambahBuku gagal: " + e.getMessage());
            return false;
        } finally {
            try { connection.setAutoCommit(true); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    /** Ambil semua buku. JOIN tabel dokumen + buku. */
    public List<Buku> getAllBuku() {
        List<Buku> list = new ArrayList<>();
        String sql = "SELECT d.id_dokumen, d.judul, d.pengarang, d.tahun_terbit, "
                   + "b.isbn, b.penerbit, b.jumlah_halaman, b.stok, b.lokasi_rak "
                   + "FROM dokumen d INNER JOIN buku b ON d.id_dokumen = b.id_dokumen";
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Buku(
                    rs.getString("id_dokumen"), rs.getString("judul"),
                    rs.getString("pengarang"), rs.getInt("tahun_terbit"),
                    rs.getString("isbn"), rs.getString("penerbit"),
                    rs.getInt("jumlah_halaman"), rs.getInt("stok"),
                    rs.getString("lokasi_rak")
                ));
            }
        } catch (SQLException e) {
            System.err.println("[ManagerKoleksi] getAllBuku gagal: " + e.getMessage());
        }
        return list;
    }

    /** Cari buku berdasarkan id_dokumen. Return null jika tidak ada. */
    public Buku getBukuById(String idDokumen) {
        String sql = "SELECT d.id_dokumen, d.judul, d.pengarang, d.tahun_terbit, "
                   + "b.isbn, b.penerbit, b.jumlah_halaman, b.stok, b.lokasi_rak "
                   + "FROM dokumen d INNER JOIN buku b ON d.id_dokumen = b.id_dokumen "
                   + "WHERE d.id_dokumen = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, idDokumen);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Buku(
                        rs.getString("id_dokumen"), rs.getString("judul"),
                        rs.getString("pengarang"), rs.getInt("tahun_terbit"),
                        rs.getString("isbn"), rs.getString("penerbit"),
                        rs.getInt("jumlah_halaman"), rs.getInt("stok"),
                        rs.getString("lokasi_rak")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("[ManagerKoleksi] getBukuById gagal: " + e.getMessage());
        }
        return null;
    }

    /** Update data buku. UPDATE tabel dokumen + buku (transaksi). */
    public boolean updateBuku(Buku buku) {
        String sqlDokumen = "UPDATE dokumen SET judul=?, pengarang=?, tahun_terbit=? WHERE id_dokumen=?";
        String sqlBuku    = "UPDATE buku SET isbn=?, penerbit=?, jumlah_halaman=?, stok=?, lokasi_rak=? WHERE id_dokumen=?";
        try {
            connection.setAutoCommit(false);
            try (PreparedStatement ps = connection.prepareStatement(sqlDokumen)) {
                ps.setString(1, buku.getJudul());
                ps.setString(2, buku.getPengarang());
                ps.setInt(3, buku.getTahunTerbit());
                ps.setString(4, buku.getIdDokumen());
                ps.executeUpdate();
            }
            try (PreparedStatement ps = connection.prepareStatement(sqlBuku)) {
                ps.setString(1, buku.getIsbn());
                ps.setString(2, buku.getPenerbit());
                ps.setInt(3, buku.getJumlahHalaman());
                ps.setInt(4, buku.getStok());
                ps.setString(5, buku.getLokasiRak());
                ps.setString(6, buku.getIdDokumen());
                ps.executeUpdate();
            }
            connection.commit();
            return true;
        } catch (SQLException e) {
            try { connection.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            System.err.println("[ManagerKoleksi] updateBuku gagal: " + e.getMessage());
            return false;
        } finally {
            try { connection.setAutoCommit(true); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    /** Update hanya kolom stok buku (dipakai saat peminjaman/pengembalian). */
    public boolean updateStokBuku(String idDokumen, int stokBaru) {
        String sql = "UPDATE buku SET stok=? WHERE id_dokumen=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, stokBaru);
            ps.setString(2, idDokumen);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[ManagerKoleksi] updateStokBuku gagal: " + e.getMessage());
            return false;
        }
    }

    /** Hapus buku. DELETE dari tabel dokumen (cascade ke tabel buku otomatis). */
    public boolean hapusBuku(String idDokumen) {
        String sqlPeminjaman = "DELETE FROM peminjaman WHERE id_dokumen=?";
        String sqlBuku       = "DELETE FROM buku WHERE id_dokumen=?";
        String sqlDokumen    = "DELETE FROM dokumen WHERE id_dokumen=? AND tipe_dokumen='BUKU'";
        try {
            connection.setAutoCommit(false);
            try (PreparedStatement psPjm = connection.prepareStatement(sqlPeminjaman)) {
                psPjm.setString(1, idDokumen);
                psPjm.executeUpdate();
            }
            try (PreparedStatement psBuku = connection.prepareStatement(sqlBuku)) {
                psBuku.setString(1, idDokumen);
                psBuku.executeUpdate();
            }
            int affected = 0;
            try (PreparedStatement psDokumen = connection.prepareStatement(sqlDokumen)) {
                psDokumen.setString(1, idDokumen);
                affected = psDokumen.executeUpdate();
            }
            connection.commit();
            return affected > 0;
        } catch (SQLException e) {
            try { connection.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            System.err.println("[ManagerKoleksi] hapusBuku gagal: " + e.getMessage());
            return false;
        } finally {
            try { connection.setAutoCommit(true); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    // ==================== MAJALAH ====================

    /** Tambah majalah baru. INSERT ke tabel dokumen + majalah (transaksi). */
    public boolean tambahMajalah(Majalah majalah) {
        String sqlDokumen = "INSERT INTO dokumen (id_dokumen, judul, pengarang, tahun_terbit, tipe_dokumen) "
                          + "VALUES (?, ?, ?, ?, 'MAJALAH')";
        String sqlMajalah = "INSERT INTO majalah (id_dokumen, edisi, bulan_terbit, frekuensi_terbit, lokasi_rak) "
                          + "VALUES (?, ?, ?, ?, ?)";
        try {
            connection.setAutoCommit(false);
            try (PreparedStatement ps = connection.prepareStatement(sqlDokumen)) {
                ps.setString(1, majalah.getIdDokumen());
                ps.setString(2, majalah.getJudul());
                ps.setString(3, majalah.getPengarang());
                ps.setInt(4, majalah.getTahunTerbit());
                ps.executeUpdate();
            }
            try (PreparedStatement ps = connection.prepareStatement(sqlMajalah)) {
                ps.setString(1, majalah.getIdDokumen());
                ps.setString(2, majalah.getEdisi());
                ps.setString(3, majalah.getBulanTerbit());
                ps.setString(4, majalah.getFrekuensiTerbit());
                ps.setString(5, majalah.getLokasiRak());
                ps.executeUpdate();
            }
            connection.commit();
            return true;
        } catch (SQLException e) {
            try { connection.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            System.err.println("[ManagerKoleksi] tambahMajalah gagal: " + e.getMessage());
            return false;
        } finally {
            try { connection.setAutoCommit(true); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    /** Ambil semua majalah. JOIN tabel dokumen + majalah. */
    public List<Majalah> getAllMajalah() {
        List<Majalah> list = new ArrayList<>();
        String sql = "SELECT d.id_dokumen, d.judul, d.pengarang, d.tahun_terbit, "
                   + "m.edisi, m.bulan_terbit, m.frekuensi_terbit, m.lokasi_rak "
                   + "FROM dokumen d INNER JOIN majalah m ON d.id_dokumen = m.id_dokumen";
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Majalah(
                    rs.getString("id_dokumen"), rs.getString("judul"),
                    rs.getString("pengarang"), rs.getInt("tahun_terbit"),
                    rs.getString("edisi"), rs.getString("bulan_terbit"),
                    rs.getString("frekuensi_terbit"), rs.getString("lokasi_rak")
                ));
            }
        } catch (SQLException e) {
            System.err.println("[ManagerKoleksi] getAllMajalah gagal: " + e.getMessage());
        }
        return list;
    }

    /** Cari majalah berdasarkan id_dokumen. Return null jika tidak ada. */
    public Majalah getMajalahById(String idDokumen) {
        String sql = "SELECT d.id_dokumen, d.judul, d.pengarang, d.tahun_terbit, "
                   + "m.edisi, m.bulan_terbit, m.frekuensi_terbit, m.lokasi_rak "
                   + "FROM dokumen d INNER JOIN majalah m ON d.id_dokumen = m.id_dokumen "
                   + "WHERE d.id_dokumen = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, idDokumen);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Majalah(
                        rs.getString("id_dokumen"), rs.getString("judul"),
                        rs.getString("pengarang"), rs.getInt("tahun_terbit"),
                        rs.getString("edisi"), rs.getString("bulan_terbit"),
                        rs.getString("frekuensi_terbit"), rs.getString("lokasi_rak")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("[ManagerKoleksi] getMajalahById gagal: " + e.getMessage());
        }
        return null;
    }

    /** Update data majalah. UPDATE tabel dokumen + majalah (transaksi). */
    public boolean updateMajalah(Majalah majalah) {
        String sqlDokumen = "UPDATE dokumen SET judul=?, pengarang=?, tahun_terbit=? WHERE id_dokumen=?";
        String sqlMajalah = "UPDATE majalah SET edisi=?, bulan_terbit=?, frekuensi_terbit=?, lokasi_rak=? WHERE id_dokumen=?";
        try {
            connection.setAutoCommit(false);
            try (PreparedStatement ps = connection.prepareStatement(sqlDokumen)) {
                ps.setString(1, majalah.getJudul());
                ps.setString(2, majalah.getPengarang());
                ps.setInt(3, majalah.getTahunTerbit());
                ps.setString(4, majalah.getIdDokumen());
                ps.executeUpdate();
            }
            try (PreparedStatement ps = connection.prepareStatement(sqlMajalah)) {
                ps.setString(1, majalah.getEdisi());
                ps.setString(2, majalah.getBulanTerbit());
                ps.setString(3, majalah.getFrekuensiTerbit());
                ps.setString(4, majalah.getLokasiRak());
                ps.setString(5, majalah.getIdDokumen());
                ps.executeUpdate();
            }
            connection.commit();
            return true;
        } catch (SQLException e) {
            try { connection.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            System.err.println("[ManagerKoleksi] updateMajalah gagal: " + e.getMessage());
            return false;
        } finally {
            try { connection.setAutoCommit(true); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    /** Hapus majalah. DELETE dari tabel dokumen (cascade ke tabel majalah otomatis). */
    public boolean hapusMajalah(String idDokumen) {
        String sqlPeminjaman = "DELETE FROM peminjaman WHERE id_dokumen=?";
        String sqlMajalah    = "DELETE FROM majalah WHERE id_dokumen=?";
        String sqlDokumen    = "DELETE FROM dokumen WHERE id_dokumen=? AND tipe_dokumen='MAJALAH'";
        try {
            connection.setAutoCommit(false);
            try (PreparedStatement psPjm = connection.prepareStatement(sqlPeminjaman)) {
                psPjm.setString(1, idDokumen);
                psPjm.executeUpdate();
            }
            try (PreparedStatement psMajalah = connection.prepareStatement(sqlMajalah)) {
                psMajalah.setString(1, idDokumen);
                psMajalah.executeUpdate();
            }
            int affected = 0;
            try (PreparedStatement psDokumen = connection.prepareStatement(sqlDokumen)) {
                psDokumen.setString(1, idDokumen);
                affected = psDokumen.executeUpdate();
            }
            connection.commit();
            return affected > 0;
        } catch (SQLException e) {
            try { connection.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            System.err.println("[ManagerKoleksi] hapusMajalah gagal: " + e.getMessage());
            return false;
        } finally {
            try { connection.setAutoCommit(true); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    // ==================== UMUM ====================

    /** Ambil semua dokumen (buku + majalah) sekaligus. */
    public List<Dokumen> getAllDokumen() {
        List<Dokumen> list = new ArrayList<>();
        list.addAll(getAllBuku());
        list.addAll(getAllMajalah());
        return list;
    }

    /** Cek apakah id_dokumen sudah ada di database. */
    public boolean isIdDokumenExist(String idDokumen) {
        String sql = "SELECT COUNT(*) FROM dokumen WHERE id_dokumen=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, idDokumen);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("[ManagerKoleksi] isIdDokumenExist gagal: " + e.getMessage());
        }
        return false;
    }

    /** Cari dokumen (Buku atau Majalah) berdasarkan id_dokumen. */
    public Dokumen cariDokumen(String idDokumen) {
        Buku b = getBukuById(idDokumen);
        if (b != null) return b;
        return getMajalahById(idDokumen);
    }

    /** Tampilkan info semua dokumen. */
    public void tampilkanSemua() {
        for (Dokumen d : getAllDokumen()) {
            System.out.println(d.getDetail());
        }
    }
}

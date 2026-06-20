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
 * Menyediakan berbagai metode pencarian dokumen di katalog perpustakaan.
 * Pencarian dilakukan case-insensitive (LIKE %kata%).
 * Tabel: dokumen, buku, majalah.
 * @author Farabi AM
 */
public class CatalogSearcher {

    private List<Dokumen> daftarDokumen;
    private Connection connection;

    // ===================== Constructor =====================

    public CatalogSearcher(List<Dokumen> daftar) {
        this.daftarDokumen = (daftar != null) ? daftar : new ArrayList<>();
    }

    public CatalogSearcher(Connection connection) {
        this.connection    = connection;
        this.daftarDokumen = new ArrayList<>();
    }

    // ===================== Metode Pencarian =====================

    /**
     * Cari dokumen berdasarkan kata kunci pada judul (case-insensitive).
     */
    public List<Dokumen> cariByJudul(String kata) {
        if (connection != null) {
            return cariByJudulDB(kata);
        }
        List<Dokumen> hasil = new ArrayList<>();
        if (kata == null || kata.isBlank()) return hasil;
        String kataLower = kata.toLowerCase();
        for (Dokumen d : daftarDokumen) {
            if (d.getJudul() != null && d.getJudul().toLowerCase().contains(kataLower)) {
                hasil.add(d);
            }
        }
        return hasil;
    }

    /**
     * Cari dokumen berdasarkan nama pengarang (case-insensitive).
     */
    public List<Dokumen> cariByPengarang(String nama) {
        if (connection != null) {
            return cariByPengarangDB(nama);
        }
        List<Dokumen> hasil = new ArrayList<>();
        if (nama == null || nama.isBlank()) return hasil;
        String namaLower = nama.toLowerCase();
        for (Dokumen d : daftarDokumen) {
            if (d.getPengarang() != null && d.getPengarang().toLowerCase().contains(namaLower)) {
                hasil.add(d);
            }
        }
        return hasil;
    }

    /**
     * Cari dokumen berdasarkan tahun terbit.
     */
    public List<Dokumen> cariByTahun(int tahun) {
        if (connection != null) {
            return cariByTahunDB(tahun);
        }
        List<Dokumen> hasil = new ArrayList<>();
        for (Dokumen d : daftarDokumen) {
            if (d.getTahunTerbit() == tahun) {
                hasil.add(d);
            }
        }
        return hasil;
    }

    /**
     * Cari dokumen berdasarkan jenis/tipe ("BUKU" atau "MAJALAH").
     */
    public List<Dokumen> cariByJenis(String jenis) {
        if (connection != null) {
            return cariByJenisDB(jenis);
        }
        List<Dokumen> hasil = new ArrayList<>();
        if (jenis == null || jenis.isBlank()) return hasil;
        String jenisUpper = jenis.toUpperCase();
        for (Dokumen d : daftarDokumen) {
            if (d.getTipeDokumen() != null && d.getTipeDokumen().toUpperCase().equals(jenisUpper)) {
                hasil.add(d);
            }
        }
        return hasil;
    }

    /**
     * Cari dokumen berdasarkan lokasi rak (hanya Buku dan Majalah yang punya lokasiRak).
     */
    public List<Dokumen> cariByLokasi(String lokasiRak) {
        if (connection != null) {
            return cariByLokasiDB(lokasiRak);
        }
        List<Dokumen> hasil = new ArrayList<>();
        if (lokasiRak == null || lokasiRak.isBlank()) return hasil;
        String lokasiLower = lokasiRak.toLowerCase();
        for (Dokumen d : daftarDokumen) {
            String lokasi = null;
            if (d instanceof Buku) {
                lokasi = ((Buku) d).getLokasiRak();
            } else if (d instanceof Majalah) {
                lokasi = ((Majalah) d).getLokasiRak();
            }
            if (lokasi != null && lokasi.toLowerCase().contains(lokasiLower)) {
                hasil.add(d);
            }
        }
        return hasil;
    }

    /**
     * Tampilkan semua hasil pencarian ke konsol.
     */
    public void tampilkanHasil(List<Dokumen> hasil) {
        if (hasil == null || hasil.isEmpty()) {
            System.out.println("[CatalogSearcher] Tidak ada dokumen yang ditemukan.");
            return;
        }
        System.out.println("[CatalogSearcher] Ditemukan " + hasil.size() + " dokumen:");
        for (Dokumen d : hasil) {
            System.out.println("  - " + d.getDetail());
        }
    }

    // ===================== Versi DB (JDBC) =====================

    private List<Dokumen> cariByJudulDB(String kata) {
        return cariGenericDB("judul", kata);
    }

    private List<Dokumen> cariByPengarangDB(String nama) {
        return cariGenericDB("pengarang", nama);
    }

    private List<Dokumen> cariByTahunDB(int tahun) {
        List<Dokumen> hasil = new ArrayList<>();
        String sql = "SELECT d.id_dokumen, d.judul, d.pengarang, d.tahun_terbit, d.tipe_dokumen, "
                   + "b.isbn, b.penerbit, b.jumlah_halaman, b.stok, b.lokasi_rak, "
                   + "m.edisi, m.bulan_terbit, m.frekuensi_terbit, m.lokasi_rak AS m_lokasi_rak "
                   + "FROM dokumen d "
                   + "LEFT JOIN buku b ON d.id_dokumen = b.id_dokumen "
                   + "LEFT JOIN majalah m ON d.id_dokumen = m.id_dokumen "
                   + "WHERE d.tahun_terbit = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, tahun);
            hasil = mapResultSet(ps.executeQuery());
        } catch (SQLException e) {
            System.err.println("[CatalogSearcher] cariByTahun error: " + e.getMessage());
        }
        return hasil;
    }

    private List<Dokumen> cariByJenisDB(String jenis) {
        List<Dokumen> hasil = new ArrayList<>();
        String sql = "SELECT d.id_dokumen, d.judul, d.pengarang, d.tahun_terbit, d.tipe_dokumen, "
                   + "b.isbn, b.penerbit, b.jumlah_halaman, b.stok, b.lokasi_rak, "
                   + "m.edisi, m.bulan_terbit, m.frekuensi_terbit, m.lokasi_rak AS m_lokasi_rak "
                   + "FROM dokumen d "
                   + "LEFT JOIN buku b ON d.id_dokumen = b.id_dokumen "
                   + "LEFT JOIN majalah m ON d.id_dokumen = m.id_dokumen "
                   + "WHERE UPPER(d.tipe_dokumen) = UPPER(?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, jenis);
            hasil = mapResultSet(ps.executeQuery());
        } catch (SQLException e) {
            System.err.println("[CatalogSearcher] cariByJenis error: " + e.getMessage());
        }
        return hasil;
    }

    private List<Dokumen> cariByLokasiDB(String lokasi) {
        List<Dokumen> hasil = new ArrayList<>();
        String sql = "SELECT d.id_dokumen, d.judul, d.pengarang, d.tahun_terbit, d.tipe_dokumen, "
                   + "b.isbn, b.penerbit, b.jumlah_halaman, b.stok, b.lokasi_rak, "
                   + "m.edisi, m.bulan_terbit, m.frekuensi_terbit, m.lokasi_rak AS m_lokasi_rak "
                   + "FROM dokumen d "
                   + "LEFT JOIN buku b ON d.id_dokumen = b.id_dokumen "
                   + "LEFT JOIN majalah m ON d.id_dokumen = m.id_dokumen "
                   + "WHERE b.lokasi_rak LIKE ? OR m.lokasi_rak LIKE ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, "%" + lokasi + "%");
            ps.setString(2, "%" + lokasi + "%");
            hasil = mapResultSet(ps.executeQuery());
        } catch (SQLException e) {
            System.err.println("[CatalogSearcher] cariByLokasi error: " + e.getMessage());
        }
        return hasil;
    }

    /** Helper pencarian LIKE pada kolom string (judul / pengarang). */
    private List<Dokumen> cariGenericDB(String kolom, String nilai) {
        List<Dokumen> hasil = new ArrayList<>();
        if (nilai == null || nilai.isBlank()) return hasil;
        String sql = "SELECT d.id_dokumen, d.judul, d.pengarang, d.tahun_terbit, d.tipe_dokumen, "
                   + "b.isbn, b.penerbit, b.jumlah_halaman, b.stok, b.lokasi_rak, "
                   + "m.edisi, m.bulan_terbit, m.frekuensi_terbit, m.lokasi_rak AS m_lokasi_rak "
                   + "FROM dokumen d "
                   + "LEFT JOIN buku b ON d.id_dokumen = b.id_dokumen "
                   + "LEFT JOIN majalah m ON d.id_dokumen = m.id_dokumen "
                   + "WHERE LOWER(d." + kolom + ") LIKE LOWER(?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, "%" + nilai + "%");
            hasil = mapResultSet(ps.executeQuery());
        } catch (SQLException e) {
            System.err.println("[CatalogSearcher] cari" + kolom + " error: " + e.getMessage());
        }
        return hasil;
    }

    /** Map ResultSet ke List<Dokumen> (Buku / Majalah). */
    private List<Dokumen> mapResultSet(ResultSet rs) throws SQLException {
        List<Dokumen> list = new ArrayList<>();
        while (rs.next()) {
            String tipe = rs.getString("tipe_dokumen");
            if ("BUKU".equalsIgnoreCase(tipe)) {
                list.add(new Buku(
                    rs.getString("id_dokumen"),
                    rs.getString("judul"),
                    rs.getString("pengarang"),
                    rs.getInt("tahun_terbit"),
                    rs.getString("isbn"),
                    rs.getString("penerbit"),
                    rs.getInt("jumlah_halaman"),
                    rs.getInt("stok"),
                    rs.getString("lokasi_rak")
                ));
            } else if ("MAJALAH".equalsIgnoreCase(tipe)) {
                list.add(new Majalah(
                    rs.getString("id_dokumen"),
                    rs.getString("judul"),
                    rs.getString("pengarang"),
                    rs.getInt("tahun_terbit"),
                    rs.getString("edisi"),
                    rs.getString("bulan_terbit"),
                    rs.getString("frekuensi_terbit"),
                    rs.getString("m_lokasi_rak")
                ));
            }
        }
        return list;
    }

    public List<Dokumen> getAllDokumenDB() {
        if (connection == null) {
            return daftarDokumen;
        }
        List<Dokumen> hasil = new ArrayList<>();
        String sql = "SELECT d.id_dokumen, d.judul, d.pengarang, d.tahun_terbit, d.tipe_dokumen, "
                   + "b.isbn, b.penerbit, b.jumlah_halaman, b.stok, b.lokasi_rak, "
                   + "m.edisi, m.bulan_terbit, m.frekuensi_terbit, m.lokasi_rak AS m_lokasi_rak "
                   + "FROM dokumen d "
                   + "LEFT JOIN buku b ON d.id_dokumen = b.id_dokumen "
                   + "LEFT JOIN majalah m ON d.id_dokumen = m.id_dokumen";
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            hasil = mapResultSet(rs);
        } catch (SQLException e) {
            System.err.println("[CatalogSearcher] getAllDokumenDB error: " + e.getMessage());
        }
        return hasil;
    }

    // ===================== Getter / Setter =====================

    public List<Dokumen> getDaftarDokumen()                        { return daftarDokumen; }
    public void          setDaftarDokumen(List<Dokumen> daftar)    { this.daftarDokumen = daftar; }
}

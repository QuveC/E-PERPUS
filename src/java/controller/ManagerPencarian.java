package controller;

import java.util.List;
import model.Dokumen;
import model.HasilPencarian;

/**
 * Mengkoordinasikan proses pencarian dokumen menggunakan CatalogSearcher
 * dan mengemas hasilnya ke dalam HasilPencarian.
 *
 * Mendukung pencarian berdasarkan:
 *   - judul
 *   - pengarang
 *   - tahun terbit
 *   - jenis dokumen (BUKU / MAJALAH)
 *   - lokasi rak
 *
 * Format kriteria: "JUDUL:<kata>", "PENGARANG:<nama>", "TAHUN:<tahun>",
 *                  "JENIS:<tipe>", "LOKASI:<rak>".
 * Jika tidak ada prefix, pencarian dilakukan pada judul (default).
 *
 * @author Farabi AM
 */
public class ManagerPencarian {

    private CatalogSearcher catalogSearcher;

    // ===================== Constructor =====================

    public ManagerPencarian(CatalogSearcher searcher) {
        this.catalogSearcher = searcher;
    }

    // ===================== Metode Utama =====================

    /**
     * Proses pencarian berdasarkan string kriteria.
     * Contoh: "JUDUL:Java", "PENGARANG:Suyanto", "TAHUN:2020", "JENIS:BUKU", "LOKASI:A1"
     * Tanpa prefix → dianggap pencarian judul.
     */
    public void prosesPencarian(String kriteria) {
        HasilPencarian hasil = cari(kriteria);
        tampilkanSemuaHasilPencarian(hasil);
    }

    /**
     * Lakukan pencarian dan kembalikan HasilPencarian (tanpa mencetak).
     */
    public HasilPencarian cari(String kriteria) {
        if (kriteria == null || kriteria.isBlank()) {
            List<Dokumen> dokumen = catalogSearcher.getAllDokumenDB();
            return new HasilPencarian(dokumen, "");
        }

        String upper    = kriteria.trim().toUpperCase();
        List<Dokumen> dokumen;

        if (upper.startsWith("JUDUL:")) {
            String kata = kriteria.substring(6).trim();
            dokumen = catalogSearcher.cariByJudul(kata);
        } else if (upper.startsWith("PENGARANG:")) {
            String nama = kriteria.substring(10).trim();
            dokumen = catalogSearcher.cariByPengarang(nama);
        } else if (upper.startsWith("TAHUN:")) {
            try {
                int tahun = Integer.parseInt(kriteria.substring(6).trim());
                dokumen = catalogSearcher.cariByTahun(tahun);
            } catch (NumberFormatException e) {
                System.err.println("[ManagerPencarian] Format tahun tidak valid: " + kriteria);
                dokumen = null;
            }
        } else if (upper.startsWith("JENIS:")) {
            String jenis = kriteria.substring(6).trim();
            dokumen = catalogSearcher.cariByJenis(jenis);
        } else if (upper.startsWith("LOKASI:")) {
            String lokasi = kriteria.substring(7).trim();
            dokumen = catalogSearcher.cariByLokasi(lokasi);
        } else {
            // Default: cari berdasarkan judul
            dokumen = catalogSearcher.cariByJudul(kriteria.trim());
        }

        return new HasilPencarian(dokumen, kriteria);
    }

    /**
     * Cetak semua hasil pencarian ke konsol.
     */
    public void tampilkanSemuaHasilPencarian(HasilPencarian hasil) {
        if (hasil == null) {
            System.out.println("[ManagerPencarian] Tidak ada hasil pencarian.");
            return;
        }
        System.out.println("================================================");
        System.out.println("[ManagerPencarian] Hasil pencarian untuk: \"" + hasil.getKataKunci() + "\"");
        System.out.println("Ditemukan " + hasil.getJumlahHasil() + " dokumen.");
        System.out.println("------------------------------------------------");
        catalogSearcher.tampilkanHasil(hasil.getDaftarHasil());
        System.out.println("================================================");
    }

    // ===================== Getter / Setter =====================

    public CatalogSearcher getCatalogSearcher(){ 
		return catalogSearcher; 
	}
	
    public void setCatalogSearcher(CatalogSearcher searcher){
		this.catalogSearcher = searcher; 
	}
}

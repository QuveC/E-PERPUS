package model;

/** Abstract class - blueprint dokumen perpustakaan. Tabel: dokumen. @author Farel */
public abstract class Dokumen {

    // --- Atribut (tabel: dokumen) ---
    private String idDokumen;   // PK
    private String judul;
    private String pengarang;
    private int    tahunTerbit;
    private String tipeDokumen; // enum: 'BUKU' | 'MAJALAH'

    // --- Constructor ---
    public Dokumen(String idDokumen, String judul, String pengarang,
                   int tahunTerbit, String tipeDokumen) {
        this.idDokumen   = idDokumen;
        this.judul       = judul;
        this.pengarang   = pengarang;
        this.tahunTerbit = tahunTerbit;
        this.tipeDokumen = tipeDokumen;
    }

    public Dokumen() {}

    // --- Abstract method - wajib diimplementasi subclass ---
    public abstract String getDetail();

    // --- Getter & Setter ---
    public String getIdDokumen()                  { return idDokumen; }
    public void   setIdDokumen(String idDokumen)  { this.idDokumen = idDokumen; }

    public String getJudul()                      { return judul; }
    public void   setJudul(String judul)          { this.judul = judul; }

    public String getPengarang()                      { return pengarang; }
    public void   setPengarang(String pengarang)      { this.pengarang = pengarang; }

    public int  getTahunTerbit()                      { return tahunTerbit; }
    public void setTahunTerbit(int tahunTerbit)       { this.tahunTerbit = tahunTerbit; }

    public String getTipeDokumen()                    { return tipeDokumen; }
    public void   setTipeDokumen(String tipeDokumen)  { this.tipeDokumen = tipeDokumen; }

    @Override
    public String toString() {
        return "Dokumen{id=" + idDokumen + ", judul=" + judul
                + ", pengarang=" + pengarang + ", tahun=" + tahunTerbit
                + ", tipe=" + tipeDokumen + "}";
    }
}

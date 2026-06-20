package model;

/** Subclass Dokumen untuk majalah. Tabel: majalah. @author Farel */
public class Majalah extends Dokumen {

    // --- Atribut tambahan (tabel: majalah) ---
    private String edisi;
    private String bulanTerbit;
    private String frekuensiTerbit; // contoh: Mingguan, Bulanan
    private String lokasiRak;

    // --- Constructor ---
    public Majalah(String idDokumen, String judul, String pengarang,
                   int tahunTerbit, String edisi, String bulanTerbit,
                   String frekuensiTerbit, String lokasiRak) {
        super(idDokumen, judul, pengarang, tahunTerbit, "MAJALAH");
        this.edisi           = edisi;
        this.bulanTerbit     = bulanTerbit;
        this.frekuensiTerbit = frekuensiTerbit;
        this.lokasiRak       = lokasiRak;
    }

    public Majalah() { super(); }

    // --- Implementasi abstract method ---
    @Override
    public String getDetail() {
        return "Majalah{judul=" + getJudul() + ", edisi=" + edisi
                + ", bulan=" + bulanTerbit + ", frekuensi=" + frekuensiTerbit
                + ", rak=" + lokasiRak + "}";
    }

    // --- Getter & Setter ---
    public String getEdisi()                          { return edisi; }
    public void   setEdisi(String edisi)              { this.edisi = edisi; }

    public String getBulanTerbit()                        { return bulanTerbit; }
    public void   setBulanTerbit(String bulanTerbit)      { this.bulanTerbit = bulanTerbit; }

    public String getFrekuensiTerbit()                            { return frekuensiTerbit; }
    public void   setFrekuensiTerbit(String frekuensiTerbit)      { this.frekuensiTerbit = frekuensiTerbit; }

    public String getLokasiRak()                      { return lokasiRak; }
    public void   setLokasiRak(String lokasiRak)      { this.lokasiRak = lokasiRak; }

    @Override
    public String toString() {
        return "Majalah{id=" + getIdDokumen() + ", judul=" + getJudul()
                + ", edisi=" + edisi + ", bulan=" + bulanTerbit + "}";
    }
}

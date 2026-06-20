package model;

/** Subclass Dokumen untuk buku. Tabel: buku. @author Farel */
public class Buku extends Dokumen {

    // --- Atribut tambahan (tabel: buku) ---
    private String isbn;
    private String penerbit;
    private int    jumlahHalaman;
    private int    stok;
    private String lokasiRak;

    // --- Constructor ---
    public Buku(String idDokumen, String judul, String pengarang,
                int tahunTerbit, String isbn, String penerbit,
                int jumlahHalaman, int stok, String lokasiRak) {
        super(idDokumen, judul, pengarang, tahunTerbit, "BUKU");
        this.isbn          = isbn;
        this.penerbit      = penerbit;
        this.jumlahHalaman = jumlahHalaman;
        this.stok          = stok;
        this.lokasiRak     = lokasiRak;
    }

    public Buku() { super(); }

    // --- Implementasi abstract method ---
    @Override
    public String getDetail() {
        return "Buku{judul=" + getJudul() + ", isbn=" + isbn
                + ", penerbit=" + penerbit + ", halaman=" + jumlahHalaman
                + ", stok=" + stok + ", rak=" + lokasiRak + "}";
    }

    // --- Getter & Setter ---
    public String getIsbn()                       { return isbn; }
    public void   setIsbn(String isbn)            { this.isbn = isbn; }

    public String getPenerbit()                   { return penerbit; }
    public void   setPenerbit(String penerbit)    { this.penerbit = penerbit; }

    public int  getJumlahHalaman()                        { return jumlahHalaman; }
    public void setJumlahHalaman(int jumlahHalaman)       { this.jumlahHalaman = jumlahHalaman; }

    public int  getStok()                         { return stok; }
    public void setStok(int stok)                 { this.stok = stok; }

    public String getLokasiRak()                  { return lokasiRak; }
    public void   setLokasiRak(String lokasiRak)  { this.lokasiRak = lokasiRak; }

    @Override
    public String toString() {
        return "Buku{id=" + getIdDokumen() + ", judul=" + getJudul()
                + ", isbn=" + isbn + ", stok=" + stok + "}";
    }
}

package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Menyimpan hasil pencarian dokumen beserta metadata pencarian
 * (kata kunci dan jumlah hasil).
 * @author Farabi AM
 */
public class HasilPencarian {

    private List<Dokumen> daftarHasil;
    private String        kataKunci;
    private int           jumlahHasil;

    public HasilPencarian() {
        this.daftarHasil = new ArrayList<>();
        this.kataKunci   = "";
        this.jumlahHasil = 0;
    }

    public HasilPencarian(List<Dokumen> daftarHasil, String kataKunci) {
        this.daftarHasil = (daftarHasil != null) ? daftarHasil : new ArrayList<>();
        this.kataKunci   = (kataKunci != null)   ? kataKunci   : "";
        this.jumlahHasil = this.daftarHasil.size();
    }

    public List<Dokumen> getDaftarHasil() {
        return daftarHasil;
    }

    public int getJumlahHasil() {
        return jumlahHasil;
    }

    public String getKataKunci() {
        return kataKunci;
    }

    public void setDaftarHasil(List<Dokumen> daftarHasil) {
        this.daftarHasil = (daftarHasil != null) ? daftarHasil : new ArrayList<>();
        this.jumlahHasil = this.daftarHasil.size();
    }

    public void setKataKunci(String kataKunci) {
        this.kataKunci = kataKunci;
    }

    public void setJumlahHasil(int jumlahHasil) {
        this.jumlahHasil = jumlahHasil;
    }

    @Override
    public String toString() {
        return "HasilPencarian{kataKunci='" + kataKunci
                + "', jumlahHasil=" + jumlahHasil + "}";
    }
}

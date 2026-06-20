package model; // Sesuai diagram, ini cenderung masuk ke model entitas

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RiwayatAnggota {
    private String idRiwayat;
    private String idAnggota;
    private String aksi;
    private LocalDate tanggalAksi;

    public RiwayatAnggota(String idRiwayat, String idAnggota, String aksi, LocalDate tanggalAksi) {
        this.idRiwayat = idRiwayat;
        this.idAnggota = idAnggota;
        this.aksi = aksi;
        this.tanggalAksi = tanggalAksi;
    }

    public void simpanRiwayat() {
        // Logika persistensi data riwayat ke penyimpanan
        System.out.println("Riwayat [" + idRiwayat + "] berhasil disimpan: " + aksi);
    }

    // Mengatasi kelemahan UML dengan menambahkan parameter pencarian idAnggota
    public List<RiwayatAnggota> getRiwayatByAnggota(String idAnggota) {
        System.out.println("Mengambil list riwayat untuk ID Anggota: " + idAnggota);
        return new ArrayList<>(); 
    }

    // Getter & Setter
    public String getIdRiwayat() { return idRiwayat; }
    public void setIdRiwayat(String idRiwayat) { this.idRiwayat = idRiwayat; }

    public String getIdAnggota() { return idAnggota; }
    public void setIdAnggota(String idAnggota) { this.idAnggota = idAnggota; }

    public String getAksi() { return aksi; }
    public void setAksi(String aksi) { this.aksi = aksi; }

    public LocalDate getTanggalAksi() { return tanggalAksi; }
    public void setTanggalAksi(LocalDate tanggalAksi) { this.tanggalAksi = tanggalAksi; }
}
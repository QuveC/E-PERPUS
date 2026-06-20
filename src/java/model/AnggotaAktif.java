package model;

import java.time.LocalDate;

/**
 * Subclass Anggota untuk anggota berstatus aktif.
 * Tabel: anggota + anggota_aktif (JOIN by id_anggota).
 */
public class AnggotaAktif extends Anggota {

    private LocalDate tanggalBergabung;
    private int       batasMaxPinjam;   // default 3 sesuai DB

    //Constructor lengkap
    public AnggotaAktif(String idAnggota, String nama, String email,
                        String noTelepon, String alamat, String role,
                        LocalDate tanggalBergabung, int batasMaxPinjam) {
        super(idAnggota, nama, email, noTelepon, alamat, role, "AKTIF");
        this.tanggalBergabung = tanggalBergabung;
        this.batasMaxPinjam   = batasMaxPinjam;
    }

    // Constructor dengan batasMaxPinjam default = 3
    public AnggotaAktif(String idAnggota, String nama, String email,
                        String noTelepon, String alamat, String role,
                        LocalDate tanggalBergabung) {
        this(idAnggota, nama, email, noTelepon, alamat, role, tanggalBergabung, 3);
    }

    public AnggotaAktif() { super(); }

    //Implementasi abstract method
    @Override
    public String getDetail() {
        return "AnggotaAktif{nama=" + getNama()
                + ", bergabung=" + tanggalBergabung
                + ", maxPinjam=" + batasMaxPinjam + "}";
    }

    //Getter & Setter
    public LocalDate getTanggalBergabung(){
        return tanggalBergabung;
    }
    public void setTanggalBergabung(LocalDate v){
        this.tanggalBergabung = v; 
    }

    public int getBatasMaxPinjam(){
        return batasMaxPinjam; 
    }
    public void setBatasMaxPinjam(int v){
        this.batasMaxPinjam = v; 
    }

    @Override
    public String toString() {
        return "AnggotaAktif{id=" + getIdAnggota() + ", nama=" + getNama()
                + ", bergabung=" + tanggalBergabung
                + ", maxPinjam=" + batasMaxPinjam + "}";
    }
}

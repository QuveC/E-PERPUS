package model;

import java.time.LocalDate;

/**
 * Subclass Anggota untuk anggota berstatus non-aktif.
 * Tabel: anggota + anggota_nonaktif (JOIN by id_anggota).
 * @author [nama kamu]
 */
public class AnggotaNonAktif extends Anggota {

    // --- Atribut tambahan (tabel: anggota_nonaktif) ---
    private LocalDate tanggalNonaktif;
    private String    alasanNonaktif;

    // --- Constructor lengkap ---
    public AnggotaNonAktif(String idAnggota, String nama, String email,
                           String noTelepon, String alamat, String role,
                           LocalDate tanggalNonaktif, String alasanNonaktif) {
        super(idAnggota, nama, email, noTelepon, alamat, role, "NON_AKTIF");
        this.tanggalNonaktif = tanggalNonaktif;
        this.alasanNonaktif  = alasanNonaktif;
    }

    public AnggotaNonAktif() { super(); }

    // --- Implementasi abstract method ---
    @Override
    public String getDetail() {
        return "AnggotaNonAktif{nama=" + getNama()
                + ", nonaktifSejak=" + tanggalNonaktif
                + ", alasan=" + alasanNonaktif + "}";
    }

    // --- Getter & Setter ---
    public LocalDate getTanggalNonaktif()                 { return tanggalNonaktif; }
    public void      setTanggalNonaktif(LocalDate v)      { this.tanggalNonaktif = v; }

    public String getAlasanNonaktif()                 { return alasanNonaktif; }
    public void   setAlasanNonaktif(String v)         { this.alasanNonaktif = v; }

    @Override
    public String toString() {
        return "AnggotaNonAktif{id=" + getIdAnggota() + ", nama=" + getNama()
                + ", nonaktifSejak=" + tanggalNonaktif
                + ", alasan=" + alasanNonaktif + "}";
    }
}

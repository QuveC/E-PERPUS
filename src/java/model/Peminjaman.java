package model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Class entitas transaksi peminjaman dokumen perpustakaan.
 * Tabel: peminjaman.
 * Relasi: aggregates Anggota, aggregates Dokumen.
 * @author [nama kamu]
 */
public class Peminjaman {

    // ===================== Atribut =====================

    private String     idPeminjaman;
    private Anggota    peminjam;
    private Dokumen    dokumenDipinjam;
    private LocalDate  tanggalPinjam;
    private LocalDate  tanggalJatuhTempo;
    private String     status;            // 'AKTIF' | 'KEMBALI' | 'TERLAMBAT'

    // Durasi pinjam default (hari)
    private static final int DURASI_DEFAULT = 7;

    // ===================== Constructor =====================

    /**
     * Constructor lengkap — dipakai saat membaca dari DB.
     */
    public Peminjaman(String idPeminjaman, Anggota peminjam,
                      Dokumen dokumenDipinjam, LocalDate tanggalPinjam,
                      LocalDate tanggalJatuhTempo, String status) {
        this.idPeminjaman     = idPeminjaman;
        this.peminjam         = peminjam;
        this.dokumenDipinjam  = dokumenDipinjam;
        this.tanggalPinjam    = tanggalPinjam;
        this.tanggalJatuhTempo = tanggalJatuhTempo;
        this.status           = status;
    }

    /**
     * Constructor baru — jatuh tempo otomatis DURASI_DEFAULT hari dari tanggal pinjam.
     */
    public Peminjaman(String idPeminjaman, Anggota peminjam,
                      Dokumen dokumenDipinjam, LocalDate tanggalPinjam) {
        this(idPeminjaman, peminjam, dokumenDipinjam,
             tanggalPinjam,
             tanggalPinjam.plusDays(DURASI_DEFAULT),
             "AKTIF");
    }

    public Peminjaman() {}

    // ===================== Metode =====================

    /** Getter id peminjaman. */
    public String getIdPeminjaman() {
        return idPeminjaman;
    }

    /** Getter tanggal jatuh tempo. */
    public LocalDate getTglJatuhTempo() {
        return tanggalJatuhTempo;
    }

    /** Update status peminjaman ('AKTIF' / 'KEMBALI' / 'TERLAMBAT'). */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Cetak ringkasan detail peminjaman ke konsol.
     */
    public void tampilkanDetail() {
        System.out.println("=== Detail Peminjaman ===");
        System.out.println("ID Peminjaman   : " + idPeminjaman);
        System.out.println("Peminjam        : " + (peminjam != null ? peminjam.getNama() : "-"));
        System.out.println("Dokumen         : " + (dokumenDipinjam != null ? dokumenDipinjam.getJudul() : "-"));
        System.out.println("Tanggal Pinjam  : " + tanggalPinjam);
        System.out.println("Jatuh Tempo     : " + tanggalJatuhTempo);
        System.out.println("Status          : " + status);
    }

    // ===================== Getter & Setter =====================

    public void      setIdPeminjaman(String idPeminjaman)           { this.idPeminjaman = idPeminjaman; }

    public Anggota   getPeminjam()                                   { return peminjam; }
    public void      setPeminjam(Anggota peminjam)                   { this.peminjam = peminjam; }

    public Dokumen   getDokumenDipinjam()                            { return dokumenDipinjam; }
    public void      setDokumenDipinjam(Dokumen dokumenDipinjam)     { this.dokumenDipinjam = dokumenDipinjam; }

    public LocalDate getTanggalPinjam()                              { return tanggalPinjam; }
    public void      setTanggalPinjam(LocalDate tanggalPinjam)       { this.tanggalPinjam = tanggalPinjam; }

    public LocalDate getTanggalJatuhTempo()                          { return tanggalJatuhTempo; }
    public void      setTanggalJatuhTempo(LocalDate tanggalJatuhTempo){ this.tanggalJatuhTempo = tanggalJatuhTempo; }

    public String    getStatus()                                     { return status; }

    @Override
    public String toString() {
        return "Peminjaman{id=" + idPeminjaman
                + ", peminjam=" + (peminjam != null ? peminjam.getNama() : "null")
                + ", dokumen=" + (dokumenDipinjam != null ? dokumenDipinjam.getJudul() : "null")
                + ", jatuhTempo=" + tanggalJatuhTempo
                + ", status=" + status + "}";
    }
}
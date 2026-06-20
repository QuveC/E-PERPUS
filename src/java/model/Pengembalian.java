package model;

import java.time.LocalDate;

/**
 * Class entitas transaksi pengembalian dokumen perpustakaan.
 * Tabel: pengembalian.
 * Relasi: aggregates Peminjaman.
 * @author Ahmad
 */
public class Pengembalian {

    // ===================== Atribut =====================

    private String     idPengembalian;
    private Peminjaman peminjaman;
    private LocalDate  tanggalKembaliAktual;
    private double     jumlahDenda;

    // ===================== Constructor =====================

    /**
     * Constructor lengkap — dipakai saat membaca dari DB.
     */
    public Pengembalian(String idPengembalian, Peminjaman peminjaman,
                        LocalDate tanggalKembaliAktual, double jumlahDenda) {
        this.idPengembalian      = idPengembalian;
        this.peminjaman          = peminjaman;
        this.tanggalKembaliAktual = tanggalKembaliAktual;
        this.jumlahDenda         = jumlahDenda;
    }

    /**
     * Constructor baru — denda dihitung belakangan oleh ManagerPengembalian.
     */
    public Pengembalian(String idPengembalian, Peminjaman peminjaman,
                        LocalDate tanggalKembaliAktual) {
        this(idPengembalian, peminjaman, tanggalKembaliAktual, 0.0);
    }

    public Pengembalian() {}

    // ===================== Metode =====================

    /**
     * Cetak ringkasan detail pengembalian ke konsol.
     */
    public void tampilkanDetail() {
        System.out.println("=== Detail Pengembalian ===");
        System.out.println("ID Pengembalian   : " + idPengembalian);
        System.out.println("ID Peminjaman     : " + (peminjaman != null ? peminjaman.getIdPeminjaman() : "-"));
        System.out.println("Tanggal Kembali   : " + tanggalKembaliAktual);
        System.out.println("Jumlah Denda      : Rp " + String.format("%.0f", jumlahDenda));
    }

    // ===================== Getter & Setter =====================

    public String     getIdPengembalian()                               { return idPengembalian; }
    public void       setIdPengembalian(String idPengembalian)          { this.idPengembalian = idPengembalian; }

    public Peminjaman getPeminjaman()                                    { return peminjaman; }
    public void       setPeminjaman(Peminjaman peminjaman)               { this.peminjaman = peminjaman; }

    public LocalDate  getTanggalKembaliAktual()                         { return tanggalKembaliAktual; }
    public void       setTanggalKembaliAktual(LocalDate tgl)            { this.tanggalKembaliAktual = tgl; }

    public double     getJumlahDenda()                                   { return jumlahDenda; }
    public void       setJumlahDenda(double jumlahDenda)                 { this.jumlahDenda = jumlahDenda; }

    @Override
    public String toString() {
        return "Pengembalian{id=" + idPengembalian
                + ", peminjaman=" + (peminjaman != null ? peminjaman.getIdPeminjaman() : "null")
                + ", kembali=" + tanggalKembaliAktual
                + ", denda=Rp" + String.format("%.0f", jumlahDenda) + "}";
    }
}
package controller;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Utility class untuk menghitung denda keterlambatan pengembalian dokumen.
 * Digunakan oleh ManagerPengembalian.
 * @author Ahmad
 */
public class HitungDenda {

    // ===================== Atribut =====================

    private double tarifDendaPerHari;

    // ===================== Constructor =====================

    public HitungDenda(double tarifDendaPerHari) {
        this.tarifDendaPerHari = tarifDendaPerHari;
    }

    // ===================== Metode =====================

    /**
     * Menghitung total denda berdasarkan tanggal jatuh tempo dan tanggal kembali aktual.
     *
     * @param tanggalJatuhTempo  Tanggal seharusnya dokumen dikembalikan
     * @param tanggalKembali     Tanggal aktual dokumen dikembalikan
     * @return total denda dalam rupiah (0.0 jika tidak terlambat)
     */
    public double hitungDenda(LocalDate tanggalJatuhTempo, LocalDate tanggalKembali) {
        int hariTerlambat = hitungHariTerlambat(tanggalJatuhTempo, tanggalKembali);
        if (hariTerlambat <= 0)
        return 0.0;
        return hariTerlambat * tarifDendaPerHari;
    }

    /**
     * Menghitung jumlah hari keterlambatan.
     *
     * @param tanggalJatuhTempo  Tanggal jatuh tempo peminjaman
     * @param tanggalKembali     Tanggal aktual pengembalian
     * @return jumlah hari terlambat (0 jika tepat waktu atau lebih awal)
     */
    public int hitungHariTerlambat(LocalDate tanggalJatuhTempo, LocalDate tanggalKembali) {
        long selisih = ChronoUnit.DAYS.between(tanggalJatuhTempo, tanggalKembali);
        return (int) Math.max(0, selisih);
    }

    // ===================== Getter & Setter =====================

    public double getTarifDendaPerHari()                        { return tarifDendaPerHari; }
    public void   setTarifDendaPerHari(double tarifDendaPerHari){ this.tarifDendaPerHari = tarifDendaPerHari; }
}
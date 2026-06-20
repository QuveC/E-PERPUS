package controller; // atau package manager;

import model.Anggota;
import model.AnggotaAktif;
import model.AnggotaNonAktif;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ManajemenAnggota {
    private List<Anggota> daftarAnggota;

    public ManajemenAnggota() {
        this.daftarAnggota = new ArrayList<>();
    }

    // Menambahkan objek Anggota (bisa AnggotaAktif atau AnggotaNonAktif)
    public void tambahAnggota(Anggota anggota) {
        if (anggota != null) {
            daftarAnggota.add(anggota);
        }
    }

    public void hapusAnggota(String idAnggota) {
        daftarAnggota.removeIf(a -> a.getIdAnggota().equalsIgnoreCase(idAnggota));
    }

    public Anggota cariById(String id) {
        for (Anggota a : daftarAnggota) {
            if (a.getIdAnggota().equalsIgnoreCase(id)) {
                return a;
            }
        }
        return null;
    }

    public void tampilkanSemua() {
        for (Anggota a : daftarAnggota) {
            System.out.println(a.getDetail());
        }
    }

    public void editAnggota(String idAnggota, Anggota dataBaru) {
        for (int i = 0; i < daftarAnggota.size(); i++) {
            if (daftarAnggota.get(i).getIdAnggota().equalsIgnoreCase(idAnggota)) {
                daftarAnggota.set(i, dataBaru);
                break;
            }
        }
    }

    // Mengubah status AnggotaAktif menjadi AnggotaNonAktif (Mutasi Objek)
    public void nonaktifkanAnggota(String idAnggota, String alasan) {
        Anggota target = cariById(idAnggota);
        if (target instanceof AnggotaAktif) {
            // Cetak blueprint baru berdasarkan data lama
            AnggotaNonAktif nonAktif = new AnggotaNonAktif(
                target.getIdAnggota(),
                target.getNama(),
                target.getEmail(),
                target.getNoTelepon(),
                target.getAlamat(),
                target.getRole(),
                LocalDate.now(), // tanggal nonaktif saat ini
                alasan
            );
            // Ganti objek lama di dalam list
            hapusAnggota(idAnggota);
            tambahAnggota(nonAktif);
        }
    }

    // Mengubah status AnggotaNonAktif kembali menjadi AnggotaAktif
    public void reaktivasiAnggota(String idAnggota) {
        Anggota target = cariById(idAnggota);
        if (target instanceof AnggotaNonAktif) {
            // Kembalikan ke objek AnggotaAktif dengan batas pinjam default (3)
            AnggotaAktif aktif = new AnggotaAktif(
                target.getIdAnggota(),
                target.getNama(),
                target.getEmail(),
                target.getNoTelepon(),
                target.getAlamat(),
                target.getRole(),
                LocalDate.now() // tanggal bergabung diperbarui atau disesuaikan
            );
            hapusAnggota(idAnggota);
            tambahAnggota(aktif);
        }
    }
}
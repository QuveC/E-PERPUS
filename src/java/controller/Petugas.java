package controller;

public class Petugas {
    private String idPetugas;
    private String nama;
    private String email;
    
    // Relasi 'manages' sesuai diagram UML
    private ManajemenAnggota manajemenAnggota;
    private ManagerKoleksi managerKoleksi;
    private ManagerPeminjaman managerPeminjaman;
    private ManagerPengembalian managerPengembalian;

    public Petugas(String idPetugas, String nama, String email, 
                   ManajemenAnggota manajemenAnggota,
                   ManagerKoleksi managerKoleksi,
                   ManagerPeminjaman managerPeminjaman,
                   ManagerPengembalian managerPengembalian) {
        this.idPetugas = idPetugas;
        this.nama = nama;
        this.email = email;
        this.manajemenAnggota = manajemenAnggota;
        this.managerKoleksi = managerKoleksi;
        this.managerPeminjaman = managerPeminjaman;
        this.managerPengembalian = managerPengembalian;
    }

    public void kelolaAnggota() {
        if (manajemenAnggota != null) {
            System.out.println("Petugas " + nama + " mengakses menu manajemen anggota.");
            manajemenAnggota.tampilkanSemua();
        }
    }

    public void kelolaKoleksi() {
        if (managerKoleksi != null) {
            System.out.println("Petugas " + nama + " mengakses menu manajemen koleksi.");
            managerKoleksi.tampilkanSemua();
        }
    }

    public void prosesPeminjaman() {
        if (managerPeminjaman != null) {
            System.out.println("Petugas " + nama + " mengakses menu peminjaman.");
            managerPeminjaman.tampilkanSemua();
            // PERINGATAN: Harus ada cara untuk menyalurkan objek Anggota dan Dokumen ke sini.
        }
    }

    public void prosesKembali() {
        if (managerPengembalian != null) {
            System.out.println("Petugas " + nama + " mengakses menu pengembalian.");
            // Karena di ManagerPengembalian ada tampilkanSemua(), kita panggil sebagai placeholder
            managerPengembalian.tampilkanSemua(); 
        }
    }

    // ===================== Getter & Setter =====================
    public String getIdPetugas() { return idPetugas; }
    public void setIdPetugas(String idPetugas) { this.idPetugas = idPetugas; }

    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public ManajemenAnggota getManajemenAnggota() { return manajemenAnggota; }
    public void setManajemenAnggota(ManajemenAnggota manajemenAnggota) { this.manajemenAnggota = manajemenAnggota; }

    public ManagerKoleksi getManagerKoleksi() { return managerKoleksi; }
    public void setManagerKoleksi(ManagerKoleksi managerKoleksi) { this.managerKoleksi = managerKoleksi; }

    public ManagerPeminjaman getManagerPeminjaman() { return managerPeminjaman; }
    public void setManagerPeminjaman(ManagerPeminjaman managerPeminjaman) { this.managerPeminjaman = managerPeminjaman; }

    public ManagerPengembalian getManagerPengembalian() { return managerPengembalian; }
    public void setManagerPengembalian(ManagerPengembalian managerPengembalian) { this.managerPengembalian = managerPengembalian; }
}
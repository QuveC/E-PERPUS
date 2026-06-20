# 📚 E-Perpus — Sistem Manajemen Perpustakaan

Aplikasi web manajemen perpustakaan berbasis **Java Servlet & JSP** yang dibuat sebagai Tugas Besar Pemrograman Berorientasi Objek (PBO). Aplikasi ini menangani autentikasi, manajemen koleksi (Buku/Majalah/Dokumen), manajemen anggota, hingga sirkulasi peminjaman & pengembalian beserta perhitungan denda otomatis.

## ✨ Fitur Utama

### Publik
- **Autentikasi (Login)** — login terintegrasi untuk Admin dan Member.
- **Registrasi Anggota** — pendaftaran akun baru bagi calon member.

### Admin (Pustakawan)
- **Dashboard Admin** — navigasi ke seluruh modul pengelolaan.
- **Manajemen Koleksi** — CRUD koleksi perpustakaan dengan polimorfisme (Buku, Majalah, Dokumen).
- **Manajemen Anggota** — kelola data anggota, riwayat, dan status Aktif/Nonaktif.
- **Sirkulasi Peminjaman** — pencatatan transaksi peminjaman dengan validasi kelayakan anggota.
- **Sirkulasi Pengembalian & Denda** — pencatatan pengembalian dan perhitungan denda otomatis untuk keterlambatan.
- **Katalog Pencarian (Admin)** — pencarian koleksi secara spesifik.

### Member (Anggota)
- **Dashboard Member** — halaman utama setelah login.
- **Katalog Pencarian (Member)** — cari dan cek ketersediaan koleksi.
- **Riwayat Peminjaman** — lihat koleksi yang sedang dipinjam, jatuh tempo, dan riwayat transaksi.

### Keamanan
- **Session Management** — otorisasi level JSP & Servlet sehingga Member tidak dapat mengakses halaman khusus Admin.

## 🏗️ Arsitektur & Struktur Proyek

Proyek mengikuti pola **MVC** dengan struktur paket sebagai berikut:

```
TUBESPBO/
├── src/java/
│   ├── controller/   # Logika bisnis (ManagerKoleksi, ManagerPeminjaman, ManagerPengembalian, dll.)
│   ├── model/        # Entitas (Anggota, Buku, Majalah, Dokumen, Peminjaman, Pengembalian, dll.)
│   ├── servlet/       # Servlet sebagai penghubung request HTTP ↔ controller
│   └── util/          # Koneksi database (DBConnection)
├── web/                # Halaman JSP, CSS, dan WEB-INF
├── tubesperpustakaan.sql  # Skema & data awal database
└── build.xml           # Ant build script (NetBeans project)
```

### Class Diagram

Diagram kelas lengkap tersedia pada `laporan_akhir.pdf` di repository ini, mencakup relasi antara `Anggota`, `Dokumen` (Buku/Majalah), `Peminjaman`, `Pengembalian`, beserta manager-manager terkait.

## 🛠️ Teknologi yang Digunakan

| Komponen        | Teknologi              |
|------------------|-------------------------|
| Bahasa           | Java (Servlet & JSP)    |
| Build Tool       | Apache Ant (NetBeans)   |
| Database         | MySQL                   |
| Konektor DB      | MySQL Connector/J       |
| Frontend         | JSP, CSS                |

## 🚀 Cara Menjalankan Proyek

### Prasyarat
- JDK terinstal
- Apache Tomcat (atau server Java EE lain) yang kompatibel dengan NetBeans
- MySQL Server
- MySQL Connector/J (`mysql-connector-j.jar`) ditambahkan ke Libraries project

### Langkah-langkah

1. **Clone repository**
   ```bash
   git clone https://github.com/QuveC/TUBESPBO.git
   ```

2. **Import database**

   Buat database dan import skema dari file `tubesperpustakaan.sql` menggunakan phpMyAdmin atau MySQL CLI:
   ```bash
   mysql -u root -p < tubesperpustakaan.sql
   ```

3. **Konfigurasi koneksi database**

   Sesuaikan kredensial pada `src/java/util/DBConnection.java` bila perlu (default: `root` tanpa password, database `tubesperpustakaan` di `localhost:3306`).

4. **Buka & jalankan proyek**

   Buka folder proyek di **Apache NetBeans**, pastikan `mysql-connector-j.jar` sudah ditambahkan ke Libraries, lalu jalankan (Run Project) pada server aplikasi yang sudah dikonfigurasi.

5. **Akses aplikasi**

   Buka browser ke `http://localhost:8080/TUBESPBO/` (sesuaikan port server).

   - Login Admin menggunakan akun pustakawan.
   - Login Member menggunakan NIM/NIP sebagai username dan password.

## 👥 Anggota Kelompok & Kontribusi

| Nama | NIM | Kontribusi |
|------|-----|------------|
| Ahmad Aqiela | 103012400077 | UI/UX (JSP), Session Management, fitur Auth |
| Akmal Luigi Pradana | 103012400155 | Fitur yang berhubungan dengan anggota |
| Farel Gian Pratama | 103012400195 | Peminjaman, Pengembalian, dan Perhitungan Denda |
| Farabi Arafat Muttaqien | 103012430003 | Fitur pencarian |
| Baliana Daniswara | 103012400363 | Manajemen Anggota, Riwayat Anggota, dan Petugas |
| Naufal Yuava Rasyadan | 103012400210 | Peminjaman, ManagerPeminjaman, dan ValidasiPeminjaman |

## 📄 Dokumentasi

Laporan lengkap (kontribusi anggota, daftar fitur, class diagram, dan screenshot antarmuka) tersedia pada file `laporan_akhir.pdf`.

---
Proyek ini dibuat untuk memenuhi Tugas Besar mata kuliah Pemrograman Berorientasi Objek — Program Studi Informatika, Fakultas Informatika, Universitas Telkom.

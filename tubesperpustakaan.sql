-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Generation Time: Jun 22, 2026 at 06:38 AM
-- Server version: 8.4.3
-- PHP Version: 8.3.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `tubesperpustakaan`
--

-- --------------------------------------------------------

--
-- Table structure for table `anggota`
--

CREATE TABLE `anggota` (
  `id_anggota` varchar(50) COLLATE utf8mb4_general_ci NOT NULL,
  `nama` varchar(150) COLLATE utf8mb4_general_ci NOT NULL,
  `email` varchar(100) COLLATE utf8mb4_general_ci NOT NULL,
  `no_telepon` varchar(20) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `alamat` text COLLATE utf8mb4_general_ci,
  `role` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `tipe_anggota` enum('AKTIF','NON_AKTIF') COLLATE utf8mb4_general_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `anggota`
--

INSERT INTO `anggota` (`id_anggota`, `nama`, `email`, `no_telepon`, `alamat`, `role`, `tipe_anggota`) VALUES
('103012400077', 'Ahmad Aqiela', 'ahmadaqiela123@gmail.com', '081231231', 'asda', 'Mahasiswa', 'AKTIF'),
('AG001', 'Budi Santoso', 'budi@email.com', '081234567890', 'Bandung', 'Mahasiswa', 'AKTIF'),
('AG002', 'Siti Aisyah', 'siti@email.com', '081234567891', 'Jakarta', 'Mahasiswa', 'AKTIF'),
('AG003', 'Andi Wijaya', 'andi@email.com', '081234567892', 'Surabaya', 'Dosen', 'AKTIF');

-- --------------------------------------------------------

--
-- Table structure for table `anggota_aktif`
--

CREATE TABLE `anggota_aktif` (
  `id_anggota` varchar(50) COLLATE utf8mb4_general_ci NOT NULL,
  `tanggal_bergabung` date NOT NULL,
  `batas_max_pinjam` int NOT NULL DEFAULT '3'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `anggota_aktif`
--

INSERT INTO `anggota_aktif` (`id_anggota`, `tanggal_bergabung`, `batas_max_pinjam`) VALUES
('103012400077', '2026-06-20', 3),
('AG001', '2025-01-15', 3),
('AG002', '2025-02-10', 3),
('AG003', '2026-06-20', 3);

-- --------------------------------------------------------

--
-- Table structure for table `anggota_nonaktif`
--

CREATE TABLE `anggota_nonaktif` (
  `id_anggota` varchar(50) COLLATE utf8mb4_general_ci NOT NULL,
  `tanggal_nonaktif` date NOT NULL,
  `alasan_nonaktif` text COLLATE utf8mb4_general_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `buku`
--

CREATE TABLE `buku` (
  `id_dokumen` varchar(50) COLLATE utf8mb4_general_ci NOT NULL,
  `isbn` varchar(50) COLLATE utf8mb4_general_ci NOT NULL,
  `penerbit` varchar(150) COLLATE utf8mb4_general_ci NOT NULL,
  `jumlah_halaman` int NOT NULL,
  `stok` int NOT NULL DEFAULT '0',
  `lokasi_rak` varchar(50) COLLATE utf8mb4_general_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `buku`
--

INSERT INTO `buku` (`id_dokumen`, `isbn`, `penerbit`, `jumlah_halaman`, `stok`, `lokasi_rak`) VALUES
('BK001', '9786231234567', 'Informatika Press', 350, 5, 'R-A1'),
('BK002', '9786239876543', 'Teknologi Media', 420, 3, 'R-A2');

-- --------------------------------------------------------

--
-- Table structure for table `dokumen`
--

CREATE TABLE `dokumen` (
  `id_dokumen` varchar(50) COLLATE utf8mb4_general_ci NOT NULL,
  `judul` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `pengarang` varchar(150) COLLATE utf8mb4_general_ci NOT NULL,
  `tahun_terbit` int NOT NULL,
  `tipe_dokumen` enum('BUKU','MAJALAH') COLLATE utf8mb4_general_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `dokumen`
--

INSERT INTO `dokumen` (`id_dokumen`, `judul`, `pengarang`, `tahun_terbit`, `tipe_dokumen`) VALUES
('BK001', 'Pemrograman Python', 'Ahmad Fauzi', 2022, 'BUKU'),
('BK002', 'Basis Data Modern', 'Bambang Setiawan', 2021, 'BUKU'),
('MJ001', 'National Geographic Juni 2026', 'National Geographic', 2026, 'MAJALAH'),
('MJ002', 'Tempo Edisi Teknologi', 'Tempo Media', 2026, 'MAJALAH');

-- --------------------------------------------------------

--
-- Table structure for table `majalah`
--

CREATE TABLE `majalah` (
  `id_dokumen` varchar(50) COLLATE utf8mb4_general_ci NOT NULL,
  `edisi` varchar(50) COLLATE utf8mb4_general_ci NOT NULL,
  `bulan_terbit` varchar(20) COLLATE utf8mb4_general_ci NOT NULL,
  `frekuensi_terbit` varchar(50) COLLATE utf8mb4_general_ci NOT NULL,
  `lokasi_rak` varchar(50) COLLATE utf8mb4_general_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `majalah`
--

INSERT INTO `majalah` (`id_dokumen`, `edisi`, `bulan_terbit`, `frekuensi_terbit`, `lokasi_rak`) VALUES
('MJ001', 'Vol 15 No 6', 'Juni', 'Bulanan', 'R-M1'),
('MJ002', 'Vol 20 No 3', 'Maret', 'Bulanan', 'R-M2');

-- --------------------------------------------------------

--
-- Table structure for table `peminjaman`
--

CREATE TABLE `peminjaman` (
  `id_peminjaman` varchar(50) COLLATE utf8mb4_general_ci NOT NULL,
  `id_anggota` varchar(50) COLLATE utf8mb4_general_ci NOT NULL,
  `id_dokumen` varchar(50) COLLATE utf8mb4_general_ci NOT NULL,
  `tanggal_pinjam` date NOT NULL,
  `tanggal_jatuh_tempo` date NOT NULL,
  `status` varchar(50) COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'DIPINJAM'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `peminjaman`
--

INSERT INTO `peminjaman` (`id_peminjaman`, `id_anggota`, `id_dokumen`, `tanggal_pinjam`, `tanggal_jatuh_tempo`, `status`) VALUES
('PJM-20260620-001', '103012400077', 'BK001', '2026-06-20', '2026-06-27', 'DIKEMBALIKAN'),
('PJM-20260620-002', '103012400077', 'BK001', '2026-06-20', '2026-06-27', 'DIKEMBALIKAN'),
('PJM-20260620-003', '103012400077', 'BK002', '2026-06-20', '2026-06-27', 'DIKEMBALIKAN'),
('PJM-20260620-004', '103012400077', 'BK002', '2026-06-20', '2026-06-27', 'DIKEMBALIKAN'),
('PM001', 'AG001', 'BK001', '2026-05-20', '2026-06-03', 'DIKEMBALIKAN'),
('PM002', 'AG002', 'BK002', '2026-06-10', '2026-06-24', 'DIPINJAM'),
('PM003', 'AG001', 'MJ001', '2026-06-01', '2026-06-15', 'DIKEMBALIKAN');

-- --------------------------------------------------------

--
-- Table structure for table `pengembalian`
--

CREATE TABLE `pengembalian` (
  `id_pengembalian` varchar(50) COLLATE utf8mb4_general_ci NOT NULL,
  `id_peminjaman` varchar(50) COLLATE utf8mb4_general_ci NOT NULL,
  `tanggal_kembali_aktual` date NOT NULL,
  `jumlah_denda` decimal(10,2) NOT NULL DEFAULT '0.00'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `pengembalian`
--

INSERT INTO `pengembalian` (`id_pengembalian`, `id_peminjaman`, `tanggal_kembali_aktual`, `jumlah_denda`) VALUES
('PG001', 'PM001', '2026-06-01', '0.00'),
('PG002', 'PM003', '2026-06-17', '10000.00'),
('PGB-20260620-001', 'PJM-20260620-001', '2026-06-20', '0.00'),
('PGB-20260620-002', 'PJM-20260620-002', '2026-06-20', '0.00'),
('PGB-20260620-003', 'PJM-20260620-003', '2026-06-20', '0.00'),
('PGB-20260620-004', 'PJM-20260620-004', '2026-06-20', '0.00');

-- --------------------------------------------------------

--
-- Table structure for table `petugas`
--

CREATE TABLE `petugas` (
  `id_petugas` varchar(50) COLLATE utf8mb4_general_ci NOT NULL,
  `nama` varchar(150) COLLATE utf8mb4_general_ci NOT NULL,
  `email` varchar(100) COLLATE utf8mb4_general_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `petugas`
--

INSERT INTO `petugas` (`id_petugas`, `nama`, `email`) VALUES
('PT001', 'Rina Permata', 'rina@perpus.com'),
('PT002', 'Dedi Kurniawan', 'dedi@perpus.com');

-- --------------------------------------------------------

--
-- Table structure for table `riwayat_anggota`
--

CREATE TABLE `riwayat_anggota` (
  `id_riwayat` varchar(50) COLLATE utf8mb4_general_ci NOT NULL,
  `id_anggota` varchar(50) COLLATE utf8mb4_general_ci NOT NULL,
  `aksi` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `tanggal_aksi` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `riwayat_anggota`
--

INSERT INTO `riwayat_anggota` (`id_riwayat`, `id_anggota`, `aksi`, `tanggal_aksi`) VALUES
('RW001', 'AG001', 'Registrasi anggota', '2025-01-15 08:00:00'),
('RW002', 'AG002', 'Registrasi anggota', '2025-02-10 09:15:00'),
('RW003', 'AG003', 'Status anggota dinonaktifkan', '2026-01-01 10:30:00');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `anggota`
--
ALTER TABLE `anggota`
  ADD PRIMARY KEY (`id_anggota`),
  ADD UNIQUE KEY `uq_anggota_email` (`email`);

--
-- Indexes for table `anggota_aktif`
--
ALTER TABLE `anggota_aktif`
  ADD PRIMARY KEY (`id_anggota`);

--
-- Indexes for table `anggota_nonaktif`
--
ALTER TABLE `anggota_nonaktif`
  ADD PRIMARY KEY (`id_anggota`);

--
-- Indexes for table `buku`
--
ALTER TABLE `buku`
  ADD PRIMARY KEY (`id_dokumen`),
  ADD UNIQUE KEY `uq_buku_isbn` (`isbn`);

--
-- Indexes for table `dokumen`
--
ALTER TABLE `dokumen`
  ADD PRIMARY KEY (`id_dokumen`);

--
-- Indexes for table `majalah`
--
ALTER TABLE `majalah`
  ADD PRIMARY KEY (`id_dokumen`);

--
-- Indexes for table `peminjaman`
--
ALTER TABLE `peminjaman`
  ADD PRIMARY KEY (`id_peminjaman`),
  ADD KEY `fk_peminjaman_anggota` (`id_anggota`),
  ADD KEY `fk_peminjaman_dokumen` (`id_dokumen`);

--
-- Indexes for table `pengembalian`
--
ALTER TABLE `pengembalian`
  ADD PRIMARY KEY (`id_pengembalian`),
  ADD UNIQUE KEY `uq_pengembalian_peminjaman` (`id_peminjaman`);

--
-- Indexes for table `petugas`
--
ALTER TABLE `petugas`
  ADD PRIMARY KEY (`id_petugas`),
  ADD UNIQUE KEY `uq_petugas_email` (`email`);

--
-- Indexes for table `riwayat_anggota`
--
ALTER TABLE `riwayat_anggota`
  ADD PRIMARY KEY (`id_riwayat`),
  ADD KEY `fk_riwayat_anggota` (`id_anggota`);

--
-- Constraints for dumped tables
--

--
-- Constraints for table `anggota_aktif`
--
ALTER TABLE `anggota_aktif`
  ADD CONSTRAINT `fk_aktif_anggota` FOREIGN KEY (`id_anggota`) REFERENCES `anggota` (`id_anggota`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `anggota_nonaktif`
--
ALTER TABLE `anggota_nonaktif`
  ADD CONSTRAINT `fk_nonaktif_anggota` FOREIGN KEY (`id_anggota`) REFERENCES `anggota` (`id_anggota`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `buku`
--
ALTER TABLE `buku`
  ADD CONSTRAINT `fk_buku_dokumen` FOREIGN KEY (`id_dokumen`) REFERENCES `dokumen` (`id_dokumen`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `majalah`
--
ALTER TABLE `majalah`
  ADD CONSTRAINT `fk_majalah_dokumen` FOREIGN KEY (`id_dokumen`) REFERENCES `dokumen` (`id_dokumen`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `peminjaman`
--
ALTER TABLE `peminjaman`
  ADD CONSTRAINT `fk_peminjaman_anggota` FOREIGN KEY (`id_anggota`) REFERENCES `anggota` (`id_anggota`) ON DELETE RESTRICT ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_peminjaman_dokumen` FOREIGN KEY (`id_dokumen`) REFERENCES `dokumen` (`id_dokumen`) ON DELETE RESTRICT ON UPDATE CASCADE;

--
-- Constraints for table `pengembalian`
--
ALTER TABLE `pengembalian`
  ADD CONSTRAINT `fk_pengembalian_peminjaman` FOREIGN KEY (`id_peminjaman`) REFERENCES `peminjaman` (`id_peminjaman`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `riwayat_anggota`
--
ALTER TABLE `riwayat_anggota`
  ADD CONSTRAINT `fk_riwayat_anggota` FOREIGN KEY (`id_anggota`) REFERENCES `anggota` (`id_anggota`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

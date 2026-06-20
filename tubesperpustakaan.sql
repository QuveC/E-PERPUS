-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Generation Time: Jun 06, 2026 at 11:00 PM
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
CREATE DATABASE IF NOT EXISTS `tubesperpustakaan` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE `tubesperpustakaan`;

-- --------------------------------------------------------

--
-- Table structure for table `anggota`
--

CREATE TABLE `anggota` (
  `id_anggota` varchar(50) NOT NULL,
  `nama` varchar(150) NOT NULL,
  `email` varchar(100) NOT NULL,
  `no_telepon` varchar(20) DEFAULT NULL,
  `alamat` text,
  `role` varchar(50) DEFAULT NULL,
  `tipe_anggota` enum('AKTIF','NON_AKTIF') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `anggota_aktif`
--

CREATE TABLE `anggota_aktif` (
  `id_anggota` varchar(50) NOT NULL,
  `tanggal_bergabung` date NOT NULL,
  `batas_max_pinjam` int NOT NULL DEFAULT '3'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `anggota_nonaktif`
--

CREATE TABLE `anggota_nonaktif` (
  `id_anggota` varchar(50) NOT NULL,
  `tanggal_nonaktif` date NOT NULL,
  `alasan_nonaktif` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `buku`
--

CREATE TABLE `buku` (
  `id_dokumen` varchar(50) NOT NULL,
  `isbn` varchar(50) NOT NULL,
  `penerbit` varchar(150) NOT NULL,
  `jumlah_halaman` int NOT NULL,
  `stok` int NOT NULL DEFAULT '0',
  `lokasi_rak` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `dokumen`
--

CREATE TABLE `dokumen` (
  `id_dokumen` varchar(50) NOT NULL,
  `judul` varchar(255) NOT NULL,
  `pengarang` varchar(150) NOT NULL,
  `tahun_terbit` int NOT NULL,
  `tipe_dokumen` enum('BUKU','MAJALAH') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `majalah`
--

CREATE TABLE `majalah` (
  `id_dokumen` varchar(50) NOT NULL,
  `edisi` varchar(50) NOT NULL,
  `bulan_terbit` varchar(20) NOT NULL,
  `frekuensi_terbit` varchar(50) NOT NULL,
  `lokasi_rak` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `peminjaman`
--

CREATE TABLE `peminjaman` (
  `id_peminjaman` varchar(50) NOT NULL,
  `id_anggota` varchar(50) NOT NULL,
  `id_dokumen` varchar(50) NOT NULL,
  `tanggal_pinjam` date NOT NULL,
  `tanggal_jatuh_tempo` date NOT NULL,
  `status` varchar(50) NOT NULL DEFAULT 'DIPINJAM'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `pengembalian`
--

CREATE TABLE `pengembalian` (
  `id_pengembalian` varchar(50) NOT NULL,
  `id_peminjaman` varchar(50) NOT NULL,
  `tanggal_kembali_aktual` date NOT NULL,
  `jumlah_denda` decimal(10,2) NOT NULL DEFAULT '0.00'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `petugas`
--

CREATE TABLE `petugas` (
  `id_petugas` varchar(50) NOT NULL,
  `nama` varchar(150) NOT NULL,
  `email` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `riwayat_anggota`
--

CREATE TABLE `riwayat_anggota` (
  `id_riwayat` varchar(50) NOT NULL,
  `id_anggota` varchar(50) NOT NULL,
  `aksi` varchar(255) NOT NULL,
  `tanggal_aksi` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

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

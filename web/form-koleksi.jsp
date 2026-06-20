<%@page import="model.Buku"%>
<%@page import="model.Majalah"%>
<%@page import="model.Dokumen"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    String userRole = (String) session.getAttribute("userRole");
    if (userRole == null || !"ADMIN".equals(userRole)) {
        response.sendRedirect("index.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html lang="id">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Formulir Koleksi - E-Perpus</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Bootstrap Icons -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css" rel="stylesheet">
</head>
<body class="bg-light">
    <!-- Navbar -->
    <nav class="navbar navbar-expand-lg navbar-dark bg-primary shadow-sm mb-4">
        <div class="container">
            <a class="navbar-brand fw-bold" href="dashboard_admin.jsp"><i class="bi bi-book"></i> E-Perpus Admin</a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarNav">
                <ul class="navbar-nav ms-auto">
                    <li class="nav-item"><a class="nav-link" href="dashboard_admin.jsp">Beranda</a></li>
                    <li class="nav-item"><a class="nav-link active" href="KoleksiServlet">Koleksi</a></li>
                    <li class="nav-item"><a class="nav-link" href="AnggotaServlet">Anggota</a></li>
                    <li class="nav-item"><a class="nav-link" href="PencarianServlet">Pencarian</a></li>
                    <li class="nav-item"><a class="nav-link" href="PeminjamanServlet">Peminjaman</a></li>
                    <li class="nav-item"><a class="nav-link" href="PengembalianServlet">Pengembalian</a></li>
                    <li class="nav-item ms-lg-3"><a class="btn btn-danger btn-sm mt-1" href="LoginServlet?action=logout"><i class="bi bi-box-arrow-right"></i> Logout</a></li>
                </ul>
            </div>
        </div>
    </nav>

    <% 
        Dokumen dokumen = (Dokumen) request.getAttribute("dokumen");
        boolean isEdit = dokumen != null;
        
        // Atribut Buku
        String isbn = "", penerbit = "", lokasiRak = "";
        int halaman = 0, stok = 0;
        
        // Atribut Majalah
        String edisi = "", bulanTerbit = "", frekuensi = "", lokasiRakMajalah = "";
        
        if (isEdit) {
            if (dokumen instanceof Buku) {
                Buku b = (Buku) dokumen;
                isbn = b.getIsbn();
                penerbit = b.getPenerbit();
                lokasiRak = b.getLokasiRak();
                halaman = b.getJumlahHalaman();
                stok = b.getStok();
            } else if (dokumen instanceof Majalah) {
                Majalah m = (Majalah) dokumen;
                edisi = m.getEdisi();
                bulanTerbit = m.getBulanTerbit();
                frekuensi = m.getFrekuensiTerbit();
                lokasiRakMajalah = m.getLokasiRak();
            }
        }
    %>

    <div class="container mb-5">
        <div class="row justify-content-center">
            <div class="col-md-8">
                <div class="mb-3">
                    <a href="KoleksiServlet" class="btn btn-outline-secondary btn-sm"><i class="bi bi-arrow-left"></i> Kembali ke Daftar Koleksi</a>
                </div>
                <div class="card shadow-sm border-0">
                    <div class="card-header bg-white py-3">
                        <h4 class="mb-0 text-primary">
                            <% if (isEdit) { %> <i class="bi bi-pencil-square"></i> Edit Dokumen <% } else { %> <i class="bi bi-plus-square"></i> Tambah Dokumen Baru <% } %>
                        </h4>
                    </div>
                    <div class="card-body p-4">
                        <form action="KoleksiServlet" method="POST">
                            <input type="hidden" name="action" value="<%= isEdit ? "update" : "insert" %>">
                            
                            <!-- Umum -->
                            <h5 class="border-bottom pb-2 mb-3">Informasi Umum</h5>
                            <div class="row g-3 mb-3">
                                <div class="col-md-4">
                                    <label class="form-label">Tipe Dokumen</label>
                                    <select name="tipe" id="tipeSelect" class="form-select" onchange="toggleFields()" <%= isEdit ? "disabled" : "" %> required>
                                        <option value="BUKU" <%= isEdit && dokumen instanceof Buku ? "selected" : "" %>>Buku</option>
                                        <option value="MAJALAH" <%= isEdit && dokumen instanceof Majalah ? "selected" : "" %>>Majalah</option>
                                    </select>
                                    <% if (isEdit) { %>
                                        <input type="hidden" name="tipe" value="<%= dokumen instanceof Buku ? "BUKU" : "MAJALAH" %>">
                                    <% } %>
                                </div>
                                <div class="col-md-8">
                                    <label class="form-label">ID Dokumen</label>
                                    <input type="text" name="idDokumen" class="form-control" value="<%= isEdit ? dokumen.getIdDokumen() : "" %>" <%= isEdit ? "readonly" : "" %> required>
                                </div>
                            </div>

                            <div class="mb-3">
                                <label class="form-label">Judul Dokumen</label>
                                <input type="text" name="judul" class="form-control" value="<%= isEdit ? dokumen.getJudul() : "" %>" required>
                            </div>
                            
                            <div class="row g-3 mb-4">
                                <div class="col-md-8">
                                    <label class="form-label">Pengarang</label>
                                    <input type="text" name="pengarang" class="form-control" value="<%= isEdit ? dokumen.getPengarang() : "" %>" required>
                                </div>
                                <div class="col-md-4">
                                    <label class="form-label">Tahun Terbit</label>
                                    <input type="number" name="tahun" class="form-control" value="<%= isEdit ? dokumen.getTahunTerbit() : "" %>" required>
                                </div>
                            </div>

                            <!-- Bidang Buku -->
                            <div id="bukuFields" style="<%= isEdit && dokumen instanceof Majalah ? "display: none;" : "" %>">
                                <h5 class="border-bottom pb-2 mb-3">Informasi Buku</h5>
                                <div class="row g-3 mb-3">
                                    <div class="col-md-6">
                                        <label class="form-label">ISBN</label>
                                        <input type="text" name="isbn" class="form-control" value="<%= isbn %>">
                                    </div>
                                    <div class="col-md-6">
                                        <label class="form-label">Penerbit</label>
                                        <input type="text" name="penerbit" class="form-control" value="<%= penerbit %>">
                                    </div>
                                    <div class="col-md-4">
                                        <label class="form-label">Jumlah Halaman</label>
                                        <input type="number" name="halaman" class="form-control" value="<%= isEdit ? halaman : "" %>">
                                    </div>
                                    <div class="col-md-4">
                                        <label class="form-label">Stok Tersedia</label>
                                        <input type="number" name="stok" class="form-control" value="<%= isEdit ? stok : "" %>">
                                    </div>
                                    <div class="col-md-4">
                                        <label class="form-label">Lokasi Rak</label>
                                        <input type="text" name="lokasiRak" class="form-control" value="<%= lokasiRak %>">
                                    </div>
                                </div>
                            </div>

                            <!-- Bidang Majalah -->
                            <div id="majalahFields" style="<%= (!isEdit || dokumen instanceof Buku) ? "display: none;" : "" %>">
                                <h5 class="border-bottom pb-2 mb-3">Informasi Majalah</h5>
                                <div class="row g-3 mb-3">
                                    <div class="col-md-6">
                                        <label class="form-label">Edisi</label>
                                        <input type="text" name="edisi" class="form-control" value="<%= edisi %>">
                                    </div>
                                    <div class="col-md-6">
                                        <label class="form-label">Bulan Terbit</label>
                                        <input type="text" name="bulanTerbit" class="form-control" value="<%= bulanTerbit %>">
                                    </div>
                                    <div class="col-md-6">
                                        <label class="form-label">Frekuensi Terbit</label>
                                        <input type="text" name="frekuensi" class="form-control" value="<%= frekuensi %>">
                                    </div>
                                    <div class="col-md-6">
                                        <label class="form-label">Lokasi Rak</label>
                                        <input type="text" name="lokasiRakMajalah" class="form-control" value="<%= lokasiRakMajalah %>">
                                    </div>
                                </div>
                            </div>

                            <div class="d-flex justify-content-end gap-2 mt-4 pt-3 border-top">
                                <a href="KoleksiServlet" class="btn btn-secondary">Batal</a>
                                <button type="submit" class="btn btn-primary"><i class="bi bi-save"></i> Simpan Dokumen</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        function toggleFields() {
            var tipe = document.getElementById("tipeSelect").value;
            var bukuFields = document.getElementById("bukuFields");
            var majalahFields = document.getElementById("majalahFields");
            
            if (tipe === "BUKU") {
                bukuFields.style.display = "block";
                majalahFields.style.display = "none";
            } else {
                bukuFields.style.display = "none";
                majalahFields.style.display = "block";
            }
        }
    </script>
</body>
</html>

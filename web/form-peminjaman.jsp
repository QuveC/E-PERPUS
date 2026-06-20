<%@page import="model.AnggotaAktif"%>
<%@page import="model.Dokumen"%>
<%@page import="model.Buku"%>
<%@page import="model.Majalah"%>
<%@page import="java.util.List"%>
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
    <title>Transaksi Peminjaman Baru - E-Perpus</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
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
                    <li class="nav-item"><a class="nav-link" href="KoleksiServlet">Koleksi</a></li>
                    <li class="nav-item"><a class="nav-link" href="AnggotaServlet">Anggota</a></li>
                    <li class="nav-item"><a class="nav-link" href="PencarianServlet">Pencarian</a></li>
                    <li class="nav-item"><a class="nav-link active" href="PeminjamanServlet">Peminjaman</a></li>
                    <li class="nav-item"><a class="nav-link" href="PengembalianServlet">Pengembalian</a></li>
                    <li class="nav-item ms-lg-3"><a class="btn btn-danger btn-sm mt-1" href="LoginServlet?action=logout"><i class="bi bi-box-arrow-right"></i> Logout</a></li>
                </ul>
            </div>
        </div>
    </nav>

    <div class="container mb-5">
        <div class="row justify-content-center">
            <div class="col-md-7">
                <div class="mb-3">
                    <a href="PeminjamanServlet" class="btn btn-outline-secondary btn-sm"><i class="bi bi-arrow-left"></i> Kembali ke Daftar Peminjaman</a>
                </div>
                <div class="card shadow-sm border-0">
                    <div class="card-header bg-white py-3">
                        <h4 class="mb-0 text-warning text-dark"><i class="bi bi-arrow-repeat"></i> Formulir Peminjaman Dokumen</h4>
                    </div>
                    <div class="card-body p-4">
                        <form action="PeminjamanServlet" method="POST">
                            <input type="hidden" name="action" value="insert">
                            
                            <div class="mb-4">
                                <label class="form-label fw-bold">Pilih Peminjam (Anggota Aktif)</label>
                                <select name="idAnggota" class="form-select" required>
                                    <option value="">-- Pilih Anggota --</option>
                                    <% 
                                        List<AnggotaAktif> listAnggota = (List<AnggotaAktif>) request.getAttribute("listAnggota");
                                        if (listAnggota != null) {
                                            for (AnggotaAktif a : listAnggota) {
                                    %>
                                    <option value="<%= a.getIdAnggota() %>"><%= a.getIdAnggota() %> - <%= a.getNama() %></option>
                                    <% 
                                            }
                                        } 
                                    %>
                                </select>
                            </div>

                            <div class="mb-4">
                                <label class="form-label fw-bold">Pilih Dokumen (Buku / Majalah)</label>
                                <select name="idDokumen" class="form-select" required>
                                    <option value="">-- Pilih Dokumen --</option>
                                    <% 
                                        List<Dokumen> listDokumen = (List<Dokumen>) request.getAttribute("listDokumen");
                                        if (listDokumen != null) {
                                            for (Dokumen dok : listDokumen) {
                                                String stokInfo = "";
                                                if (dok instanceof Buku) {
                                                    Buku b = (Buku) dok;
                                                    stokInfo = " (Stok: " + b.getStok() + ")";
                                                    if (b.getStok() <= 0) continue; // Jangan tampilkan buku yg habis
                                                }
                                    %>
                                    <option value="<%= dok.getIdDokumen() %>"><%= dok.getIdDokumen() %> - <%= dok.getJudul() %><%= stokInfo %></option>
                                    <% 
                                            }
                                        } 
                                    %>
                                </select>
                            </div>

                            <div class="d-flex justify-content-end gap-2 mt-4 pt-3 border-top">
                                <a href="PeminjamanServlet" class="btn btn-secondary">Batal</a>
                                <button type="submit" class="btn btn-warning text-dark fw-bold"><i class="bi bi-send-check"></i> Proses Peminjaman</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>

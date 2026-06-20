<%@page import="model.Buku"%>
<%@page import="model.Majalah"%>
<%@page import="model.Dokumen"%>
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
    <title>Manajemen Koleksi - E-Perpus</title>
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

    <!-- Main Content -->
    <div class="container mb-5">
        <div class="mb-3">
            <a href="dashboard_admin.jsp" class="btn btn-outline-secondary btn-sm"><i class="bi bi-arrow-left"></i> Kembali ke Dashboard</a>
        </div>
        <div class="card shadow-sm border-0">
            <div class="card-header bg-white py-3 d-flex justify-content-between align-items-center">
                <h4 class="mb-0 text-primary"><i class="bi bi-journal-text"></i> Manajemen Koleksi</h4>
                <a href="KoleksiServlet?action=new" class="btn btn-primary"><i class="bi bi-plus-lg"></i> Tambah Dokumen</a>
            </div>
            <div class="card-body">
                <div class="table-responsive">
                    <table class="table table-hover align-middle">
                        <thead class="table-light">
                            <tr>
                                <th>ID Dokumen</th>
                                <th>Judul</th>
                                <th>Pengarang</th>
                                <th>Tipe</th>
                                <th>Stok / Edisi</th>
                                <th>Aksi</th>
                            </tr>
                        </thead>
                        <tbody>
                            <% 
                                List<Dokumen> listDokumen = (List<Dokumen>) request.getAttribute("listKoleksi");
                                if (listDokumen != null && !listDokumen.isEmpty()) {
                                    for (Dokumen dok : listDokumen) {
                                        String tipe = "";
                                        String badgeColor = "";
                                        String stokInfo = "";
                                        if (dok instanceof Buku) {
                                            tipe = "Buku";
                                            badgeColor = "bg-info text-dark";
                                            stokInfo = "Stok: " + ((Buku) dok).getStok();
                                        } else if (dok instanceof Majalah) {
                                            tipe = "Majalah";
                                            badgeColor = "bg-secondary";
                                            stokInfo = "Edisi: " + ((Majalah) dok).getEdisi();
                                        }
                            %>
                            <tr>
                                <td><strong><%= dok.getIdDokumen() %></strong></td>
                                <td><%= dok.getJudul() %></td>
                                <td><%= dok.getPengarang() %></td>
                                <td><span class="badge <%= badgeColor %>"><%= tipe %></span></td>
                                <td><%= stokInfo %></td>
                                <td>
                                    <a href="KoleksiServlet?action=edit&id=<%= dok.getIdDokumen() %>&tipe=<%= tipe %>" class="btn btn-sm btn-outline-primary"><i class="bi bi-pencil"></i> Edit</a>
                                    <a href="KoleksiServlet?action=delete&id=<%= dok.getIdDokumen() %>&tipe=<%= tipe %>" class="btn btn-sm btn-outline-danger" onclick="return confirm('Yakin ingin menghapus dokumen ini?');"><i class="bi bi-trash"></i> Hapus</a>
                                </td>
                            </tr>
                            <% 
                                    }
                                } else {
                            %>
                            <tr>
                                <td colspan="6" class="text-center text-muted py-4">Belum ada dokumen di perpustakaan.</td>
                            </tr>
                            <% } %>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>

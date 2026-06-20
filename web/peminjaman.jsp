<%@page import="model.Peminjaman"%>
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
    <title>Sirkulasi Peminjaman - E-Perpus</title>
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
        <div class="mb-3">
            <a href="dashboard_admin.jsp" class="btn btn-outline-secondary btn-sm"><i class="bi bi-arrow-left"></i> Kembali ke Dashboard</a>
        </div>
        <div class="card shadow-sm border-0">
            <div class="card-header bg-white py-3 d-flex justify-content-between align-items-center">
                <h4 class="mb-0 text-warning text-dark"><i class="bi bi-arrow-left-right"></i> Sirkulasi Peminjaman</h4>
                <a href="PeminjamanServlet?action=new" class="btn btn-warning text-dark fw-bold"><i class="bi bi-cart-plus"></i> Transaksi Baru</a>
            </div>
            <div class="card-body">
                <div class="table-responsive">
                    <table class="table table-hover align-middle">
                        <thead class="table-light">
                            <tr>
                                <th>ID Transaksi</th>
                                <th>Nama Peminjam</th>
                                <th>Judul Dokumen</th>
                                <th>Tgl Pinjam</th>
                                <th>Jatuh Tempo</th>
                                <th>Aksi</th>
                            </tr>
                        </thead>
                        <tbody>
                            <% 
                                List<Peminjaman> listPeminjaman = (List<Peminjaman>) request.getAttribute("listPeminjaman");
                                if (listPeminjaman != null && !listPeminjaman.isEmpty()) {
                                    for (Peminjaman p : listPeminjaman) {
                            %>
                            <tr>
                                <td><strong><%= p.getIdPeminjaman() %></strong></td>
                                <td><%= p.getPeminjam().getNama() %></td>
                                <td><%= p.getDokumenDipinjam().getJudul() %></td>
                                <td><%= p.getTanggalPinjam() %></td>
                                <td><span class="text-danger fw-bold"><%= p.getTanggalJatuhTempo() %></span></td>
                                <td>
                                    <a href="PengembalianServlet?action=kembalikan&id=<%= p.getIdPeminjaman() %>" class="btn btn-sm btn-success" onclick="return confirm('Proses pengembalian dokumen ini?');"><i class="bi bi-check-circle"></i> Kembalikan</a>
                                </td>
                            </tr>
                            <% 
                                    }
                                } else {
                            %>
                            <tr>
                                <td colspan="6" class="text-center text-muted py-4">Tidak ada transaksi peminjaman yang aktif saat ini.</td>
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

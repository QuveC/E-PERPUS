<%@page import="model.Pengembalian"%>
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
    <title>Sirkulasi Pengembalian - E-Perpus</title>
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
                    <li class="nav-item"><a class="nav-link" href="PeminjamanServlet">Peminjaman</a></li>
                    <li class="nav-item"><a class="nav-link active" href="PengembalianServlet">Pengembalian</a></li>
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
            <div class="card-header bg-white py-3">
                <h4 class="mb-0 text-danger"><i class="bi bi-arrow-return-left"></i> Sirkulasi Pengembalian & Denda</h4>
                <p class="text-muted mb-0 mt-1 small">Riwayat dokumen yang dikembalikan beserta catatan denda keterlambatannya.</p>
            </div>
            
            <div class="card-body">
                <div class="table-responsive">
                    <table class="table table-hover align-middle">
                        <thead class="table-light">
                            <tr>
                                <th>ID Pengembalian</th>
                                <th>Peminjam</th>
                                <th>Dokumen</th>
                                <th>Jatuh Tempo</th>
                                <th>Dikembalikan</th>
                                <th>Total Denda</th>
                            </tr>
                        </thead>
                        <tbody>
                            <% 
                                List<Pengembalian> listPengembalian = (List<Pengembalian>) request.getAttribute("listPengembalian");
                                if (listPengembalian != null && !listPengembalian.isEmpty()) {
                                    for (Pengembalian p : listPengembalian) {
                                        String namaPeminjam = (p.getPeminjaman() != null && p.getPeminjaman().getPeminjam() != null) ? p.getPeminjaman().getPeminjam().getNama() : "Unknown";
                                        String namaDokumen = (p.getPeminjaman() != null && p.getPeminjaman().getDokumenDipinjam() != null) ? p.getPeminjaman().getDokumenDipinjam().getJudul() : "Unknown";
                                        String tglJatuhTempo = p.getPeminjaman() != null ? p.getPeminjaman().getTanggalJatuhTempo().toString() : "-";
                            %>
                            <tr>
                                <td><strong><%= p.getIdPengembalian() %></strong></td>
                                <td><%= namaPeminjam %></td>
                                <td><%= namaDokumen %></td>
                                <td><%= tglJatuhTempo %></td>
                                <td><span class="text-success fw-bold"><%= p.getTanggalKembaliAktual() %></span></td>
                                <td>
                                    <% if (p.getJumlahDenda() > 0) { %>
                                        <span class="badge bg-danger rounded-pill px-3 py-2">Rp <%= String.format("%,.0f", p.getJumlahDenda()) %></span>
                                    <% } else { %>
                                        <span class="badge bg-secondary rounded-pill px-3 py-2">Rp 0</span>
                                    <% } %>
                                </td>
                            </tr>
                            <% 
                                    }
                                } else {
                            %>
                            <tr>
                                <td colspan="6" class="text-center text-muted py-4">Belum ada riwayat pengembalian dokumen.</td>
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

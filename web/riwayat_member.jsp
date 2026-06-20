<%@page import="model.Peminjaman"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    String userRole = (String) session.getAttribute("userRole");
    if (userRole == null || !"MEMBER".equals(userRole)) {
        response.sendRedirect("index.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html lang="id">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Riwayat Peminjaman - E-Perpus</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css" rel="stylesheet">
</head>
<body class="bg-light">
    <!-- Navbar Member -->
    <nav class="navbar navbar-expand-lg navbar-dark bg-success shadow-sm mb-4">
        <div class="container">
            <a class="navbar-brand fw-bold" href="dashboard_member.jsp"><i class="bi bi-book"></i> E-Perpus Member</a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarNav">
                <ul class="navbar-nav ms-auto">
                    <li class="nav-item"><a class="nav-link" href="dashboard_member.jsp">Beranda</a></li>
                    <li class="nav-item"><a class="nav-link" href="PencarianServlet">Pencarian Katalog</a></li>
                    <li class="nav-item"><a class="nav-link active" href="RiwayatMemberServlet">Riwayat Peminjaman Saya</a></li>
                    <li class="nav-item ms-lg-3"><a class="btn btn-danger btn-sm mt-1" href="LoginServlet?action=logout"><i class="bi bi-box-arrow-right"></i> Logout</a></li>
                </ul>
            </div>
        </div>
    </nav>

    <div class="container mb-5">
        <div class="mb-3">
            <a href="dashboard_member.jsp" class="btn btn-outline-success btn-sm"><i class="bi bi-arrow-left"></i> Kembali ke Dashboard</a>
        </div>
        <div class="card shadow-sm border-0">
            <div class="card-header bg-white py-3">
                <h4 class="mb-0 text-warning text-dark"><i class="bi bi-journal-check"></i> Riwayat Peminjaman Saya</h4>
            </div>
            <div class="card-body">
                <div class="table-responsive">
                    <table class="table table-hover align-middle">
                        <thead class="table-light">
                            <tr>
                                <th>ID Transaksi</th>
                                <th>Judul Buku/Majalah</th>
                                <th>Tanggal Pinjam</th>
                                <th>Jatuh Tempo</th>
                                <th>Status</th>
                            </tr>
                        </thead>
                        <tbody>
                            <% 
                                List<Peminjaman> riwayat = (List<Peminjaman>) request.getAttribute("riwayatPeminjaman");
                                if (riwayat != null && !riwayat.isEmpty()) {
                                    for (Peminjaman p : riwayat) {
                                        String badgeStatus = "AKTIF".equals(p.getStatus()) ? "bg-warning text-dark" : "bg-success";
                            %>
                            <tr>
                                <td><strong><%= p.getIdPeminjaman() %></strong></td>
                                <td><%= p.getDokumenDipinjam().getJudul() != null ? p.getDokumenDipinjam().getJudul() : p.getDokumenDipinjam().getIdDokumen() %></td>
                                <td><%= p.getTanggalPinjam() %></td>
                                <td><%= p.getTanggalJatuhTempo() %></td>
                                <td><span class="badge <%= badgeStatus %>"><%= p.getStatus() %></span></td>
                            </tr>
                            <% 
                                    }
                                } else {
                            %>
                            <tr>
                                <td colspan="5" class="text-center text-muted py-4">Anda belum pernah meminjam dokumen apapun.</td>
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

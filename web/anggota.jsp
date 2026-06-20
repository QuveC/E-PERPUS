<%@page import="model.AnggotaAktif"%>
<%@page import="model.AnggotaNonAktif"%>
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
    <title>Manajemen Anggota - E-Perpus</title>
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
                    <li class="nav-item"><a class="nav-link active" href="AnggotaServlet">Anggota</a></li>
                    <li class="nav-item"><a class="nav-link" href="PencarianServlet">Pencarian</a></li>
                    <li class="nav-item"><a class="nav-link" href="PeminjamanServlet">Peminjaman</a></li>
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
        <div class="card shadow-sm border-0 mb-4">
            <div class="card-header bg-white py-3 d-flex justify-content-between align-items-center">
                <h4 class="mb-0 text-success"><i class="bi bi-people"></i> Manajemen Anggota</h4>
                <a href="AnggotaServlet?action=new" class="btn btn-success"><i class="bi bi-person-plus"></i> Tambah Anggota</a>
            </div>
            
            <div class="card-body">
                <ul class="nav nav-tabs" id="anggotaTab" role="tablist">
                    <li class="nav-item" role="presentation">
                        <button class="nav-link active" id="aktif-tab" data-bs-toggle="tab" data-bs-target="#aktif" type="button" role="tab">Anggota Aktif</button>
                    </li>
                    <li class="nav-item" role="presentation">
                        <button class="nav-link" id="nonaktif-tab" data-bs-toggle="tab" data-bs-target="#nonaktif" type="button" role="tab">Anggota Non-Aktif</button>
                    </li>
                </ul>
                
                <div class="tab-content pt-3" id="anggotaTabContent">
                    <!-- Tab Anggota Aktif -->
                    <div class="tab-pane fade show active" id="aktif" role="tabpanel">
                        <div class="table-responsive">
                            <table class="table table-hover align-middle">
                                <thead class="table-light">
                                    <tr>
                                        <th>ID Anggota</th>
                                        <th>Nama</th>
                                        <th>Peran</th>
                                        <th>Email</th>
                                        <th>Tgl Bergabung</th>
                                        <th>Aksi</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <% 
                                        List<AnggotaAktif> listAktif = (List<AnggotaAktif>) request.getAttribute("listAktif");
                                        if (listAktif != null && !listAktif.isEmpty()) {
                                            for (AnggotaAktif a : listAktif) {
                                    %>
                                    <tr>
                                        <td><strong><%= a.getIdAnggota() %></strong></td>
                                        <td><%= a.getNama() %></td>
                                        <td><span class="badge bg-info text-dark"><%= a.getRole() %></span></td>
                                        <td><%= a.getEmail() %></td>
                                        <td><%= a.getTanggalBergabung() %></td>
                                        <td>
                                            <a href="AnggotaServlet?action=nonaktifkan&id=<%= a.getIdAnggota() %>" class="btn btn-sm btn-outline-danger" onclick="return confirm('Nonaktifkan anggota ini?');"><i class="bi bi-person-x"></i> Nonaktifkan</a>
                                        </td>
                                    </tr>
                                    <% 
                                            }
                                        } else {
                                    %>
                                    <tr><td colspan="6" class="text-center text-muted py-3">Belum ada anggota aktif.</td></tr>
                                    <% } %>
                                </tbody>
                            </table>
                        </div>
                    </div>

                    <!-- Tab Anggota Non-Aktif -->
                    <div class="tab-pane fade" id="nonaktif" role="tabpanel">
                        <div class="table-responsive">
                            <table class="table table-hover align-middle">
                                <thead class="table-light">
                                    <tr>
                                        <th>ID Anggota</th>
                                        <th>Nama</th>
                                        <th>Tgl Non-Aktif</th>
                                        <th>Alasan</th>
                                        <th>Aksi</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <% 
                                        List<AnggotaNonAktif> listNonAktif = (List<AnggotaNonAktif>) request.getAttribute("listNonAktif");
                                        if (listNonAktif != null && !listNonAktif.isEmpty()) {
                                            for (AnggotaNonAktif na : listNonAktif) {
                                    %>
                                    <tr>
                                        <td><strong><%= na.getIdAnggota() %></strong></td>
                                        <td><%= na.getNama() %></td>
                                        <td><%= na.getTanggalNonaktif() %></td>
                                        <td><%= na.getAlasanNonaktif() %></td>
                                        <td>
                                            <a href="AnggotaServlet?action=reaktivasi&id=<%= na.getIdAnggota() %>" class="btn btn-sm btn-outline-success" onclick="return confirm('Aktifkan kembali anggota ini?');"><i class="bi bi-person-check"></i> Reaktivasi</a>
                                        </td>
                                    </tr>
                                    <% 
                                            }
                                        } else {
                                    %>
                                    <tr><td colspan="5" class="text-center text-muted py-3">Tidak ada anggota non-aktif.</td></tr>
                                    <% } %>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>

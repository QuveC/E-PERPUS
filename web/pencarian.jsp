<%@page import="model.Dokumen"%>
<%@page import="model.Buku"%>
<%@page import="model.Majalah"%>
<%@page import="model.HasilPencarian"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    String sessionRole = (String) session.getAttribute("userRole");
    if (sessionRole == null) {
        response.sendRedirect("index.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html lang="id">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Pencarian Katalog - E-Perpus</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css" rel="stylesheet">
</head>
<body class="bg-light">
    <!-- Navbar Dinamis berdasarkan Role -->
    <% 
        String userRole = (String) session.getAttribute("userRole");
        if ("MEMBER".equals(userRole)) {
    %>
    <nav class="navbar navbar-expand-lg navbar-dark bg-success shadow-sm mb-4">
        <div class="container">
            <a class="navbar-brand fw-bold" href="dashboard_member.jsp"><i class="bi bi-book"></i> E-Perpus Member</a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarNav">
                <ul class="navbar-nav ms-auto">
                    <li class="nav-item"><a class="nav-link" href="dashboard_member.jsp">Beranda</a></li>
                    <li class="nav-item"><a class="nav-link active" href="PencarianServlet">Pencarian Katalog</a></li>
                    <li class="nav-item"><a class="nav-link" href="RiwayatMemberServlet">Riwayat Peminjaman Saya</a></li>
                    <li class="nav-item ms-lg-3"><a class="btn btn-danger btn-sm mt-1" href="LoginServlet?action=logout"><i class="bi bi-box-arrow-right"></i> Logout</a></li>
                </ul>
            </div>
        </div>
    </nav>
    <% } else { %>
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
                    <li class="nav-item"><a class="nav-link active" href="PencarianServlet">Pencarian</a></li>
                    <li class="nav-item"><a class="nav-link" href="PeminjamanServlet">Peminjaman</a></li>
                    <li class="nav-item"><a class="nav-link" href="PengembalianServlet">Pengembalian</a></li>
                    <li class="nav-item ms-lg-3"><a class="btn btn-danger btn-sm mt-1" href="LoginServlet?action=logout"><i class="bi bi-box-arrow-right"></i> Logout</a></li>
                </ul>
            </div>
        </div>
    </nav>
    <% } %>

    <div class="container mb-5">
        <div class="mb-3">
            <%
                String role = (String) session.getAttribute("userRole");
                String backUrl = "index.jsp";
                String btnClass = "btn-outline-secondary";
                if ("ADMIN".equals(role)) {
                    backUrl = "dashboard_admin.jsp";
                    btnClass = "btn-outline-primary";
                } else if ("MEMBER".equals(role)) {
                    backUrl = "dashboard_member.jsp";
                    btnClass = "btn-outline-success";
                }
            %>
            <a href="<%= backUrl %>" class="btn <%= btnClass %> btn-sm"><i class="bi bi-arrow-left"></i> Kembali ke Dashboard</a>
        </div>
        <div class="card shadow-sm border-0 bg-white">
            <div class="card-body p-5">
                <h2 class="text-center text-info fw-bold mb-4"><i class="bi bi-search"></i> Pencarian Katalog Perpustakaan</h2>
                
                <form action="PencarianServlet" method="GET" class="row justify-content-center mb-4">
                    <div class="col-md-8 col-lg-6">
                        <div class="input-group input-group-lg">
                            <input type="text" name="keyword" class="form-control border-info" placeholder="Cari judul, pengarang, dll..." 
                                   value="<%= request.getParameter("keyword") != null ? request.getParameter("keyword") : "" %>" required>
                            <button type="submit" class="btn btn-info text-white"><i class="bi bi-search"></i> Cari</button>
                        </div>
                    </div>
                </form>
                
                <div class="row justify-content-center">
                    <div class="col-md-8 col-lg-6">
                        <div class="alert alert-info py-2 px-3 text-center" style="font-size: 0.9rem;">
                            <strong>Tips Lanjutan:</strong> Gunakan prefix. 
                            <code>JUDUL:Laskar</code>, <code>PENGARANG:Andrea</code>, <code>TAHUN:2020</code>, <code>JENIS:BUKU</code>, atau <code>LOKASI:RAK-01</code>
                        </div>
                    </div>
                </div>

                <% 
                    HasilPencarian hasil = (HasilPencarian) request.getAttribute("hasilPencarian");
                    if (hasil != null) {
                        boolean isSearching = hasil.getKataKunci() != null && !hasil.getKataKunci().trim().isEmpty();
                %>
                <hr class="my-5">
                <h4 class="mb-4">
                    <% if (isSearching) { %>
                        Hasil Pencarian: "<span class="text-info"><%= hasil.getKataKunci() %></span>"
                    <% } else { %>
                        Daftar Semua Katalog Perpustakaan
                    <% } %>
                    <span class="badge bg-secondary ms-2"><%= hasil.getJumlahHasil() %> ditemukan</span>
                </h4>
                
                <% if (hasil.getJumlahHasil() > 0) { %>
                <div class="table-responsive">
                    <table class="table table-hover align-middle">
                        <thead class="table-light">
                            <tr>
                                <th>ID Dokumen</th>
                                <th>Judul</th>
                                <th>Pengarang</th>
                                <th>Tipe</th>
                                <th>Tahun</th>
                                <th>Aksi</th>
                            </tr>
                        </thead>
                        <tbody>
                            <% for (Dokumen dok : hasil.getDaftarHasil()) { 
                                String tipe = dok instanceof Buku ? "BUKU" : "MAJALAH";
                                String badgeClass = dok instanceof Buku ? "bg-info text-dark" : "bg-secondary";
                            %>
                            <tr>
                                <td><strong><%= dok.getIdDokumen() %></strong></td>
                                <td><%= dok.getJudul() %></td>
                                <td><%= dok.getPengarang() %></td>
                                <td><span class="badge <%= badgeClass %>"><%= tipe %></span></td>
                                <td><%= dok.getTahunTerbit() %></td>
                                <td>
                                    <button type="button" class="btn btn-sm btn-info text-white fw-bold" data-bs-toggle="modal" data-bs-target="#detailModal<%= dok.getIdDokumen() %>">
                                        <i class="bi bi-info-circle"></i> Detail
                                    </button>
                                </td>
                            </tr>
                            <% } %>
                        </tbody>
                    </table>
                </div>
                
                <!-- Modal Detail (Rendered outside table to prevent DOM bugs) -->
                <% for (Dokumen dok : hasil.getDaftarHasil()) { 
                    String tipe = dok instanceof Buku ? "BUKU" : "MAJALAH";
                %>
                <div class="modal fade" id="detailModal<%= dok.getIdDokumen() %>" tabindex="-1" aria-labelledby="detailModalLabel<%= dok.getIdDokumen() %>" aria-hidden="true">
                    <div class="modal-dialog modal-dialog-centered">
                        <div class="modal-content border-0 shadow-lg">
                            <div class="modal-header <%= dok instanceof Buku ? "bg-info text-dark" : "bg-secondary text-white" %>">
                                <h5 class="modal-title fw-bold" id="detailModalLabel<%= dok.getIdDokumen() %>">
                                    <i class="bi <%= dok instanceof Buku ? "bi-book" : "bi-journal-text" %>"></i> Detail <%= tipe %>
                                </h5>
                                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                            </div>
                            <div class="modal-body p-4">
                                <table class="table table-borderless align-middle mb-0">
                                    <tr>
                                        <td class="fw-bold" style="width: 35%;">ID Dokumen</td>
                                        <td>: <%= dok.getIdDokumen() %></td>
                                    </tr>
                                    <tr>
                                        <td class="fw-bold">Judul</td>
                                        <td class="text-primary fw-semibold">: <%= dok.getJudul() %></td>
                                    </tr>
                                    <tr>
                                        <td class="fw-bold">Pengarang</td>
                                        <td>: <%= dok.getPengarang() %></td>
                                    </tr>
                                    <tr>
                                        <td class="fw-bold">Tahun Terbit</td>
                                        <td>: <%= dok.getTahunTerbit() %></td>
                                    </tr>
                                    <% if (dok instanceof Buku) { 
                                        Buku b = (Buku) dok;
                                    %>
                                    <tr>
                                        <td class="fw-bold">ISBN</td>
                                        <td>: <%= b.getIsbn() != null ? b.getIsbn() : "-" %></td>
                                    </tr>
                                    <tr>
                                        <td class="fw-bold">Penerbit</td>
                                        <td>: <%= b.getPenerbit() != null ? b.getPenerbit() : "-" %></td>
                                    </tr>
                                    <tr>
                                        <td class="fw-bold">Jumlah Halaman</td>
                                        <td>: <%= b.getJumlahHalaman() %> halaman</td>
                                    </tr>
                                    <tr>
                                        <td class="fw-bold">Stok Tersedia</td>
                                        <td>: <span class="badge <%= b.getStok() > 0 ? "bg-success" : "bg-danger" %>"><%= b.getStok() %> eksemplar</span></td>
                                    </tr>
                                    <tr class="table-info">
                                        <td class="fw-bold text-dark"><i class="bi bi-geo-alt-fill text-danger"></i> Lokasi Rak</td>
                                        <td class="fw-bold text-dark">: <%= b.getLokasiRak() != null ? b.getLokasiRak() : "-" %></td>
                                    </tr>
                                    <% } else if (dok instanceof Majalah) { 
                                        Majalah m = (Majalah) dok;
                                    %>
                                    <tr>
                                        <td class="fw-bold">Edisi</td>
                                        <td>: <%= m.getEdisi() != null ? m.getEdisi() : "-" %></td>
                                    </tr>
                                    <tr>
                                        <td class="fw-bold">Bulan Terbit</td>
                                        <td>: <%= m.getBulanTerbit() != null ? m.getBulanTerbit() : "-" %></td>
                                    </tr>
                                    <tr>
                                        <td class="fw-bold">Frekuensi Terbit</td>
                                        <td>: <%= m.getFrekuensiTerbit() != null ? m.getFrekuensiTerbit() : "-" %></td>
                                    </tr>
                                    <tr class="table-secondary">
                                        <td class="fw-bold text-dark"><i class="bi bi-geo-alt-fill text-danger"></i> Lokasi Rak</td>
                                        <td class="fw-bold text-dark">: <%= m.getLokasiRak() != null ? m.getLokasiRak() : "-" %></td>
                                    </tr>
                                    <% } %>
                                </table>
                            </div>
                            <div class="modal-footer bg-light">
                                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Tutup</button>
                            </div>
                        </div>
                    </div>
                </div>
                <% } %>
                <% } else { %>
                    <div class="text-center py-5">
                        <i class="bi bi-inbox text-muted" style="font-size: 4rem;"></i>
                        <h4 class="text-muted mt-3">Tidak ada dokumen yang cocok</h4>
                        <p class="text-muted">Coba gunakan kata kunci yang lebih sederhana atau hapus filter.</p>
                    </div>
                <% } %>
                
                <% } %>
            </div>
        </div>
    </div>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>

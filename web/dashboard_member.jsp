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
    <title>Dashboard Anggota - E-Perpus</title>
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
                    <li class="nav-item"><a class="nav-link active" href="dashboard_member.jsp">Beranda</a></li>
                    <li class="nav-item"><a class="nav-link" href="PencarianServlet">Pencarian Katalog</a></li>
                    <li class="nav-item"><a class="nav-link" href="RiwayatMemberServlet">Riwayat Peminjaman Saya</a></li>
                    <li class="nav-item ms-lg-3"><a class="btn btn-danger btn-sm mt-1" href="LoginServlet?action=logout"><i class="bi bi-box-arrow-right"></i> Logout</a></li>
                </ul>
            </div>
        </div>
    </nav>

    <!-- Main Content -->
    <div class="container">
        <div class="card shadow-sm border-0 mb-4">
            <div class="card-body p-4 text-center">
                <h2 class="card-title fw-bold">Selamat Datang, <%= session.getAttribute("userName") %>!</h2>
                <p class="text-muted">Akses layanan perpustakaan Anda secara mandiri.</p>
            </div>
        </div>

        <div class="row g-4 justify-content-center">
            <!-- Pencarian -->
            <div class="col-md-6 col-lg-5">
                <a href="PencarianServlet" class="text-decoration-none">
                    <div class="card bg-info text-white h-100 shadow-sm border-0 transition-hover">
                        <div class="card-body text-center p-4">
                            <i class="bi bi-search display-4 mb-3"></i>
                            <h4 class="card-title">Cari Buku & Majalah</h4>
                            <p class="card-text opacity-75">Lihat ketersediaan koleksi di perpustakaan</p>
                        </div>
                    </div>
                </a>
            </div>

            <!-- Riwayat Pinjaman -->
            <div class="col-md-6 col-lg-5">
                <a href="RiwayatMemberServlet" class="text-decoration-none">
                    <div class="card bg-warning text-dark h-100 shadow-sm border-0 transition-hover">
                        <div class="card-body text-center p-4">
                            <i class="bi bi-journal-check display-4 mb-3"></i>
                            <h4 class="card-title">Riwayat Peminjaman Saya</h4>
                            <p class="card-text opacity-75">Cek batas waktu pengembalian dan denda</p>
                        </div>
                    </div>
                </a>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <style>
        .transition-hover { transition: transform 0.2s ease, box-shadow 0.2s ease; }
        .transition-hover:hover { transform: translateY(-5px); box-shadow: 0 .5rem 1rem rgba(0,0,0,.15)!important; }
    </style>
</body>
</html>

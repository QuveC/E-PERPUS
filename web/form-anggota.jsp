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
    <title>Tambah Anggota - E-Perpus</title>
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
        <div class="row justify-content-center">
            <div class="col-md-8">
                <div class="mb-3">
                    <a href="AnggotaServlet" class="btn btn-outline-secondary btn-sm"><i class="bi bi-arrow-left"></i> Kembali ke Daftar Anggota</a>
                </div>
                <div class="card shadow-sm border-0">
                    <div class="card-header bg-white py-3">
                        <h4 class="mb-0 text-success"><i class="bi bi-person-plus"></i> Registrasi Anggota Baru</h4>
                    </div>
                    <div class="card-body p-4">
                        <form action="AnggotaServlet" method="POST">
                            <input type="hidden" name="action" value="insert">
                            
                            <div class="row g-3 mb-3">
                                <div class="col-md-6">
                                    <label class="form-label">ID Anggota (NIM/NIP)</label>
                                    <input type="text" name="idAnggota" class="form-control" placeholder="Contoh: 1301201234" required>
                                </div>
                                <div class="col-md-6">
                                    <label class="form-label">Nama Lengkap</label>
                                    <input type="text" name="nama" class="form-control" required>
                                </div>
                                <div class="col-md-6">
                                    <label class="form-label">Email</label>
                                    <input type="email" name="email" class="form-control" required>
                                </div>
                                <div class="col-md-6">
                                    <label class="form-label">Nomor Telepon</label>
                                    <input type="text" name="noTelepon" class="form-control" required>
                                </div>
                                <div class="col-12">
                                    <label class="form-label">Peran (Role)</label>
                                    <select name="role" class="form-select" required>
                                        <option value="MAHASISWA">Mahasiswa</option>
                                        <option value="DOSEN">Dosen</option>
                                        <option value="STAF">Staf</option>
                                    </select>
                                </div>
                                <div class="col-12">
                                    <label class="form-label">Alamat</label>
                                    <textarea name="alamat" class="form-control" rows="3" required></textarea>
                                </div>
                            </div>

                            <div class="d-flex justify-content-end gap-2 mt-4 pt-3 border-top">
                                <a href="AnggotaServlet" class="btn btn-secondary">Batal</a>
                                <button type="submit" class="btn btn-success"><i class="bi bi-save"></i> Daftarkan Anggota</button>
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

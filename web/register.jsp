<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="id">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Daftar Anggota Baru - E-Perpus</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css" rel="stylesheet">
    <style>
        body { background-color: #f0f2f5; min-height: 100vh; display: flex; align-items: center; justify-content: center; padding: 2rem 0; }
        .register-card { border-radius: 1rem; overflow: hidden; box-shadow: 0 0.5rem 1rem rgba(0,0,0,0.15); border: none; width: 100%; max-width: 550px; }
    </style>
</head>
<body>
    <div class="container">
        <div class="row justify-content-center">
            <div class="col-12 d-flex justify-content-center">
                <div class="card register-card">
                    <div class="card-body p-5">
                        <div class="text-center mb-4">
                            <i class="bi bi-person-plus text-success" style="font-size: 3rem;"></i>
                            <h3 class="fw-bold mt-2">Daftar Anggota Baru</h3>
                            <p class="text-muted">Isi formulir di bawah untuk membuat akun perpustakaan</p>
                        </div>
                        
                        <% 
                            String errorMsg = (String) request.getAttribute("errorMessage");
                            if (errorMsg != null) {
                        %>
                        <div class="alert alert-danger" role="alert">
                            <i class="bi bi-exclamation-triangle-fill"></i> <%= errorMsg %>
                        </div>
                        <% } %>

                        <form action="RegisterServlet" method="POST">
                            <div class="mb-3">
                                <label class="form-label fw-semibold">ID Anggota / NIM / NIP</label>
                                <div class="input-group">
                                    <span class="input-group-text"><i class="bi bi-card-text"></i></span>
                                    <input type="text" name="idAnggota" class="form-control" placeholder="Contoh: 1301213000" required>
                                </div>
                                <div class="form-text">Ini akan digunakan sebagai Username dan Password login awal Anda.</div>
                            </div>
                            
                            <div class="mb-3">
                                <label class="form-label fw-semibold">Nama Lengkap</label>
                                <div class="input-group">
                                    <span class="input-group-text"><i class="bi bi-person"></i></span>
                                    <input type="text" name="nama" class="form-control" placeholder="Nama Lengkap Anda" required>
                                </div>
                            </div>

                            <div class="mb-3">
                                <label class="form-label fw-semibold">Email</label>
                                <div class="input-group">
                                    <span class="input-group-text"><i class="bi bi-envelope"></i></span>
                                    <input type="email" name="email" class="form-control" placeholder="nama@email.com" required>
                                </div>
                            </div>

                            <div class="mb-3">
                                <label class="form-label fw-semibold">Nomor Telepon</label>
                                <div class="input-group">
                                    <span class="input-group-text"><i class="bi bi-telephone"></i></span>
                                    <input type="text" name="noTelepon" class="form-control" placeholder="0812xxxxxxxx">
                                </div>
                            </div>

                            <div class="mb-3">
                                <label class="form-label fw-semibold">Peran (Role)</label>
                                <div class="input-group">
                                    <span class="input-group-text"><i class="bi bi-briefcase"></i></span>
                                    <select name="role" class="form-select" required>
                                        <option value="Mahasiswa" selected>Mahasiswa</option>
                                        <option value="Dosen">Dosen</option>
                                        <option value="Staff">Staff</option>
                                        <option value="Umum">Umum</option>
                                    </select>
                                </div>
                            </div>

                            <div class="mb-4">
                                <label class="form-label fw-semibold">Alamat</label>
                                <div class="input-group">
                                    <span class="input-group-text"><i class="bi bi-geo-alt"></i></span>
                                    <textarea name="alamat" class="form-control" rows="2" placeholder="Alamat Lengkap"></textarea>
                                </div>
                            </div>
                            
                            <button type="submit" class="btn btn-success w-100 py-2 fw-bold mb-3">Buat Akun</button>
                            <div class="text-center">
                                <span class="text-muted">Sudah punya akun?</span>
                                <a href="index.jsp" class="text-decoration-none fw-semibold text-success ms-1">Login di sini</a>
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

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="id">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login - E-Perpus</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css" rel="stylesheet">
    <style>
        body { background-color: #f0f2f5; height: 100vh; display: flex; align-items: center; justify-content: center; }
        .login-card { border-radius: 1rem; overflow: hidden; box-shadow: 0 0.5rem 1rem rgba(0,0,0,0.15); border: none; }
    </style>
</head>
<body>
    <div class="container">
        <div class="row justify-content-center">
            <div class="col-md-6 col-lg-5">
                <div class="card login-card">
                    <div class="card-body p-5">
                        <div class="text-center mb-4">
                            <i class="bi bi-book text-primary" style="font-size: 3rem;"></i>
                            <h3 class="fw-bold mt-2">E-Perpus</h3>
                            <p class="text-muted">Sistem Manajemen Perpustakaan</p>
                        </div>
                        
                        <% 
                            String errorMsg = (String) request.getAttribute("errorMessage");
                            if (errorMsg != null) {
                        %>
                        <div class="alert alert-danger" role="alert">
                            <i class="bi bi-exclamation-triangle-fill"></i> <%= errorMsg %>
                        </div>
                        <% } %>

                        <% 
                            String successMsg = (String) request.getAttribute("successMessage");
                            if (successMsg != null) {
                        %>
                        <div class="alert alert-success" role="alert">
                            <i class="bi bi-check-circle-fill"></i> <%= successMsg %>
                        </div>
                        <% } %>

                        <form action="LoginServlet" method="POST">
                            <input type="hidden" name="action" value="login">
                            
                            <div class="mb-3">
                                <label class="form-label">Username / ID Anggota</label>
                                <div class="input-group">
                                    <span class="input-group-text"><i class="bi bi-person"></i></span>
                                    <input type="text" name="username" class="form-control" placeholder="admin atau NIM/NIP" required>
                                </div>
                            </div>
                            
                            <div class="mb-4">
                                <label class="form-label">Password</label>
                                <div class="input-group">
                                    <span class="input-group-text"><i class="bi bi-lock"></i></span>
                                    <input type="password" name="password" class="form-control" placeholder="Masukkan password" required>
                                </div>
                                <div class="form-text mt-2"><i class="bi bi-info-circle"></i> Info: Member login menggunakan NIM sebagai username dan password.</div>
                            </div>
                            
                            <button type="submit" class="btn btn-primary w-100 py-2 fw-bold mb-3">Login</button>
                            <div class="text-center">
                                <span class="text-muted">Belum punya akun?</span>
                                <a href="register.jsp" class="text-decoration-none fw-semibold text-primary ms-1">Buat Akun</a>
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

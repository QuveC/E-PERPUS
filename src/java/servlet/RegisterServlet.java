package servlet;

import controller.ManagerAnggota;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.AnggotaAktif;
import util.DBConnection;

@WebServlet(name = "RegisterServlet", urlPatterns = {"/RegisterServlet"})
public class RegisterServlet extends HttpServlet {

    private ManagerAnggota managerAnggota;

    @Override
    public void init() throws ServletException {
        try {
            Connection conn = DBConnection.getConnection();
            managerAnggota = new ManagerAnggota(conn);
        } catch (SQLException e) {
            throw new ServletException("Koneksi DB gagal", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idAnggota = request.getParameter("idAnggota");
        String nama = request.getParameter("nama");
        String email = request.getParameter("email");
        String noTelepon = request.getParameter("noTelepon");
        String alamat = request.getParameter("alamat");
        String role = request.getParameter("role");

        if (idAnggota == null || idAnggota.trim().isEmpty() ||
            nama == null || nama.trim().isEmpty() ||
            email == null || email.trim().isEmpty()) {
            request.setAttribute("errorMessage", "ID Anggota, Nama, dan Email wajib diisi.");
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }

        try {
            // Cek apakah ID Anggota sudah terdaftar
            if (managerAnggota.isIdAnggotaExist(idAnggota)) {
                request.setAttribute("errorMessage", "ID Anggota / NIM sudah terdaftar.");
                request.getRequestDispatcher("register.jsp").forward(request, response);
                return;
            }

            // Cek apakah Email sudah terdaftar
            if (managerAnggota.isEmailExist(email)) {
                request.setAttribute("errorMessage", "Email sudah terdaftar.");
                request.getRequestDispatcher("register.jsp").forward(request, response);
                return;
            }

            // Buat objek AnggotaAktif
            AnggotaAktif anggota = new AnggotaAktif(
                    idAnggota, nama, email, noTelepon, alamat, role, LocalDate.now(), 3
            );

            // Simpan ke database
            if (managerAnggota.tambahAnggotaAktif(anggota)) {
                request.setAttribute("successMessage", "Registrasi berhasil! Silakan login menggunakan ID Anggota Anda sebagai Username dan Password.");
                request.getRequestDispatcher("index.jsp").forward(request, response);
            } else {
                request.setAttribute("errorMessage", "Terjadi kesalahan saat registrasi. Silakan coba lagi.");
                request.getRequestDispatcher("register.jsp").forward(request, response);
            }

        } catch (Exception e) {
            request.setAttribute("errorMessage", "Error: " + e.getMessage());
            request.getRequestDispatcher("register.jsp").forward(request, response);
        }
    }
}

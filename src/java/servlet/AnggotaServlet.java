package servlet;

import controller.ManagerAnggota;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.AnggotaAktif;
import model.AnggotaNonAktif;
import util.DBConnection;

@WebServlet(name = "AnggotaServlet", urlPatterns = {"/AnggotaServlet"})
public class AnggotaServlet extends HttpServlet {

    private ManagerAnggota manager;
    private Connection conn;

    @Override
    public void init() throws ServletException {
        try {
            conn = DBConnection.getConnection();
            manager = new ManagerAnggota(conn);
        } catch (SQLException e) {
            throw new ServletException("Koneksi DB gagal", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        javax.servlet.http.HttpSession session = request.getSession();
        if (session.getAttribute("userRole") == null || !"ADMIN".equals(session.getAttribute("userRole"))) {
            response.sendRedirect("index.jsp");
            return;
        }

        String action = request.getParameter("action");
        if (action == null) {
            action = "list";
        }

        switch (action) {
            case "new":
                showNewForm(request, response);
                break;
            case "nonaktifkan":
                nonaktifkanAnggota(request, response);
                break;
            case "reaktivasi":
                reaktivasiAnggota(request, response);
                break;
            default:
                listAnggota(request, response);
                break;
        }
    }

    private void listAnggota(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<AnggotaAktif> listAktif = manager.getAllAnggotaAktif();
        List<AnggotaNonAktif> listNonAktif = manager.getAllAnggotaNonAktif();
        
        request.setAttribute("listAktif", listAktif);
        request.setAttribute("listNonAktif", listNonAktif);
        request.getRequestDispatcher("anggota.jsp").forward(request, response);
    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("form-anggota.jsp").forward(request, response);
    }

    private void nonaktifkanAnggota(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String id = request.getParameter("id");
        String alasan = "Dinonaktifkan oleh sistem web.";
        manager.nonaktifkanAnggota(id, alasan);
        response.sendRedirect("AnggotaServlet");
    }

    private void reaktivasiAnggota(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String id = request.getParameter("id");
        manager.reaktivasiAnggota(id);
        response.sendRedirect("AnggotaServlet");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
            
        javax.servlet.http.HttpSession session = request.getSession();
        if (session.getAttribute("userRole") == null || !"ADMIN".equals(session.getAttribute("userRole"))) {
            response.sendRedirect("index.jsp");
            return;
        }

        String action = request.getParameter("action");
        
        if ("insert".equals(action)) {
            String idAnggota = request.getParameter("idAnggota");
            String nama = request.getParameter("nama");
            String email = request.getParameter("email");
            String noTelepon = request.getParameter("noTelepon");
            String alamat = request.getParameter("alamat");
            String role = request.getParameter("role"); // MAHASISWA atau DOSEN
            
            // Batas pinjam default 3 untuk Mahasiswa, 5 untuk dosen dsb. Sederhanakan jadi 3 saja sesuai parameter Manager.
            AnggotaAktif anggota = new AnggotaAktif(idAnggota, nama, email, noTelepon, alamat, role, LocalDate.now(), 3);
            manager.tambahAnggotaAktif(anggota);
        }
        
        response.sendRedirect("AnggotaServlet");
    }
}

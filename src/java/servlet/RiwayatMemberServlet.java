package servlet;

import controller.ManagerPeminjaman;
import controller.ManagerKoleksi;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.Peminjaman;
import model.Dokumen;
import util.DBConnection;

@WebServlet(name = "RiwayatMemberServlet", urlPatterns = {"/RiwayatMemberServlet"})
public class RiwayatMemberServlet extends HttpServlet {

    private ManagerPeminjaman managerPeminjaman;
    private ManagerKoleksi managerKoleksi;

    @Override
    public void init() throws ServletException {
        try {
            Connection conn = DBConnection.getConnection();
            managerPeminjaman = new ManagerPeminjaman(conn);
            managerKoleksi = new ManagerKoleksi(conn);
        } catch (SQLException e) {
            throw new ServletException("Koneksi DB gagal", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Proteksi
        HttpSession session = request.getSession();
        String userRole = (String) session.getAttribute("userRole");
        if (userRole == null || !"MEMBER".equals(userRole)) {
            response.sendRedirect("index.jsp");
            return;
        }

        String userId = (String) session.getAttribute("userId");
        List<Peminjaman> riwayat = managerPeminjaman.getPeminjamanByAnggota(userId);

        // Ambil nama dokumen lengkap dari ManagerKoleksi (karena mapRow hanya ID)
        for (Peminjaman p : riwayat) {
            Dokumen dok = managerKoleksi.cariDokumen(p.getDokumenDipinjam().getIdDokumen());
            if (dok != null) {
                p.setDokumenDipinjam(dok);
            }
        }

        request.setAttribute("riwayatPeminjaman", riwayat);
        request.getRequestDispatcher("riwayat_member.jsp").forward(request, response);
    }
}

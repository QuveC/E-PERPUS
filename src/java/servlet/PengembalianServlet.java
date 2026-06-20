package servlet;

import controller.ManagerPengembalian;
import controller.ManagerAnggota;
import controller.ManagerKoleksi;
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
import model.Peminjaman;
import model.Pengembalian;
import model.Dokumen;
import model.AnggotaAktif;
import util.DBConnection;

@WebServlet(name = "PengembalianServlet", urlPatterns = {"/PengembalianServlet"})
public class PengembalianServlet extends HttpServlet {

    private ManagerPengembalian managerPengembalian;
    private ManagerAnggota managerAnggota;
    private ManagerKoleksi managerKoleksi;
    private Connection conn;

    @Override
    public void init() throws ServletException {
        try {
            conn = DBConnection.getConnection();
            managerPengembalian = new ManagerPengembalian(conn);
            managerAnggota = new ManagerAnggota(conn);
            managerKoleksi = new ManagerKoleksi(conn);
        } catch (SQLException e) {
            throw new ServletException("Koneksi DB gagal", e);
        }
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
        if (action == null) action = "list";

        if ("kembalikan".equals(action)) {
            String idPeminjaman = request.getParameter("id");
            if (idPeminjaman != null && !idPeminjaman.isEmpty()) {
                // Proses pengembalian (otomatis insert denda kalau ada)
                managerPengembalian.prosesPengembalian(idPeminjaman, LocalDate.now());
            }
            response.sendRedirect("PengembalianServlet");
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
        if (action == null) action = "list";

        if ("kembalikan".equals(action)) {
            String idPeminjaman = request.getParameter("id");
            if (idPeminjaman != null && !idPeminjaman.isEmpty()) {
                // Proses pengembalian (otomatis insert denda kalau ada)
                managerPengembalian.prosesPengembalian(idPeminjaman, LocalDate.now());
            }
            response.sendRedirect("PengembalianServlet");
        } else {
            List<Pengembalian> listPengembalian = managerPengembalian.getDaftarPengembalian();
            
            // Hydrate Peminjaman data agar UI bisa menampilkan nama
            if (listPengembalian != null) {
                for (Pengembalian pgb : listPengembalian) {
                    Peminjaman pem = managerPengembalian.getManagerPeminjaman().cariById(pgb.getPeminjaman().getIdPeminjaman());
                    if (pem != null) {
                        AnggotaAktif a = managerAnggota.getAnggotaAktifById(pem.getPeminjam().getIdAnggota());
                        if (a != null) pem.setPeminjam(a);
                        
                        Dokumen dok = managerKoleksi.getBukuById(pem.getDokumenDipinjam().getIdDokumen());
                        if (dok == null) dok = managerKoleksi.getMajalahById(pem.getDokumenDipinjam().getIdDokumen());
                        if (dok != null) pem.setDokumenDipinjam(dok);
                        
                        pgb.setPeminjaman(pem);
                    }
                }
            }
            
            request.setAttribute("listPengembalian", listPengembalian);
            request.getRequestDispatcher("pengembalian.jsp").forward(request, response);
        }
    }
}

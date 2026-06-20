package servlet;

import controller.ManagerAnggota;
import controller.ManagerKoleksi;
import controller.ManagerPeminjaman;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.AnggotaAktif;
import model.Dokumen;
import model.Peminjaman;
import util.DBConnection;

@WebServlet(name = "PeminjamanServlet", urlPatterns = {"/PeminjamanServlet"})
public class PeminjamanServlet extends HttpServlet {

    private ManagerPeminjaman managerPeminjaman;
    private ManagerAnggota managerAnggota;
    private ManagerKoleksi managerKoleksi;
    private Connection conn;

    @Override
    public void init() throws ServletException {
        try {
            conn = DBConnection.getConnection();
            managerPeminjaman = new ManagerPeminjaman(conn);
            managerAnggota = new ManagerAnggota(conn);
            managerKoleksi = new ManagerKoleksi(conn);
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
        if (action == null) action = "list";

        if ("new".equals(action)) {
            List<AnggotaAktif> listAnggota = managerAnggota.getAllAnggotaAktif();
            List<Dokumen> listDokumen = managerKoleksi.getAllDokumen();
            
            request.setAttribute("listAnggota", listAnggota);
            request.setAttribute("listDokumen", listDokumen);
            request.getRequestDispatcher("form-peminjaman.jsp").forward(request, response);
        } else {
            List<Peminjaman> listPeminjaman = managerPeminjaman.getPeminjamanAktif();
            
            // Hydrate shell objects agar nama dan judul bisa ditampilkan di JSP
            if (listPeminjaman != null) {
                for (Peminjaman p : listPeminjaman) {
                    if (p.getPeminjam() != null) {
                        AnggotaAktif a = managerAnggota.getAnggotaAktifById(p.getPeminjam().getIdAnggota());
                        if (a != null) p.setPeminjam(a);
                    }
                    if (p.getDokumenDipinjam() != null) {
                        Dokumen dok = managerKoleksi.getBukuById(p.getDokumenDipinjam().getIdDokumen());
                        if (dok == null) {
                            dok = managerKoleksi.getMajalahById(p.getDokumenDipinjam().getIdDokumen());
                        }
                        if (dok != null) p.setDokumenDipinjam(dok);
                    }
                }
            }
            
            request.setAttribute("listPeminjaman", listPeminjaman);
            request.getRequestDispatcher("peminjaman.jsp").forward(request, response);
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
        
        if ("insert".equals(action)) {
            String idAnggota = request.getParameter("idAnggota");
            String idDokumen = request.getParameter("idDokumen");
            
            AnggotaAktif anggota = managerAnggota.getAnggotaAktifById(idAnggota);
            Dokumen dokumen = managerKoleksi.getBukuById(idDokumen);
            if (dokumen == null) {
                dokumen = managerKoleksi.getMajalahById(idDokumen);
            }
            
            if (anggota != null && dokumen != null) {
                managerPeminjaman.prosesKeminjaman(anggota, dokumen);
            }
        }
        
        response.sendRedirect("PeminjamanServlet");
    }
}

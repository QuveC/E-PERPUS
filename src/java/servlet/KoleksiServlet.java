package servlet;

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
import model.Buku;
import model.Dokumen;
import model.Majalah;
import util.DBConnection;

@WebServlet(name = "KoleksiServlet", urlPatterns = {"/KoleksiServlet"})
public class KoleksiServlet extends HttpServlet {

    private ManagerKoleksi manager;
    private Connection conn;

    @Override
    public void init() throws ServletException {
        try {
            conn = DBConnection.getConnection();
            manager = new ManagerKoleksi(conn);
        } catch (SQLException e) {
            throw new ServletException("Koneksi DB gagal", e);
        }
    }

    @Override
    public void destroy() {
        // Jangan tutup koneksi secara keseluruhan jika dipakai servlet lain, tapi di tugas ini bisa ditutup.
        // DBConnection.closeConnection(); 
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

        try {
            switch (action) {
                case "new":
                case "newBuku":
                    showNewFormBuku(request, response);
                    break;
                case "newMajalah":
                    showNewFormMajalah(request, response);
                    break;
                case "edit":
                    showEditForm(request, response);
                    break;
                case "delete":
                    deleteKoleksi(request, response);
                    break;
                default:
                    listKoleksi(request, response);
                    break;
            }
        } catch (Exception ex) {
            throw new ServletException(ex);
        }
    }

    private void listKoleksi(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Dokumen> listKoleksi = manager.getAllDokumen();
        request.setAttribute("listKoleksi", listKoleksi);
        request.getRequestDispatcher("koleksi.jsp").forward(request, response);
    }

    private void showNewFormBuku(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("tipeForm", "BUKU");
        request.getRequestDispatcher("form-koleksi.jsp").forward(request, response);
    }

    private void showNewFormMajalah(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("tipeForm", "MAJALAH");
        request.getRequestDispatcher("form-koleksi.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String id = request.getParameter("id");
        String tipe = request.getParameter("tipe");
        
        if ("BUKU".equalsIgnoreCase(tipe)) {
            Buku buku = manager.getBukuById(id);
            request.setAttribute("dokumen", buku);
            request.setAttribute("tipeForm", "BUKU");
        } else {
            Majalah majalah = manager.getMajalahById(id);
            request.setAttribute("dokumen", majalah);
            request.setAttribute("tipeForm", "MAJALAH");
        }
        
        request.getRequestDispatcher("form-koleksi.jsp").forward(request, response);
    }

    private void deleteKoleksi(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String id = request.getParameter("id");
        String tipe = request.getParameter("tipe");
        
        if ("BUKU".equalsIgnoreCase(tipe)) {
            manager.hapusBuku(id);
        } else {
            manager.hapusMajalah(id);
        }
        response.sendRedirect("KoleksiServlet");
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
        String tipe = request.getParameter("tipeForm");
        if (tipe == null) {
            tipe = request.getParameter("tipe");
        }
        
        String idDokumen = request.getParameter("idDokumen");
        String judul = request.getParameter("judul");
        String pengarang = request.getParameter("pengarang");
        
        String tahunRaw = request.getParameter("tahunTerbit");
        if (tahunRaw == null) {
            tahunRaw = request.getParameter("tahun");
        }
        int tahunTerbit = Integer.parseInt(tahunRaw);
        
        if ("BUKU".equalsIgnoreCase(tipe)) {
            String isbn = request.getParameter("isbn");
            String penerbit = request.getParameter("penerbit");
            
            String halamanRaw = request.getParameter("jumlahHalaman");
            if (halamanRaw == null) {
                halamanRaw = request.getParameter("halaman");
            }
            int jumlahHalaman = Integer.parseInt(halamanRaw);
            
            int stok = Integer.parseInt(request.getParameter("stok"));
            String lokasiRak = request.getParameter("lokasiRak");
            
            Buku buku = new Buku(idDokumen, judul, pengarang, tahunTerbit, isbn, penerbit, jumlahHalaman, stok, lokasiRak);
            if ("insert".equals(action)) {
                manager.tambahBuku(buku);
            } else if ("update".equals(action)) {
                manager.updateBuku(buku);
            }
        } else if ("MAJALAH".equalsIgnoreCase(tipe)) {
            String edisi = request.getParameter("edisi");
            String bulanTerbit = request.getParameter("bulanTerbit");
            
            String frekuensiTerbit = request.getParameter("frekuensiTerbit");
            if (frekuensiTerbit == null) {
                frekuensiTerbit = request.getParameter("frekuensi");
            }
            
            String lokasiRak = request.getParameter("lokasiRak");
            if (lokasiRak == null || lokasiRak.trim().isEmpty()) {
                lokasiRak = request.getParameter("lokasiRakMajalah");
            }
            
            Majalah majalah = new Majalah(idDokumen, judul, pengarang, tahunTerbit, edisi, bulanTerbit, frekuensiTerbit, lokasiRak);
            if ("insert".equals(action)) {
                manager.tambahMajalah(majalah);
            } else if ("update".equals(action)) {
                manager.updateMajalah(majalah);
            }
        }
        
        response.sendRedirect("KoleksiServlet");
    }
}

package servlet;

import controller.CatalogSearcher;
import controller.ManagerPencarian;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.HasilPencarian;
import util.DBConnection;

@WebServlet(name = "PencarianServlet", urlPatterns = {"/PencarianServlet"})
public class PencarianServlet extends HttpServlet {

    private ManagerPencarian managerPencarian;
    private Connection conn;

    @Override
    public void init() throws ServletException {
        try {
            conn = DBConnection.getConnection();
            CatalogSearcher searcher = new CatalogSearcher(conn);
            managerPencarian = new ManagerPencarian(searcher);
        } catch (SQLException e) {
            throw new ServletException("Koneksi DB gagal", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        javax.servlet.http.HttpSession session = request.getSession();
        if (session.getAttribute("userRole") == null) {
            response.sendRedirect("index.jsp");
            return;
        }

        String keyword = request.getParameter("keyword");
        if (keyword == null) {
            keyword = "";
        }
        
        HasilPencarian hasil = managerPencarian.cari(keyword);
        request.setAttribute("hasilPencarian", hasil);
        
        request.getRequestDispatcher("pencarian.jsp").forward(request, response);
    }
}

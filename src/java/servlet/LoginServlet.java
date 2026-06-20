package servlet;

import controller.ManagerAnggota;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.AnggotaAktif;
import util.DBConnection;

@WebServlet(name = "LoginServlet", urlPatterns = {"/LoginServlet"})
public class LoginServlet extends HttpServlet {

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
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        
        if ("login".equals(action)) {
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            
            // Login Admin
            if ("admin".equals(username) && "12345".equals(password)) {
                HttpSession session = request.getSession();
                session.setAttribute("userRole", "ADMIN");
                session.setAttribute("userName", "Administrator");
                response.sendRedirect("dashboard_admin.jsp");
                return;
            }
            
            // Login Member
            if (username != null && username.equals(password)) {
                AnggotaAktif anggota = managerAnggota.getAnggotaAktifById(username);
                if (anggota != null) {
                    HttpSession session = request.getSession();
                    session.setAttribute("userRole", "MEMBER");
                    session.setAttribute("userName", anggota.getNama());
                    session.setAttribute("userId", anggota.getIdAnggota());
                    response.sendRedirect("dashboard_member.jsp");
                    return;
                }
            }
            
            // Jika login gagal
            request.setAttribute("errorMessage", "Username atau Password salah, atau Anda bukan anggota aktif.");
            request.getRequestDispatcher("index.jsp").forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        
        if ("logout".equals(action)) {
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate();
            }
            response.sendRedirect("index.jsp");
        } else {
            response.sendRedirect("index.jsp");
        }
    }
}

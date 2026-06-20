package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Singleton koneksi JDBC ke database perpustakaan_db.
 * Sebelum digunakan: tambahkan mysql-connector-j.jar ke Libraries project NetBeans.
 * @author Farel
 */
public class DBConnection {

    private static final String DB_URL  = "jdbc:mysql://localhost:3306/tubesperpustakaan?useSSL=false&serverTimezone=Asia/Jakarta";
    private static final String DB_USER = "root";
    private static final String DB_PASS = ""; // sesuaikan password MySQL
    private static final String DRIVER  = "com.mysql.cj.jdbc.Driver";

    private static Connection connection = null;

    private DBConnection() {}

    /** Ambil koneksi aktif. Buat baru jika belum ada / sudah tutup. */
    public static Connection getConnection() throws SQLException {
        try {
            Class.forName(DRIVER);
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
            }
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver MySQL tidak ditemukan! Tambahkan mysql-connector-j.jar ke Libraries NetBeans. " + e.getMessage());
        }
        return connection;
    }

    /** Tutup koneksi jika masih aktif. */
    public static void closeConnection() {
        if (connection != null) {
            try {
                if (!connection.isClosed()) connection.close();
            } catch (SQLException e) {
                System.err.println("[DBConnection] Gagal tutup koneksi: " + e.getMessage());
            } finally {
                connection = null;
            }
        }
    }
}

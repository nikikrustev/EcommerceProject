import java.io.InputStream;
import java.sql.*;
import org.mindrot.jbcrypt.BCrypt;

import javax.swing.*;


public class Final {
    private static final String URL = "jdbc:mysql://localhost:3306/final1";
    private static final String USER = "root";
    private static final String PASSWORD = "password";
    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static boolean loginUser(String email, String password) {
        String query = "SELECT password_hash FROM users WHERE email = ?";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String storedHash = rs.getString("password_hash");
                return BCrypt.checkpw(password, storedHash); // ✅ Compare hashed
            } else {
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean registerUser(String full_name, String email, String password) {
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt()); // 🔐 Hashing here
        String query = "INSERT INTO users (full_name, email, password_hash) VALUES (?, ?, ?)";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, full_name);
            pstmt.setString(2, email);
            pstmt.setString(3, hashedPassword);
            pstmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            if (e.getMessage().contains("Duplicate")) {
                System.out.println("Email or name already exists.");
            } else {
                e.printStackTrace();
            }
            return false;
        }
    }

}
class Product{
    int id;
    String name;
    InputStream image;
    double price;


    public Product(int id, String name, double price, InputStream image) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.image = image;
    }

    public int getId() {
        return id;
    };

    public double getPrice() {
        return price;
    }

    public InputStream getImage() {
        return image;
    }

    public String getName() {
        return name;
    }
}
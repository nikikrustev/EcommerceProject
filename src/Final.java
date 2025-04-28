import java.io.InputStream;
import java.sql.*;
import org.mindrot.jbcrypt.BCrypt;
import java.util.ArrayList;

public class Final {
    private static final String URL = "jdbc:mysql://localhost:3306/final1";
    private static final String USER = "root";
    private static final String PASSWORD = "password";
    public static String loggedInUserEmail;

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
                if (BCrypt.checkpw(password, storedHash)) {
                    Final.loggedInUserEmail = email;
                    return true;
                } else {
                    return false;
                }
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
    public static void clearCart() {
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM cart_items")) {
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Object[]> getCartItems() {
        ArrayList<Object[]> cartItems = new ArrayList<>();
        String query = "SELECT p.name, c.quantity, (p.price * c.quantity) AS total_price " +
                "FROM cart_items c " +
                "JOIN products p ON c.product_id = p.product_id";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String name = rs.getString("name");
                int quantity = rs.getInt("quantity");
                double totalPrice = rs.getDouble("total_price");

                cartItems.add(new Object[]{name, quantity, totalPrice});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cartItems;
    }
    public static void updateCartItemQuantity(String productName, int quantity) {
        String query = "UPDATE cart_items SET quantity = ? WHERE product_id = (SELECT product_id FROM products WHERE name = ?)";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, quantity);
            pstmt.setString(2, productName);
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static double getPriceForProduct(String productName) {
        String query = "SELECT price FROM products WHERE name = ?";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, productName);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("price");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    public static void addToCart(int productId) {
        try (Connection conn = connect()) {
            String checkQuery = "SELECT quantity FROM cart_items WHERE product_id = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
            checkStmt.setInt(1, productId);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                int currentQuantity = rs.getInt("quantity");
                String updateQuery = "UPDATE cart_items SET quantity = ? WHERE product_id = ?";
                PreparedStatement updateStmt = conn.prepareStatement(updateQuery);
                updateStmt.setInt(1, currentQuantity + 1);
                updateStmt.setInt(2, productId);
                updateStmt.executeUpdate();
            } else {
                String insertQuery = "INSERT INTO cart_items (product_id, quantity) VALUES (?, ?)";
                PreparedStatement insertStmt = conn.prepareStatement(insertQuery);
                insertStmt.setInt(1, productId);
                insertStmt.setInt(2, 1);
                insertStmt.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void removeFromCart(String productName) {
        String query = "DELETE FROM cart_items WHERE product_id = (SELECT product_id FROM products WHERE name = ?)";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, productName);
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static int getUserIdByEmail(String email) {
        String query = "SELECT user_id FROM users WHERE email = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("user_id");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }
    public static int insertOrder(int userId, String address, double totalPrice) {
        String query = "INSERT INTO orders (user_id, shipping_address, total_price) VALUES (?, ?, ?)";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, userId);
            pstmt.setString(2, address);
            pstmt.setDouble(3, totalPrice);
            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }
    public static void insertOrderItem(int orderId, int productId, int quantity, double priceAtPurchase) {
        String query = "INSERT INTO order_items (order_id, product_id, quantity, price_at_purchase) VALUES (?, ?, ?, ?)";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, orderId);
            pstmt.setInt(2, productId);
            pstmt.setInt(3, quantity);
            pstmt.setDouble(4, priceAtPurchase);
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static int getProductIdByName(String name) {
        String query = "SELECT product_id FROM products WHERE name = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, name);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("product_id");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }
    public static boolean isCartEmpty() {
        String query = "SELECT COUNT(*) AS total FROM cart_items";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            if (rs.next()) {
                int total = rs.getInt("total");
                return total == 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

}


class Product{
    int id;
    String name;
    byte[] imageData;
    double price;

    public Product(int id, String name, double price, byte[] imageData) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageData = imageData;
    }

    public int getId() {
        return id;
    };

    public double getPrice() {
        return price;
    }

    public byte[] getImageData() {
        return imageData;
    }

    public String getName() {
        return name;
    }

    public static Product loadById(int id) {
        try (Connection conn = Final.connect();
             PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM products WHERE product_id = ?")) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                InputStream imageStream = rs.getBinaryStream("image");
                byte[] imageBytes = null;

                if (imageStream != null) {
                    imageBytes = imageStream.readAllBytes();
                }

                return new Product(
                        rs.getInt("product_id"),
                        rs.getString("name"),
                        rs.getDouble("price"),
                        imageBytes
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
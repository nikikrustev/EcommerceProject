import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Last extends JFrame {
    private JPanel panel1;
    private JTextField textField1;
    private JTable table1;
    private JButton placeOrderButton;
    private JLabel price;

    public Last() {
        setTitle("Login Page");
        setContentPane(panel1);
        setSize(700, 330);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

        loadCartItems();
        double total = 0.0;
        DefaultTableModel model = (DefaultTableModel) table1.getModel();
        for (int i = 0; i < model.getRowCount(); i++) {
            total += Double.parseDouble(model.getValueAt(i, 2).toString());
        }
        price.setText(String.format("Total: %.2f $", total));

        placeOrderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String address = textField1.getText();

                // Calculate total price
                double total = 0.0;
                DefaultTableModel model = (DefaultTableModel) table1.getModel();
                for (int i = 0; i < model.getRowCount(); i++) {
                    total += Double.parseDouble(model.getValueAt(i, 2).toString());
                }

                // Get user_id
                int userId = Final.getUserIdByEmail(Final.loggedInUserEmail);

                // Insert into orders table
                int orderId = Final.insertOrder(userId, address, total);

                if (orderId != -1) {
                    // Insert each item into order_items
                    for (int i = 0; i < model.getRowCount(); i++) {
                        String productName = (String) model.getValueAt(i, 0);
                        int quantity = Integer.parseInt(model.getValueAt(i, 1).toString());
                        double pricePerProduct = Final.getPriceForProduct(productName);
                        int productId = Final.getProductIdByName(productName);

                        Final.insertOrderItem(orderId, productId, quantity, pricePerProduct);
                    }

                    // Clear the cart
                    Final.clearCart();

                    // Success popup
                    JOptionPane.showMessageDialog(null, "Thank you so much for buying a shirt from us. You placed a successful order for address: " + address);
                    System.exit(0);
                } else {
                    JOptionPane.showMessageDialog(null, "Error placing order!");
                }
            }
        });
    }

    private void loadCartItems() {
        String[] columnNames = {"Product Name", "Quantity", "Total Price"};

        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        ArrayList<Object[]> cartItems = Final.getCartItems();
        for (Object[] item : cartItems) {
            model.addRow(item);
        }

        table1.setModel(model);
    }
}
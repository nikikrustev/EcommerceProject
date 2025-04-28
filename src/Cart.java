import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.table.TableRowSorter;
import javax.swing.RowFilter;


public class Cart extends JFrame {
    private JPanel panel1;
    private JTable table1;
    private JButton proceedToCheckoutButton;
    private JButton removeProductButton;
    private JButton backToTheShopButton;
    private JComboBox comboBox1;
    private JTextField textField1;
    private JLabel price;

    public Cart() {
        setTitle("Cart");
        setContentPane(panel1);
        setSize(800, 640);
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
        comboBox1.addActionListener(e -> applyFilter());

        textField1.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent e) {
                searchFilter();
            }
        });
        backToTheShopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                dispose();
                new Shop();
            }
        });
        proceedToCheckoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (Final.isCartEmpty()) {
                    JOptionPane.showMessageDialog(null, "Your cart is empty! Add some products first.");
                } else {
                    dispose();
                    new Last();
                }
            }
        });
        removeProductButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table1.getSelectedRow();

                if (selectedRow != -1) {
                    DefaultTableModel model = (DefaultTableModel) table1.getModel();
                    String productName = (String) model.getValueAt(selectedRow, 0);

                    Final.removeFromCart(productName);
                    model.removeRow(selectedRow);

                    double total = 0.0;
                    for (int i = 0; i < model.getRowCount(); i++) {
                        total += Double.parseDouble(model.getValueAt(i, 2).toString());
                    }
                    price.setText(String.format("Total: %.2f $", total));
                } else {
                    JOptionPane.showMessageDialog(null, "Please select a product to remove.");
                }
            }
        });
    }

        private void loadCartItems() {
        String[] columnNames = {"Product Name", "Quantity", "Total Price"};

        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 1;
            }
        };

        ArrayList<Object[]> cartItems = Final.getCartItems();
        for (Object[] item : cartItems) {
            model.addRow(item);
        }

        table1.setModel(model);

        model.addTableModelListener(e -> {
            if (e.getType() == javax.swing.event.TableModelEvent.UPDATE) {
                int row = e.getFirstRow();
                int column = e.getColumn();
                if (column == 1) {
                    updateQuantityInDatabase(row);
                }
            }
        });
    }
    private void updateQuantityInDatabase(int row) {
        DefaultTableModel model = (DefaultTableModel) table1.getModel();
        String productName = (String) model.getValueAt(row, 0);
        int newQuantity = Integer.parseInt(model.getValueAt(row, 1).toString());

        Final.updateCartItemQuantity(productName, newQuantity);

        double pricePerItem = Final.getPriceForProduct(productName);
        double newTotalPrice = pricePerItem * newQuantity;
        model.setValueAt(newTotalPrice, row, 2);

        double total = 0.0;
        for (int i = 0; i < model.getRowCount(); i++) {
            total += Double.parseDouble(model.getValueAt(i, 2).toString());
        }
        price.setText(String.format("Total: %.2f $", total));
    }
    private void applyFilter() {
        String selected = (String) comboBox1.getSelectedItem();
        DefaultTableModel model = (DefaultTableModel) table1.getModel();
        ArrayList<Object[]> rows = new ArrayList<>();

        for (int i = 0; i < model.getRowCount(); i++) {
            String name = (String) model.getValueAt(i, 0);
            int quantity = Integer.parseInt(model.getValueAt(i, 1).toString());
            double totalPrice = Double.parseDouble(model.getValueAt(i, 2).toString());
            rows.add(new Object[]{name, quantity, totalPrice});
        }
        rows.sort((o1, o2) -> {
            if (selected.equals("Price High to Low")) {
                return Double.compare((double) o2[2], (double) o1[2]); // Total price
            } else if (selected.equals("Price Low to High")) {
                return Double.compare((double) o1[2], (double) o2[2]);
            } else if (selected.equals("Quantity High to Low")) {
                return Integer.compare((int) o2[1], (int) o1[1]); // Quantity
            } else if (selected.equals("Quantity Low to High")) {
                return Integer.compare((int) o1[1], (int) o2[1]);
            }
            return 0;
        });

        model.setRowCount(0);

        for (Object[] row : rows) {
            model.addRow(row);
        }
    }

    private void searchFilter() {
        String searchText = textField1.getText().toLowerCase();
        DefaultTableModel model = (DefaultTableModel) table1.getModel();
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        table1.setRowSorter(sorter);

        if (searchText.trim().length() == 0) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + searchText, 0));
        }
    }
}

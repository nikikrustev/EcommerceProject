import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Shop extends JFrame {
    private JButton addToCartButton1;
    private JButton addToCartButton2;
    private JButton addToCartButton4;
    private JButton addToCartButton3;
    private JButton goToCartButton;
    private JLabel price1;
    private JPanel panel1;
    private JLabel price2;
    private JLabel price3;
    private JLabel price4;
    private JLabel name1;
    private JLabel name2;
    private JLabel name3;
    private JLabel name4;
    private JLabel pic1;
    private JLabel pic2;
    private JLabel pic3;
    private JLabel pic4;

    public Shop() {
        setTitle("Shop");
        setContentPane(panel1);
        setSize(800, 640);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

        Product p1 = Product.loadById(2);
        Product p2 = Product.loadById(3);
        Product p3 = Product.loadById(4);
        Product p4 = Product.loadById(5);

        name1.setText(p1.getName());
        price1.setText(p1.getPrice() + " $");
        if (p1.getImageData() != null) {
            pic1.setIcon(new ImageIcon(p1.getImageData()));
        }

        name2.setText(p2.getName());
        price2.setText(p2.getPrice() + " $");
        if (p2.getImageData() != null) {
            pic2.setIcon(new ImageIcon(p2.getImageData()));
        }

        name3.setText(p3.getName());
        price3.setText(p3.getPrice() + " $");
        if (p3.getImageData() != null) {
            pic3.setIcon(new ImageIcon(p3.getImageData()));
        }

        name4.setText(p4.getName());
        price4.setText(p4.getPrice() + " $");
        if (p4.getImageData() != null) {
            pic4.setIcon(new ImageIcon(p4.getImageData()));
        }


        addToCartButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Final.addToCart(p1.getId());
            }
        });


        addToCartButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Final.addToCart(p2.getId());
            }
        });
        addToCartButton3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Final.addToCart(p3.getId());
            }
        });
        addToCartButton4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Final.addToCart(p4.getId());
            }
        });
        goToCartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (Final.isCartEmpty()) {
                    JOptionPane.showMessageDialog(null, "Your cart is empty! Add some products first.");
                } else {
                    dispose();
                    new Cart();
                }
            }
        });

    }
}

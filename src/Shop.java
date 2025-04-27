import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Shop extends JFrame {
    private JButton addToCartButton;
    private JButton addToCartButton1;
    private JButton addToCartButton2;
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
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

        //Product p1 = new
        //  Product(1);

       //price1.setText(String.valueOf(p1.getPrice()));

        addToCartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

            }
        });
    }
}

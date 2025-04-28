import javax.swing.*;
import java.awt.event.*;

public class login extends JFrame {
    private JPanel panel1;
    private JTextField textField1; // email
    private JTextField textField2; // password
    private JButton logInButton;
    private JButton signUpButton;

    public login() {
        setTitle("Login Page");
        setContentPane(panel1);
        setSize(400, 330);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

        logInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String email = textField1.getText().trim();
                String password = textField2.getText().trim();

                if (email.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please enter email and password.");
                    return;
                }

                boolean success = Final.loginUser(email, password);
                if (success) {
                    JOptionPane.showMessageDialog(null, "Login successful!");
                    dispose();
                    new Shop();
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid login. Try again.");
                }
            }
        });

        signUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                dispose();
                new signup();
            }
        });
    }
}

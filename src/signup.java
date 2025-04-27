import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class signup extends JFrame {
    private JPanel panel1;
    private JTextField textField1; // Username
    private JTextField textField2; // Email
    private JTextField textField3; // Password
    private JButton signUpButton;
    private JButton clickHereToLogButton;

    public signup() {
        setTitle("Sign Up Page");
        setContentPane(panel1);
        setSize(400, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

        signUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String username = textField1.getText().trim();
                String email = textField2.getText().trim();
                String password = textField3.getText().trim();

                if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please fill in all fields.");
                    return;
                }

                boolean registered = Final.registerUser(username, email, password);
                if (registered) {
                    JOptionPane.showMessageDialog(null, "Account created successfully!");
                    dispose();
                    new login();
                } else {
                    JOptionPane.showMessageDialog(null, "Failed to create account. Try a different email or username.");
                }
            }
        });

        clickHereToLogButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                dispose(); // Close signup window
                new login(); // Open login window
            }
        });
    }
}

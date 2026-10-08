import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class LoginGUI extends JFrame {

    static String url = "jdbc:mysql://localhost:3306/vehicle_db";
    static String username = "root";
    static String password = "5127";

    JTextField usernameField;
    JPasswordField passwordField;

    public LoginGUI() {

        setTitle("Vehicle Management System - Login");
        setSize(450, 330);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(
                BorderFactory.createEmptyBorder(30, 40, 30, 40));

        // Title
        JLabel title = new JLabel(
                "VEHICLE MANAGEMENT SYSTEM",
                JLabel.CENTER);

        title.setFont(
                new Font("Arial", Font.BOLD, 20));

        mainPanel.add(title, BorderLayout.NORTH);

        // Login fields
        JPanel formPanel = new JPanel(
                new GridLayout(2, 2, 10, 15));

        JLabel usernameLabel = new JLabel("Username:");
        JLabel passwordLabel = new JLabel("Password:");

        usernameLabel.setFont(
                new Font("Arial", Font.BOLD, 14));

        passwordLabel.setFont(
                new Font("Arial", Font.BOLD, 14));

        usernameField = new JTextField();

        passwordField = new JPasswordField();

        formPanel.add(usernameLabel);
        formPanel.add(usernameField);

        formPanel.add(passwordLabel);
        formPanel.add(passwordField);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Login button
        JButton loginButton = new JButton("LOGIN");

        loginButton.setFont(
                new Font("Arial", Font.BOLD, 14));

        JPanel buttonPanel = new JPanel();

        buttonPanel.add(loginButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // Login action
        loginButton.addActionListener(
                e -> login());

        passwordField.addActionListener(
                e -> login());
    }

    public void login() {

        String user = usernameField.getText();

        String pass = new String(
                passwordField.getPassword());

        if (user.isEmpty() || pass.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter username and password.");

            return;
        }

        String sql = "SELECT * FROM users " +
                "WHERE username = ? AND password = ?";

        try (
                Connection con = DriverManager.getConnection(
                        url,
                        username,
                        password);

                PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, user);
            pst.setString(2, pass);

            ResultSet rs = pst.executeQuery();

            if (rs.next()) {

                JOptionPane.showMessageDialog(
                        this,
                        "Login successful!");

                dispose();

                VehicleGUI gui = new VehicleGUI();

                gui.setVisible(true);

                gui.viewVehicles();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Invalid username or password.");
            }

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Database error: " +
                            e.getMessage());
        }
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            LoginGUI login = new LoginGUI();

            login.setVisible(true);
        });
    }
}
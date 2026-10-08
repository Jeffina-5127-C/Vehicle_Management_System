import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class VehicleGUI extends JFrame {

    static String url = "jdbc:mysql://localhost:3306/vehicle_db";
    static String username = "root";
    static String password = "5127";

    JTextField vehicleNumberField;
    JTextField ownerNameField;
    JTextField vehicleTypeField;
    JTextField modelField;
    JTextField yearField;
    JTextField phoneField;

    JTable table;
    DefaultTableModel tableModel;

    public VehicleGUI() {

        setTitle("Vehicle Management System");
        setSize(1050, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        createGUI();
    }

    public Connection connect() throws SQLException {
        return DriverManager.getConnection(
                url, username, password);
    }

    public void createGUI() {

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));

        mainPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        20, 20, 20, 20));

        // ---------------- TITLE ----------------

        JLabel title = new JLabel(
                "VEHICLE MANAGEMENT SYSTEM",
                JLabel.CENTER);

        title.setFont(
                new Font("Arial", Font.BOLD, 26));

        mainPanel.add(title, BorderLayout.NORTH);

        // ---------------- INPUT PANEL ----------------

        JPanel inputPanel = new JPanel(
                new GridLayout(6, 2, 10, 12));

        JLabel vehicleNumberLabel = new JLabel("Vehicle Number:");

        JLabel ownerNameLabel = new JLabel("Owner Name:");

        JLabel vehicleTypeLabel = new JLabel("Vehicle Type:");

        JLabel modelLabel = new JLabel("Model:");

        JLabel yearLabel = new JLabel("Year:");

        JLabel phoneLabel = new JLabel("Phone Number:");

        Font labelFont = new Font("Arial", Font.BOLD, 14);

        vehicleNumberLabel.setFont(labelFont);
        ownerNameLabel.setFont(labelFont);
        vehicleTypeLabel.setFont(labelFont);
        modelLabel.setFont(labelFont);
        yearLabel.setFont(labelFont);
        phoneLabel.setFont(labelFont);

        vehicleNumberField = new JTextField();
        ownerNameField = new JTextField();
        vehicleTypeField = new JTextField();
        modelField = new JTextField();
        yearField = new JTextField();
        phoneField = new JTextField();

        inputPanel.add(vehicleNumberLabel);
        inputPanel.add(vehicleNumberField);

        inputPanel.add(ownerNameLabel);
        inputPanel.add(ownerNameField);

        inputPanel.add(vehicleTypeLabel);
        inputPanel.add(vehicleTypeField);

        inputPanel.add(modelLabel);
        inputPanel.add(modelField);

        inputPanel.add(yearLabel);
        inputPanel.add(yearField);

        inputPanel.add(phoneLabel);
        inputPanel.add(phoneField);

        // ---------------- LEFT SIDE ----------------

        JPanel leftPanel = new JPanel(
                new BorderLayout(10, 10));

        JLabel detailsLabel = new JLabel("Vehicle Details");

        detailsLabel.setFont(
                new Font("Arial", Font.BOLD, 18));

        leftPanel.add(
                detailsLabel,
                BorderLayout.NORTH);

        leftPanel.add(
                inputPanel,
                BorderLayout.CENTER);

        mainPanel.add(
                leftPanel,
                BorderLayout.WEST);

        // ---------------- BUTTONS ----------------

        JPanel buttonPanel = new JPanel(
                new GridLayout(7, 1, 8, 8));

        JButton addButton = new JButton("Add Vehicle");

        JButton viewButton = new JButton("View Vehicles");

        JButton searchButton = new JButton("Search Vehicle");

        JButton updateButton = new JButton("Update Vehicle");

        JButton deleteButton = new JButton("Delete Vehicle");

        JButton clearButton = new JButton("Clear");

        JButton logoutButton = new JButton("Logout");

        buttonPanel.add(addButton);
        buttonPanel.add(viewButton);
        buttonPanel.add(searchButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(logoutButton);

        mainPanel.add(
                buttonPanel,
                BorderLayout.EAST);

        // ---------------- TABLE ----------------

        tableModel = new DefaultTableModel();

        tableModel.addColumn("ID");
        tableModel.addColumn("Vehicle Number");
        tableModel.addColumn("Owner");
        tableModel.addColumn("Type");
        tableModel.addColumn("Model");
        tableModel.addColumn("Year");
        tableModel.addColumn("Phone");

        table = new JTable(tableModel);

        table.setRowHeight(25);

        JScrollPane scrollPane = new JScrollPane(table);

        JPanel tablePanel = new JPanel(new BorderLayout(5, 5));

        JLabel tableLabel = new JLabel("Vehicle Records");

        tableLabel.setFont(
                new Font("Arial", Font.BOLD, 18));

        tablePanel.add(
                tableLabel,
                BorderLayout.NORTH);

        tablePanel.add(
                scrollPane,
                BorderLayout.CENTER);

        mainPanel.add(
                tablePanel,
                BorderLayout.CENTER);

        setContentPane(mainPanel);

        // ---------------- BUTTON ACTIONS ----------------

        addButton.addActionListener(
                e -> addVehicle());

        viewButton.addActionListener(
                e -> viewVehicles());

        searchButton.addActionListener(
                e -> searchVehicle());

        updateButton.addActionListener(
                e -> updateVehicle());

        deleteButton.addActionListener(
                e -> deleteVehicle());

        clearButton.addActionListener(
                e -> clearFields());

        logoutButton.addActionListener(
                e -> logout());

        // ---------------- TABLE SELECTION ----------------

        table.getSelectionModel()
                .addListSelectionListener(e -> {

                    if (!e.getValueIsAdjusting()
                            && table.getSelectedRow() != -1) {

                        int row = table.getSelectedRow();

                        vehicleNumberField.setText(
                                table.getValueAt(row, 1)
                                        .toString());

                        ownerNameField.setText(
                                table.getValueAt(row, 2)
                                        .toString());

                        vehicleTypeField.setText(
                                table.getValueAt(row, 3)
                                        .toString());

                        modelField.setText(
                                table.getValueAt(row, 4)
                                        .toString());

                        yearField.setText(
                                table.getValueAt(row, 5)
                                        .toString());

                        phoneField.setText(
                                table.getValueAt(row, 6)
                                        .toString());
                    }
                });
    }

    // ---------------- ADD VEHICLE ----------------

    public void addVehicle() {

        String vehicleNumber = vehicleNumberField.getText();

        String ownerName = ownerNameField.getText();

        String vehicleType = vehicleTypeField.getText();

        String model = modelField.getText();

        String year = yearField.getText();

        String phone = phoneField.getText();

        if (vehicleNumber.isEmpty()
                || ownerName.isEmpty()
                || vehicleType.isEmpty()
                || model.isEmpty()
                || year.isEmpty()
                || phone.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please fill all fields.");

            return;
        }

        String sql = "INSERT INTO vehicles " +
                "(vehicle_number, owner_name, " +
                "vehicle_type, model, year, phone) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (
                Connection con = connect();
                PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, vehicleNumber);
            pst.setString(2, ownerName);
            pst.setString(3, vehicleType);
            pst.setString(4, model);
            pst.setInt(5, Integer.parseInt(year));
            pst.setString(6, phone);

            pst.executeUpdate();

            JOptionPane.showMessageDialog(
                    this,
                    "Vehicle added successfully!");

            clearFields();
            viewVehicles();

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Year must be a number.");

        } catch (SQLIntegrityConstraintViolationException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Vehicle number already exists.");

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Database error: " + e.getMessage());
        }
    }

    // ---------------- VIEW VEHICLES ----------------

    public void viewVehicles() {

        tableModel.setRowCount(0);

        String sql = "SELECT * FROM vehicles";

        try (
                Connection con = connect();
                PreparedStatement pst = con.prepareStatement(sql);
                ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {

                tableModel.addRow(
                        new Object[] {
                                rs.getInt("id"),
                                rs.getString("vehicle_number"),
                                rs.getString("owner_name"),
                                rs.getString("vehicle_type"),
                                rs.getString("model"),
                                rs.getInt("year"),
                                rs.getString("phone")
                        });
            }

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Database error: " + e.getMessage());
        }
    }

    // ---------------- SEARCH VEHICLE ----------------

    public void searchVehicle() {

        String vehicleNumber = JOptionPane.showInputDialog(
                this,
                "Enter Vehicle Number:");

        if (vehicleNumber == null
                || vehicleNumber.trim().isEmpty()) {

            return;
        }

        tableModel.setRowCount(0);

        String sql = "SELECT * FROM vehicles " +
                "WHERE vehicle_number = ?";

        try (
                Connection con = connect();
                PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, vehicleNumber);

            ResultSet rs = pst.executeQuery();

            if (rs.next()) {

                tableModel.addRow(
                        new Object[] {
                                rs.getInt("id"),
                                rs.getString("vehicle_number"),
                                rs.getString("owner_name"),
                                rs.getString("vehicle_type"),
                                rs.getString("model"),
                                rs.getInt("year"),
                                rs.getString("phone")
                        });

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Vehicle not found.");
            }

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Database error: " + e.getMessage());
        }
    }

    // ---------------- UPDATE VEHICLE ----------------

    public void updateVehicle() {

        String vehicleNumber = vehicleNumberField.getText();

        if (vehicleNumber.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Select a vehicle first.");

            return;
        }

        String sql = "UPDATE vehicles SET " +
                "owner_name=?, " +
                "vehicle_type=?, " +
                "model=?, " +
                "year=?, " +
                "phone=? " +
                "WHERE vehicle_number=?";

        try (
                Connection con = connect();
                PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(
                    1,
                    ownerNameField.getText());

            pst.setString(
                    2,
                    vehicleTypeField.getText());

            pst.setString(
                    3,
                    modelField.getText());

            pst.setInt(
                    4,
                    Integer.parseInt(
                            yearField.getText()));

            pst.setString(
                    5,
                    phoneField.getText());

            pst.setString(
                    6,
                    vehicleNumber);

            int result = pst.executeUpdate();

            if (result > 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "Vehicle updated successfully!");

                clearFields();
                viewVehicles();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Vehicle not found.");
            }

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Year must be a number.");

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Database error: " + e.getMessage());
        }
    }

    // ---------------- DELETE VEHICLE ----------------

    public void deleteVehicle() {

        String vehicleNumber = vehicleNumberField.getText();

        if (vehicleNumber.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Select a vehicle first.");

            return;
        }

        int choice = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete this vehicle?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION);

        if (choice != JOptionPane.YES_OPTION) {
            return;
        }

        String sql = "DELETE FROM vehicles " +
                "WHERE vehicle_number=?";

        try (
                Connection con = connect();
                PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(
                    1,
                    vehicleNumber);

            int result = pst.executeUpdate();

            if (result > 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "Vehicle deleted successfully!");

                clearFields();
                viewVehicles();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Vehicle not found.");
            }

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Database error: " + e.getMessage());
        }
    }

    // ---------------- CLEAR ----------------

    public void clearFields() {

        vehicleNumberField.setText("");
        ownerNameField.setText("");
        vehicleTypeField.setText("");
        modelField.setText("");
        yearField.setText("");
        phoneField.setText("");

        table.clearSelection();

        viewVehicles();
    }

    // ---------------- LOGOUT ----------------

    public void logout() {

        int choice = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to logout?",
                "Logout",
                JOptionPane.YES_NO_OPTION);

        if (choice == JOptionPane.YES_OPTION) {

            dispose();

            LoginGUI login = new LoginGUI();

            login.setVisible(true);
        }
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            VehicleGUI gui = new VehicleGUI();

            gui.setVisible(true);

            gui.viewVehicles();
        });
    }
}
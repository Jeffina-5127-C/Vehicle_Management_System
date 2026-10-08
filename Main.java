import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Scanner;

public class Main {

    // MySQL connection details
    static String url = "jdbc:mysql://localhost:3306/vehicle_db";
    static String username = "root";
    static String password = "5127";

    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {

        try {
            // Connect to MySQL
            Connection con = DriverManager.getConnection(
                    url, username, password);

            System.out.println("======================================");
            System.out.println("      VEHICLE MANAGEMENT SYSTEM");
            System.out.println("======================================");

            // Login
            if (login(con)) {
                mainMenu(con);
            }

            con.close();

        } catch (Exception e) {
            System.out.println("Database connection failed!");
            e.printStackTrace();
        }
    }

    // ================= LOGIN =================

    static boolean login(Connection con) {

        System.out.println("\n---------- LOGIN ----------");

        System.out.print("Username: ");
        String user = sc.nextLine();

        System.out.print("Password: ");
        String pass = sc.nextLine();

        try {
            String sql = "SELECT * FROM users WHERE username = ? AND password = ?";

            PreparedStatement pst = con.prepareStatement(sql);

            pst.setString(1, user);
            pst.setString(2, pass);

            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                System.out.println("\nLogin successful!");
                System.out.println("Welcome, " + user + "!");
                return true;
            } else {
                System.out.println("\nInvalid username or password!");
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // ================= MAIN MENU =================

    static void mainMenu(Connection con) {

        while (true) {

            System.out.println("\n======================================");
            System.out.println("        VEHICLE MANAGEMENT");
            System.out.println("======================================");
            System.out.println("1. Add Vehicle");
            System.out.println("2. View All Vehicles");
            System.out.println("3. Search Vehicle");
            System.out.println("4. Update Vehicle");
            System.out.println("5. Delete Vehicle");
            System.out.println("6. Logout");
            System.out.println("======================================");

            System.out.print("Enter your choice: ");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {

                case 1:
                    addVehicle(con);
                    break;

                case 2:
                    viewVehicles(con);
                    break;

                case 3:
                    searchVehicle(con);
                    break;

                case 4:
                    updateVehicle(con);
                    break;

                case 5:
                    deleteVehicle(con);
                    break;

                case 6:
                    System.out.println("\nLogged out successfully.");
                    return;

                default:
                    System.out.println("\nInvalid choice!");
            }
        }
    }

    // ================= ADD VEHICLE =================

    static void addVehicle(Connection con) {

        try {

            System.out.println("\n---------- ADD VEHICLE ----------");

            System.out.print("Enter Vehicle Number: ");
            String vehicleNumber = sc.nextLine();

            System.out.print("Enter Owner Name: ");
            String ownerName = sc.nextLine();

            System.out.print("Enter Vehicle Type: ");
            String vehicleType = sc.nextLine();

            System.out.print("Enter Model: ");
            String model = sc.nextLine();

            System.out.print("Enter Year: ");
            int year = sc.nextInt();
            sc.nextLine();

            System.out.print("Enter Phone Number: ");
            String phone = sc.nextLine();

            String sql = "INSERT INTO vehicles " +
                    "(vehicle_number, owner_name, vehicle_type, model, year, phone) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";

            PreparedStatement pst = con.prepareStatement(sql);

            pst.setString(1, vehicleNumber);
            pst.setString(2, ownerName);
            pst.setString(3, vehicleType);
            pst.setString(4, model);
            pst.setInt(5, year);
            pst.setString(6, phone);

            pst.executeUpdate();

            System.out.println("\nVehicle added successfully!");

        } catch (SQLIntegrityConstraintViolationException e) {

            System.out.println("\nVehicle number already exists!");

        } catch (Exception e) {

            System.out.println("\nError adding vehicle.");
            e.printStackTrace();
        }
    }

    // ================= VIEW VEHICLES =================

    static void viewVehicles(Connection con) {

        try {

            System.out.println("\n---------- ALL VEHICLES ----------");

            String sql = "SELECT * FROM vehicles";

            PreparedStatement pst = con.prepareStatement(sql);

            ResultSet rs = pst.executeQuery();

            boolean found = false;

            while (rs.next()) {

                found = true;

                System.out.println("--------------------------------------");
                System.out.println("ID: " + rs.getInt("id"));
                System.out.println("Vehicle Number: "
                        + rs.getString("vehicle_number"));
                System.out.println("Owner Name: "
                        + rs.getString("owner_name"));
                System.out.println("Vehicle Type: "
                        + rs.getString("vehicle_type"));
                System.out.println("Model: "
                        + rs.getString("model"));
                System.out.println("Year: "
                        + rs.getInt("year"));
                System.out.println("Phone: "
                        + rs.getString("phone"));
            }

            if (!found) {
                System.out.println("No vehicles found.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= SEARCH VEHICLE =================

    static void searchVehicle(Connection con) {

        try {

            System.out.println("\n---------- SEARCH VEHICLE ----------");

            System.out.print("Enter Vehicle Number: ");
            String vehicleNumber = sc.nextLine();

            String sql = "SELECT * FROM vehicles WHERE vehicle_number = ?";

            PreparedStatement pst = con.prepareStatement(sql);

            pst.setString(1, vehicleNumber);

            ResultSet rs = pst.executeQuery();

            if (rs.next()) {

                System.out.println("\nVehicle Found!");
                System.out.println("--------------------------------------");
                System.out.println("ID: " + rs.getInt("id"));
                System.out.println("Vehicle Number: "
                        + rs.getString("vehicle_number"));
                System.out.println("Owner Name: "
                        + rs.getString("owner_name"));
                System.out.println("Vehicle Type: "
                        + rs.getString("vehicle_type"));
                System.out.println("Model: "
                        + rs.getString("model"));
                System.out.println("Year: "
                        + rs.getInt("year"));
                System.out.println("Phone: "
                        + rs.getString("phone"));

            } else {

                System.out.println("\nVehicle not found.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= UPDATE VEHICLE =================

    static void updateVehicle(Connection con) {

        try {

            System.out.println("\n---------- UPDATE VEHICLE ----------");

            System.out.print("Enter Vehicle Number to update: ");
            String vehicleNumber = sc.nextLine();

            String checkSql = "SELECT * FROM vehicles WHERE vehicle_number = ?";

            PreparedStatement checkStmt = con.prepareStatement(checkSql);

            checkStmt.setString(1, vehicleNumber);

            ResultSet rs = checkStmt.executeQuery();

            if (!rs.next()) {

                System.out.println("\nVehicle not found.");
                return;
            }

            System.out.print("Enter New Owner Name: ");
            String ownerName = sc.nextLine();

            System.out.print("Enter New Vehicle Type: ");
            String vehicleType = sc.nextLine();

            System.out.print("Enter New Model: ");
            String model = sc.nextLine();

            System.out.print("Enter New Year: ");
            int year = sc.nextInt();
            sc.nextLine();

            System.out.print("Enter New Phone Number: ");
            String phone = sc.nextLine();

            String sql = "UPDATE vehicles SET " +
                    "owner_name = ?, " +
                    "vehicle_type = ?, " +
                    "model = ?, " +
                    "year = ?, " +
                    "phone = ? " +
                    "WHERE vehicle_number = ?";

            PreparedStatement pst = con.prepareStatement(sql);

            pst.setString(1, ownerName);
            pst.setString(2, vehicleType);
            pst.setString(3, model);
            pst.setInt(4, year);
            pst.setString(5, phone);
            pst.setString(6, vehicleNumber);

            pst.executeUpdate();

            System.out.println("\nVehicle updated successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= DELETE VEHICLE =================

    static void deleteVehicle(Connection con) {

        try {

            System.out.println("\n---------- DELETE VEHICLE ----------");

            System.out.print("Enter Vehicle Number to delete: ");
            String vehicleNumber = sc.nextLine();

            String sql = "DELETE FROM vehicles WHERE vehicle_number = ?";

            PreparedStatement pst = con.prepareStatement(sql);

            pst.setString(1, vehicleNumber);

            int rows = pst.executeUpdate();

            if (rows > 0) {

                System.out.println("\nVehicle deleted successfully!");

            } else {

                System.out.println("\nVehicle not found.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
package DBlogic;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Car;



public class DBlogic {

    //Database connection details
    private final String DB_URL = "jdbc:mysql://localhost:3306/car_registry_system";
    private final String DB_USER = "root";
    private final String DB_PASS = "";

    private Connection connection;

    // try to connect&handle errors
    public Connection connect() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);

                System.out.println("Connected to DB");
            } catch (SQLException e) {
                System.err.println("Error while connecting to DB: " + e.getMessage());
                throw e;
            }
        }

        return connection;
    }

    //CREATE car
    public boolean insertCar(String brand, String model, Integer year, String registration_number) {

        String sql = "INSERT INTO cars (brand, model, year, registration_number) VALUES (?, ?, ?, ?)";

        try (Connection conn = connect();
             PreparedStatement pst = conn.prepareStatement(sql)
        ) {

            pst.setString(1, brand);
            pst.setString(2, model);
            pst.setInt(3, year);
            pst.setString(4, registration_number);

            //Check how many rows affected
            int affectedRows = pst.executeUpdate();

            return affectedRows > 0;

        } catch (SQLException e) {

            System.err.println("Error while adding register: " + e.getMessage());

            return false;
        }

    }
    //DELETE car (by ID)
    public boolean deleteCar(int id) {

        String sql = "DELETE FROM cars WHERE id = ?";

        try (Connection conn = connect();
             PreparedStatement pst = conn.prepareStatement(sql)
        ) {

            pst.setInt(1, id);

            int affectedRows = pst.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("Data with ID: " + id + " is erased!");
                return true;
            } else {
                System.out.println("Couldn't find car with ID: " + id + " to erase!");

                return false;
            }

        } catch (SQLException e) {

            System.err.println("Error while deleting register: " + e.getMessage());

            return false;
        }

    }


    //UPDATE car (by ID)
    public boolean updateCar(int id, String brand, String model, Integer year, String registration_number) {

        String sql = "UPDATE cars SET brand =?, model = ?, year = ?, registration_number = ? WHERE id = ?";

        try (Connection conn = connect();
             PreparedStatement pst = conn.prepareStatement(sql)
        ) {

            pst.setString(1, brand);
            pst.setString(2, model);
            pst.setInt(3, year);
            pst.setString(4, registration_number);
            pst.setInt(5, id);

            int affectedRows = pst.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("Car with ID: " + id + " has been updated!");
                return true;
            } else {
                System.out.println("Couldn't find car with ID: " + id + " to update!");

                return false;
            }

        } catch (SQLException e) {

            System.err.println("Error while updating register: " + e.getMessage());

            return false;
        }

    }

    //READ car
    public ArrayList<Car> getAllCars() {
        ArrayList<Car> cars = new ArrayList<>();
        String sql = "SELECT * FROM cars ORDER BY created_at ASC";

        try (Connection conn = connect();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)
        ) {
            while (rs.next()) {

                Car car = new Car (
                        rs.getInt("id"),
                        rs.getString("brand"),
                        rs.getString("model"),
                        rs.getInt("year"),
                        rs.getString("registration_number")
                );
                cars.add(car);

            }

        } catch (SQLException e) {
            System.err.println("Error while loading data from DB: " +e.getMessage());
        }

        return cars;

    }

    // Closing DB connection
    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Connection with DB is closed.");
            } catch (SQLException e) {
                System.err.println("Error while closing DB connection: " + e.getMessage());
            }
        }
    }


}
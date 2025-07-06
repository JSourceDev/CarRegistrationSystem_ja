import DBlogic.DBlogic;
import model.Car;
import UI.CarRegistryAppGUI;

import java.util.ArrayList;
import javax.swing.*;
import java.sql.SQLException;

public class Main {
    // Main method: starts the application
    public static void main(String[] args) {


        //Start DB logic
        DBlogic db = new DBlogic();

        //Try establish connections & handle errors
        try {
            db.connect();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Critical Error: Cannot connect to the DB.\n" + "Check MySQL server and DB configuration.\n" + "Error message: " + e.getMessage(), "Database Connection Error", JOptionPane.ERROR_MESSAGE);

            //Exit due to critical error
            System.exit(1);
        }

        //Shutdown hook to close DB connection
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutting down...closing DB connection.");
            db.closeConnection();
        }));

        //Launch GUI
        SwingUtilities.invokeLater(() -> {
            CarRegistryAppGUI appWindow = new CarRegistryAppGUI(db);
            appWindow.setVisible(true);
        });


        //Get all cars from DB
        ArrayList<Car> carList = db.getAllCars();

        //Print all cars
        if (carList.isEmpty()) {
            System.out.println("No cars found in DB.");
        } else {
            System.out.println("List of cars: ");
            for (Car car : carList) {
                System.out.println(car);
            }
        }
    }
}
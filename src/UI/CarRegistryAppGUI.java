package UI;

import DBlogic.DBlogic;
import model.Car;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

// Main GUI class
public class CarRegistryAppGUI extends JFrame {

    // UI components
    private JTable carTable;
    private DefaultTableModel carTableModel;
    private JTextField brandInput;
    private JTextField modelInput;
    private JTextField yearInput;
    private JTextField regNumberInput;
    private JButton registerButton;
    private JButton updateButton;
    private JButton deleteButton;
    private int selectedCarId = -1;
    private JTextField searchField;
    private List<Car> allCars = new ArrayList<>();

    private DBlogic db;

    //Constructor
    public CarRegistryAppGUI(DBlogic db) {
        this.db = db;

        //Frame settings
        setTitle("Car Registry System");
        setSize(880, 580);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Input fields
        brandInput = new JTextField(12);
        modelInput = new JTextField(12);
        yearInput = new JTextField(7);
        regNumberInput = new JTextField(12);

        //Buttons
        registerButton = new JButton("Register Car");
        updateButton = new JButton("Update Car");
        deleteButton = new JButton("Delete Car");


        // Input panel, labels, fields
        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        inputPanel.add(new JLabel("Brand:"));
        inputPanel.add(brandInput);
        inputPanel.add(new JLabel("Model:"));
        inputPanel.add(modelInput);
        inputPanel.add(new JLabel("Year:"));
        inputPanel.add(yearInput);
        inputPanel.add(new JLabel("Reg. Number:"));
        inputPanel.add(regNumberInput);

        // Search field
        searchField = new JTextField(15);
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { filterCarList(); }
            public void removeUpdate(DocumentEvent e) { filterCarList(); }
            public void changedUpdate(DocumentEvent e) { filterCarList(); }
        });
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(searchField);

        // Button panel alignment
        JPanel buttonsOnlyPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonsOnlyPanel.add(registerButton);
        buttonsOnlyPanel.add(updateButton);
        buttonsOnlyPanel.add(deleteButton);

        //Combine search&button panel
        JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.add(searchPanel, BorderLayout.WEST);
        buttonPanel.add(buttonsOnlyPanel, BorderLayout.EAST);

        // Combine input&buttons on top
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(inputPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Padding for top section
        JPanel paddedTopPanel = new JPanel(new BorderLayout());
        paddedTopPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // top, left, bottom, right
        paddedTopPanel.add(topPanel, BorderLayout.CENTER);
        add(paddedTopPanel, BorderLayout.NORTH);

        // Table&column setup
        String[] columnNames = {"ID", "Brand", "Model", "Year", "Reg. Number"};
        carTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        carTable = new JTable(carTableModel);
        carTable.setRowHeight(24);
        carTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(carTable), BorderLayout.CENTER); // Make it scroll

        // Handle row selection
        carTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && carTable.getSelectedRow() != -1) {
                int row = carTable.getSelectedRow();
                selectedCarId = (int) carTableModel.getValueAt(row, 0);
                brandInput.setText(carTableModel.getValueAt(row, 1).toString());
                modelInput.setText(carTableModel.getValueAt(row, 2).toString());
                yearInput.setText(carTableModel.getValueAt(row, 3).toString());
                regNumberInput.setText(carTableModel.getValueAt(row, 4).toString());
            }
        });

        // Button listeners
        registerButton.addActionListener(e -> registerCar());
        updateButton.addActionListener(e -> updateCar());
        deleteButton.addActionListener(e -> deleteSelectedCar());

        // Initial info from DB
        loadCarsFromDB();
    }

    //Load cars from DB&refresh
    private void loadCarsFromDB() {
        carTableModel.setRowCount(0);
        allCars = db.getAllCars();
        filterCarList();
        System.out.println("Loaded " + allCars.size() + " cars from DB.");
    }

    //Filters displayed cars based on search input
    private void filterCarList() {
        String query = searchField.getText().toLowerCase().trim();
        carTableModel.setRowCount(0);

        for (Car car : allCars) {
            String combined = (car.getBrand() + " " + car.getModel() + " " + car.getYear() + " " + car.getRegistrationNumber()).toLowerCase();
            if (combined.contains(query)) {
                Object[] row = {
                        car.getId(),
                        car.getBrand(),
                        car.getModel(),
                        car.getYear(),
                        car.getRegistrationNumber()
                };
                carTableModel.addRow(row);
            }
        }

        carTable.clearSelection();
        selectedCarId = -1;
    }

    // Register Car & update the existing table
    private void registerCar() {
        String brand = brandInput.getText().trim();
        String model = modelInput.getText().trim();
        String year = yearInput.getText().trim();
        String regNumber = regNumberInput.getText().trim();

        if (!brand.isEmpty() && !model.isEmpty() && !year.isEmpty() && !regNumber.isEmpty()) {
            try {
                boolean addedToDB = db.insertCar(brand, model, Integer.parseInt(year), regNumber);
                if (addedToDB) {
                    loadCarsFromDB();
                    clearForm();
                    JOptionPane.showMessageDialog(this, "Car registered successfully!");
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to add car to DB!", "DB Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Year must be a valid number.", "Input Error", JOptionPane.WARNING_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please fill all the fields!", "Incomplete input!", JOptionPane.WARNING_MESSAGE);
        }
    }

    //Updates car
    private void updateCar() {
        if (selectedCarId == -1) {
            JOptionPane.showMessageDialog(this, "Please select a car to update.");
            return;
        }

        String brand = brandInput.getText().trim();
        String model = modelInput.getText().trim();
        String yearStr = yearInput.getText().trim();
        String regNumber = regNumberInput.getText().trim();

        if (brand.isEmpty() || model.isEmpty() || yearStr.isEmpty() || regNumber.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all the fields!", "Incomplete input!", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int year = Integer.parseInt(yearStr);
            boolean updated = db.updateCar(selectedCarId, brand, model, year, regNumber);

            if (updated) {
                loadCarsFromDB();
                clearForm();
                JOptionPane.showMessageDialog(this, "Car updated successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "Update failed. Car may not exist.", "Update Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Year must be a valid number.", "Input Error", JOptionPane.WARNING_MESSAGE);
        }
    }

    // Deletes car
    private void deleteSelectedCar() {
        int row = carTable.getSelectedRow();
        if (row != -1) {
            int id = (int) carTableModel.getValueAt(row, 0);

            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete this car?",
                    "Confirm Deletion",
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                boolean success = db.deleteCar(id);
                if (success) {
                    loadCarsFromDB();
                    JOptionPane.showMessageDialog(this, "Car deleted successfully!");
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to delete car.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a car to delete.");
        }
    }

    //Clears all input fields
    private void clearForm() {
        brandInput.setText("");
        modelInput.setText("");
        yearInput.setText("");
        regNumberInput.setText("");
        selectedCarId = -1;
    }
}
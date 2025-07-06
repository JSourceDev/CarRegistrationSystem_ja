package model;

public class Car {

    // Car attributes
    private int id;
    private String brand;
    private String model;
    private int year;
    private String registrationNumber;

        // Constructor
        public Car(int id, String brand, String model, int year, String registrationNumber) {
            this.id = id;
            this.brand = brand;
            this.model = model;
            this.year = year;
            this.registrationNumber = registrationNumber;
        }

        // Getters for attributes
        public int getId() {
            return id;
        }
        public String getBrand() {
            return brand;
        }
        public String getModel() {
            return model;
        }
        public int getYear() {
            return year;
        }
        public String getRegistrationNumber() {
            return registrationNumber;
        }

        //Describes the car object
        @Override
        public String toString() {
            return id + ": " + brand + " " + model + " (" + year + ") - " + registrationNumber;

        }
}

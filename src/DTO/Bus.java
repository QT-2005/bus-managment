package DTO;

public class Bus {
    private int id;
    private String busNumber;
    private String busType; // "AC", "NON-AC", "SLEEPER", etc.
    private int capacity;
    private String status; // "ACTIVE", "MAINTENANCE", "INACTIVE"

    public Bus() {
    }

    public Bus(String busNumber, String busType, int capacity, String status) {
        this.busNumber = busNumber;
        this.busType = busType;
        this.capacity = capacity;
        this.status = status;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBusNumber() {
        return busNumber;
    }

    public void setBusNumber(String busNumber) {
        this.busNumber = busNumber;
    }

    public String getBusType() {
        return busType;
    }

    public void setBusType(String busType) {
        this.busType = busType;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return busNumber + " (" + busType + ")";
    }
}
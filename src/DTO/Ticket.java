package DTO;

import java.util.Date;

public class Ticket {
    private int id;
    private int routeId;
    private int customerId;
    private String seatNumber;
    private double price;
    private Date bookingDate;
    private Date travelDate;
    private String status; // "BOOKED", "CANCELLED", "COMPLETED"

    public Ticket() {
    }

    public Ticket(int routeId, int customerId, String seatNumber, double price, Date bookingDate, Date travelDate, String status) {
        this.routeId = routeId;
        this.customerId = customerId;
        this.seatNumber = seatNumber;
        this.price = price;
        this.bookingDate = bookingDate;
        this.travelDate = travelDate;
        this.status = status;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRouteId() {
        return routeId;
    }

    public void setRouteId(int routeId) {
        this.routeId = routeId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Date getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(Date bookingDate) {
        this.bookingDate = bookingDate;
    }

    public Date getTravelDate() {
        return travelDate;
    }

    public void setTravelDate(Date travelDate) {
        this.travelDate = travelDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Ticket #" + id + " - " + seatNumber;
    }
}
package DTO;

import java.sql.Time;

public class Route {
    private int id;
    private String source;
    private String destination;
    private double distance; // in kilometers
    private int duration; // in minutes
    private int busId;
    private Time departureTime;
    private double basePrice;

    public Route() {
    }

    public Route(String source, String destination, double distance, int duration, int busId, Time departureTime, double basePrice) {
        this.source = source;
        this.destination = destination;
        this.distance = distance;
        this.duration = duration;
        this.busId = busId;
        this.departureTime = departureTime;
        this.basePrice = basePrice;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getBusId() {
        return busId;
    }

    public void setBusId(int busId) {
        this.busId = busId;
    }

    public Time getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(Time departureTime) {
        this.departureTime = departureTime;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(double basePrice) {
        this.basePrice = basePrice;
    }

    @Override
    public String toString() {
        return source + " to " + destination + " (" + departureTime + ")";
    }
}
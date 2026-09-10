package com.emergency.model;

public class Ambulance {
    private String id;
    private AmbulanceType type;
    private AmbulanceState state;
    private String driverDetails;
    private double latitude;
    private double longitude;

    public Ambulance(String id, AmbulanceType type, String driverDetails, double latitude, double longitude) {
        this.id = id;
        this.type = type;
        this.state = AmbulanceState.AVAILABLE;
        this.driverDetails = driverDetails;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // Getters and Setters
    public String getId() { return id; }
    public AmbulanceType getType() { return type; }
    public AmbulanceState getState() { return state; }
    public void setState(AmbulanceState state) { this.state = state; }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }
}

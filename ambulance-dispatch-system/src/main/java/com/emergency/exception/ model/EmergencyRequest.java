package com.emergency.model;

public class EmergencyRequest implements Comparable<EmergencyRequest> {
    private String requestId;
    private String patientId;
    private EmergencyPriority priority;
    private AmbulanceType requiredType;
    private double pickupLat;
    private double pickupLon;
    private String destinationHospital;

    public EmergencyRequest(String requestId, String patientId, EmergencyPriority priority, 
                            AmbulanceType requiredType, double pickupLat, double pickupLon, String destinationHospital) {
        this.requestId = requestId;
        this.patientId = patientId;
        this.priority = priority;
        this.requiredType = requiredType;
        this.pickupLat = pickupLat;
        this.pickupLon = pickupLon;
        this.destinationHospital = destinationHospital;
    }

    @Override
    public int compareTo(EmergencyRequest o) {
        return Integer.compare(this.priority.getRank(), o.priority.getRank());
    }

    // Getters
    public String getRequestId() { return requestId; }
    public String getPatientId() { return patientId; }
    public EmergencyPriority getPriority() { return priority; }
    public AmbulanceType getRequiredType() { return requiredType; }
    public double getPickupLat() { return pickupLat; }
    public double getPickupLon() { return pickupLon; }
}

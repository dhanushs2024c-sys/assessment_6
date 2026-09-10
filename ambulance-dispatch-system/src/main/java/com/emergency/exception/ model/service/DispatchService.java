package com.emergency.service;

import com.emergency.exception.*;
import com.emergency.model.*;
import java.util.*;

public class DispatchService {
    private final List<Ambulance> ambulances = new ArrayList<>();
    private final PriorityQueue<EmergencyRequest> waitingQueue = new PriorityQueue<>();
    private final List<String> historyLog = new ArrayList<>();

    public void registerAmbulance(Ambulance ambulance) {
        ambulances.add(ambulance);
    }

    public void submitEmergencyRequest(EmergencyRequest request) {
        if (request == null || request.getPatientId() == null || request.getPatientId().isEmpty()) {
            throw new InvalidRequestException("Invalid emergency request data provided.");
        }
        
        waitingQueue.add(request);
        historyLog.add("Request added to queue: ID " + request.getRequestId() + " (Priority: " + request.getPriority() + ")");
        processQueue();
    }

    public synchronized void processQueue() {
        while (!waitingQueue.isEmpty()) {
            EmergencyRequest currentRequest = waitingQueue.peek();
            Ambulance bestMatch = findBestAmbulance(currentRequest);

            if (bestMatch != null) {
                waitingQueue.poll();
                dispatchAmbulance(bestMatch, currentRequest);
            } else {
                break; // No suitable available ambulance right now, keep rest in queue
            }
        }
    }

    private Ambulance findBestAmbulance(EmergencyRequest request) {
        Ambulance bestAmbulance = null;
        double minDistance = Double.MAX_VALUE;

        for (Ambulance amb : ambulances) {
            if (amb.getState() == AmbulanceState.AVAILABLE && amb.getType() == request.getRequiredType()) {
                double distance = calculateDistance(amb.getLatitude(), amb.getLongitude(), request.getPickupLat(), request.getPickupLon());
                if (distance < minDistance) {
                    minDistance = distance;
                    bestAmbulance = amb;
                }
            }
        }
        return bestAmbulance;
    }

    public void updateAmbulanceState(String ambulanceId, AmbulanceState newState) {
        Ambulance ambulance = ambulances.stream()
                .filter(a -> a.getId().equals(ambulanceId))
                .findFirst()
                .orElseThrow(() -> new ResourceUnavailableException("Ambulance not found."));

        ambulance.setState(newState);
        historyLog.add("Ambulance " + ambulanceId + " updated state to: " + newState);

        if (newState == AmbulanceState.AVAILABLE) {
            processQueue(); // Automatically allocate to waiting requests
        }
    }

    private void dispatchAmbulance(Ambulance ambulance, EmergencyRequest request) {
        ambulance.setState(AmbulanceState.DISPATCHED);
        double distance = calculateDistance(ambulance.getLatitude(), ambulance.getLongitude(), request.getPickupLat(), request.getPickupLon());
        double etaMinutes = (distance / 50.0) * 60.0; // Assuming avg speed 50 km/h

        historyLog.add(String.format("Dispatched Ambulance %s to Request %s. Est. Distance: %.2f km, ETA: %.1f mins", 
                ambulance.getId(), request.getRequestId(), distance, etaMinutes));
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        return Math.sqrt(Math.pow(lat1 - lat2, 2) + Math.pow(lon1 - lon2, 2)) * 111.0; // Simple approximation to km
    }

    public List<String> getHistoryLog() { return new ArrayList<>(historyLog); }
    public int getWaitingQueueSize() { return waitingQueue.size(); }
}

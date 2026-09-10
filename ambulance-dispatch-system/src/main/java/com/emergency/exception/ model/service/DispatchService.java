package com.emergency.service;

import com.emergency.exception.InvalidRequestException;
import com.emergency.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DispatchServiceTest {
    private DispatchService dispatchService;

    @BeforeEach
    public void setup() {
        dispatchService = new DispatchService();
    }

    @Test
    public void testPriorityQueueAndAllocation() {
        Ambulance amb1 = new Ambulance("AMB01", AmbulanceType.ICU, "Driver A", 12.9716, 77.5946);
        dispatchService.registerAmbulance(amb1);

        EmergencyRequest lowPriorityReq = new EmergencyRequest("REQ01", "P101", EmergencyPriority.NORMAL, AmbulanceType.ICU, 12.9800, 77.6000, "City Hospital");
        EmergencyRequest highPriorityReq = new EmergencyRequest("REQ02", "P102", EmergencyPriority.CRITICAL, AmbulanceType.ICU, 12.9900, 77.6100, "General Hospital");

        dispatchService.submitEmergencyRequest(lowPriorityReq);
        dispatchService.submitEmergencyRequest(highPriorityReq);

        // Verify that the critical ambulance request gets actioned via logs
        assertTrue(dispatchService.getHistoryLog().stream().anyMatch(log -> log.contains("Dispatched Ambulance AMB01 to Request REQ02")));
    }

    @Test
    public void testInvalidRequestException() {
        assertThrows(InvalidRequestException.class, () -> {
            dispatchService.submitEmergencyRequest(new EmergencyRequest("REQ03", "", EmergencyPriority.HIGH, AmbulanceType.BASIC, 0, 0, ""));
        });
    }
}

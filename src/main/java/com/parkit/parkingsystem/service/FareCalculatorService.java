package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {
     
	
	 public static final int FREE_DURATION = 30; 
	 public static final double DISCOUNT = 0.05;

    public void calculateFare(Ticket ticket, boolean isRecurrent){
        if( (ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime())) ){
            throw new IllegalArgumentException("Out time provided is incorrect:"+ ticket.getOutTime().toString());
        }
        
        
        Long arrivalDateTimestamp = ticket.getInTime().getTime();
        Long departureDateTimestamp = ticket.getOutTime().getTime();
        long durationInMinutes = (departureDateTimestamp - arrivalDateTimestamp) / 1000 / 60;
       
        if (durationInMinutes <= FREE_DURATION) {
            ticket.setPrice(0);
            return;
        }
      
        switch (ticket.getParkingSpot().getParkingType()){
        case CAR: {
            ticket.setPrice(durationInMinutes * Fare.CAR_RATE_PER_MINUTE * (isRecurrent ? (1 - DISCOUNT) : 1)); 
            break;
        }
        case BIKE: {
            ticket.setPrice(durationInMinutes * Fare.BIKE_RATE_PER_MINUTE * (isRecurrent ? (1 - DISCOUNT) : 1)); 
            break;
        }
        default:
            throw new IllegalArgumentException("Unkown Parking Type");
    }
    }
}
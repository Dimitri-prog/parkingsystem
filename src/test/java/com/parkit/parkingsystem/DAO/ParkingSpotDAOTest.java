package com.parkit.parkingsystem.DAO;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.Date;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;

@ExtendWith(MockitoExtension.class)
public class ParkingSpotDAOTest {
	
	private static TicketDAO ticketDAO; 
	private static ParkingType parkingType ;
	private static ParkingSpotDAO parkingSpotDAO;
	private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
	private static DataBasePrepareService dataBasePrepareService;

     @BeforeAll
     private static void setUp() throws Exception{
    	
     parkingType = parkingType.CAR;
	 dataBasePrepareService = new DataBasePrepareService();
	 parkingSpotDAO = new ParkingSpotDAO();
     parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
     
	 }
 
    @BeforeEach
	private void setUpPerTest() throws Exception {
	
	dataBasePrepareService.clearDataBaseEntries();
	}
    
    @Test
    public void getNextAvailableSlotTest(){
    int parkingNumber=0;  
    parkingNumber = parkingSpotDAO.getNextAvailableSlot(parkingType);

    assertEquals(parkingNumber,1);
    }
    
   /* @Test
    public void updateParkingTest (ParkingSpot parkingSpot){
    	
    	
    	 ParkingSpot parkingSpot1 = new ParkingSpot(3, ParkingType.CAR,false);
         Ticket ticket = new Ticket();
         ticket.setInTime(new Date(System.currentTimeMillis() - (60*60*1000)));
         ticket.setParkingSpot(parkingSpot1);
         ticket.setVehicleRegNumber("abcd");
         ticketDAO.saveTicket(ticket);
         
         Ticket tickets = ticketDAO.getTicket("abcd");
        
         ParkingSpot parkingSpot11 = tickets.getParkingSpot();
         parkingSpot11.setAvailable(true);
         boolean parking = parkingSpotDAO.updateParking(parkingSpot11);
        
         assertEquals(true , parkingSpot11);
         
    
}
   */
   
  }	
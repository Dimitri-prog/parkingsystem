
package com.parkit.parkingsystem.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Date;
import java.text.SimpleDateFormat;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;

import net.bytebuddy.implementation.bind.annotation.Super;

@ExtendWith(MockitoExtension.class)
public class ParkingDataBaseIT {

	private static final String VEHICLE_REG_NUMBER = "ABCDEF";
	private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
	private static ParkingSpotDAO parkingSpotDAO;
	private static TicketDAO ticketDAO;
	private static DataBasePrepareService dataBasePrepareService;

	@Mock
	private static InputReaderUtil inputReaderUtil;

	@BeforeAll
	private static void setUp() throws Exception {
		parkingSpotDAO = new ParkingSpotDAO();
		parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
		ticketDAO = new TicketDAO();
		ticketDAO.dataBaseConfig = dataBaseTestConfig;
		dataBasePrepareService = new DataBasePrepareService();
	}

	@BeforeEach
	private void setUpPerTest() throws Exception {

		when(inputReaderUtil.readSelection()).thenReturn(1);
		when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn(VEHICLE_REG_NUMBER);
		dataBasePrepareService.clearDataBaseEntries();
	}

	@AfterAll
	private static void tearDown() {

		dataBasePrepareService = null;
	}

	@Test
	public void testParkingACar() throws Exception {
		ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
		parkingService.processIncomingVehicle();

		Ticket ticket = ticketDAO.getTicket(VEHICLE_REG_NUMBER);
		assertEquals(ticket.getVehicleRegNumber(), VEHICLE_REG_NUMBER);
		assertEquals(ticket.getParkingSpot().isAvailable(), false);
		verify(inputReaderUtil, Mockito.times(1)).readSelection();
		verify(inputReaderUtil, Mockito.times(1)).readVehicleRegistrationNumber();
	}

	@Test
	public void testParkingLotExit() throws Exception {
		ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");

		parkingService.processIncomingVehicle();
		parkingService.processExitingVehicle();

		Date date = new Date(System.currentTimeMillis());
		Ticket ticket = new Ticket();
		ticket.setInTime(new Date(System.currentTimeMillis() - (60 * 60 * 1000)));
		ticket.setOutTime(date);
		ticket.setVehicleRegNumber(VEHICLE_REG_NUMBER);
		ticket.setParkingSpot(parkingSpot);
		ticketDAO.saveTicket(ticket);
		Ticket ticketResult = ticketDAO.getTicket(VEHICLE_REG_NUMBER);
		ticketResult.setPrice(0.3d);
		ticketResult.getOutTime();

		String expectedDate = formatter.format(date);
		String dateToTest = formatter.format(ticketResult.getOutTime());

		assertEquals(expectedDate, dateToTest);
		assertEquals(ticketResult.getPrice(), 0.3d);

	}

}

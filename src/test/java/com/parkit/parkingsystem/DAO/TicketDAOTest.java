package com.parkit.parkingsystem.DAO;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.Date;
import java.sql.SQLException;

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
import com.parkit.parkingsystem.util.InputReaderUtil;

@ExtendWith(MockitoExtension.class)
public class TicketDAOTest {

	@Mock
	private static InputReaderUtil inputReaderUtil;

	private static Ticket ticket;
	private static final String VEHICLE_REG_NUMBER = "ABCDEF";
	private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
	private static TicketDAO ticketDAO;
	private static DataBasePrepareService dataBasePrepareService;
	private static ParkingSpotDAO parkingSpotDAO;

	@BeforeAll
	private static void setUp() throws Exception {
		ticketDAO = new TicketDAO();
		ticketDAO.dataBaseConfig = dataBaseTestConfig;
		dataBasePrepareService = new DataBasePrepareService();

	}

	@BeforeEach
	private void setUpPerTest() throws Exception {

		dataBasePrepareService.clearDataBaseEntries();
	}

	@Test
	public void getTicketTest() throws SQLException {
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
		Ticket ticket = new Ticket();
		ticket.setInTime(new Date(System.currentTimeMillis() - (60 * 60 * 1000)));
		ticket.setParkingSpot(parkingSpot);
		ticket.setVehicleRegNumber(VEHICLE_REG_NUMBER);
		ticketDAO.saveTicket(ticket);

		Ticket tickets = ticketDAO.getTicket(VEHICLE_REG_NUMBER);

		assertEquals(tickets.getVehicleRegNumber(), VEHICLE_REG_NUMBER);
		assertEquals(tickets.getParkingSpot(), parkingSpot);
	}

	@Test
	public void updateTicketTest() {

		Ticket ticket = new Ticket();
		Date outTime = new Date(System.currentTimeMillis());
		ticket.setPrice(3);
		ticket.setOutTime(outTime);
		boolean ticket2 = ticketDAO.updateTicket(ticket);

		assertEquals(ticket2, true);

	}

	@Test
	public void isReccurentVehicleTest() throws SQLException {
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
		Ticket ticket = new Ticket();
		ticket.setParkingSpot(parkingSpot);
		ticket.setVehicleRegNumber(VEHICLE_REG_NUMBER);
		ticket.setInTime(new Date(System.currentTimeMillis() - (60 * 60 * 1000)));
		ticketDAO.saveTicket(ticket);
		boolean bool = ticketDAO.isReccurentVehicle(VEHICLE_REG_NUMBER);

		assertEquals(bool, false);

	}

}

package com.parkit.parkingsystem.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.constants.DBConstants;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;

public class TicketDAO {

	private static final Logger logger = LogManager.getLogger("TicketDAO");

	public DataBaseConfig dataBaseConfig = new DataBaseConfig();

	/**
	 * Cette méthode permet d'enregistrer les informations d'un ticket dans la base
	 * de données.
	 * 
	 * @param ticket Objet contenant les informations à enregistrer en base de
	 *               données
	 * 
	 * @return true Si le ticket est correctement enregistré dans la base de
	 *         données.
	 * @throws SQLException Exception levée en cas d'évènements imprévus.
	 */
	public boolean saveTicket(Ticket ticket) throws SQLException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		boolean result = false;
		try {
			connection = dataBaseConfig.getConnection();
			preparedStatement = connection.prepareStatement(DBConstants.SAVE_TICKET);
			preparedStatement.setInt(1, ticket.getParkingSpot().getId());
			preparedStatement.setString(2, ticket.getVehicleRegNumber());
			preparedStatement.setDouble(3, ticket.getPrice());
			preparedStatement.setTimestamp(4, new Timestamp(ticket.getInTime().getTime()));
			preparedStatement.setTimestamp(5,
					(ticket.getOutTime() == null) ? null : (new Timestamp(ticket.getOutTime().getTime())));
			result = preparedStatement.execute();

		} catch (Exception ex) {
			logger.error("Error fetching next available slot", ex);
		} finally {
			this.closeConnection(connection, preparedStatement, null);
		}
		return result;
	}

	/**
	 * Cette méthode permet de récupérer un ticket de stationnement enregistré dans
	 * la base de données.
	 * 
	 * @param vehicleRegNumber Numéro d'immatriculation du véhicule passer en
	 *                         paramètre.
	 * 
	 * @return Un ticket avec les informations enregistrées en base de données.
	 */
	public Ticket getTicket(String vehicleRegNumber) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		Ticket ticket = null;
		try {
			connection = dataBaseConfig.getConnection();
			preparedStatement = connection.prepareStatement(DBConstants.GET_TICKET);
			preparedStatement.setString(1, vehicleRegNumber);
			resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				ticket = new Ticket();
				ParkingSpot parkingSpot = new ParkingSpot(resultSet.getInt(1),
						ParkingType.valueOf(resultSet.getString(6)), resultSet.getBoolean(7));
				ticket.setParkingSpot(parkingSpot);
				ticket.setId(resultSet.getInt(2));
				ticket.setVehicleRegNumber(vehicleRegNumber);
				ticket.setPrice(resultSet.getDouble(3));
				ticket.setInTime(resultSet.getTimestamp(4));
				ticket.setOutTime(resultSet.getTimestamp(5));

			}
		} catch (Exception ex) {
			logger.error("Error fetching next available slot", ex);
		} finally {

			this.closeConnection(connection, preparedStatement, resultSet);

		}
		return ticket;
	}

	/**
	 * Cette méthode permet d'actualiser le ticket de stationnement.
	 * 
	 * @param ticket Nom du ticket passer en paramètre.
	 * 
	 * @return true Si le ticket est correctement mis à jour.
	 */
	public boolean updateTicket(Ticket ticket) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			connection = dataBaseConfig.getConnection();
			preparedStatement = connection.prepareStatement(DBConstants.UPDATE_TICKET);
			preparedStatement.setDouble(1, ticket.getPrice());
			preparedStatement.setTimestamp(2, new Timestamp(ticket.getOutTime().getTime()));
			preparedStatement.setInt(3, ticket.getId());
			preparedStatement.execute();
		} catch (Exception ex) {
			logger.error("Error saving ticket info", ex);
		} finally {
			this.closeConnection(connection, preparedStatement, null);
		}
		return true;
	}

	/**
	 * Cette méthode permet de vérifier qu'un utilisateur est récurrent.
	 * 
	 * @param vRegNumber Numéro d'immatriculation du véhicule passer en paramètre.
	 * 
	 * @return true Si l'utilisateur est récurrent.
	 */
	public boolean isReccurentVehicle(String vRegNumber) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		boolean isRecurrent = false;
		try {
			connection = dataBaseConfig.getConnection();
			preparedStatement = connection.prepareStatement(DBConstants.RECURRENT_VEHICLE);
			preparedStatement.setString(1, vRegNumber);
			resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				isRecurrent = resultSet.getInt("VRN") > 1;
			}
		} catch (Exception ex) {
			logger.error("Error fetching next available slot", ex);
		} finally {
			this.closeConnection(connection, preparedStatement, resultSet);
		}
		return isRecurrent;
	}

	private void closeConnection(Connection connection, PreparedStatement preparedStatement, ResultSet resultSet) {
		if (resultSet != null) {
			dataBaseConfig.closeResultSet(resultSet);
		}
		if (preparedStatement != null) {
			dataBaseConfig.closePreparedStatement(preparedStatement);
		}
		if (connection != null) {
			dataBaseConfig.closeConnection(connection);
		}
	}

}
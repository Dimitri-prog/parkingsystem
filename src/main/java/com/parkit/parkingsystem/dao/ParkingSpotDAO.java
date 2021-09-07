package com.parkit.parkingsystem.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.constants.DBConstants;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;

public class ParkingSpotDAO {
	private static final Logger logger = LogManager.getLogger("ParkingSpotDAO");

	public DataBaseConfig dataBaseConfig = new DataBaseConfig();

	/**
	 * Cette méthode permet d'obtenir le prochain numéro de parking disponible, pour
	 * le véhicule passer en paramètre.
	 * 
	 * @param parkingType Type du véhicule à passer en paramètre.
	 * 
	 * @return Le prochain numéro de parking disponible.
	 */
	public int getNextAvailableSlot(ParkingType parkingType) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		int result = -1;
		try {
			connection = dataBaseConfig.getConnection();
			preparedStatement = connection.prepareStatement(DBConstants.GET_NEXT_PARKING_SPOT);
			preparedStatement.setString(1, parkingType.toString());
			resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				result = resultSet.getInt(1);
				;
			}

		} catch (Exception ex) {
			logger.error("Error fetching next available slot", ex);
		} finally {
			this.closeConnection(connection, preparedStatement, resultSet);
		}
		return result;
	}

	/**
	 * Cette méthode permet d'actualiser la disponibilité d'un emplacement de
	 * parking.
	 * 
	 * @param parkingSpot Objet contenant le numéro de parking, le type de véhicule
	 *                    et la disponibilité de l'emplacement.
	 * 
	 * @return L'instruction indiquer pour la disponibilité de l'emplacement(true ou
	 *         false).
	 */
	public boolean updateParking(ParkingSpot parkingSpot) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		int updateRowCount = 0;
		try {
			connection = dataBaseConfig.getConnection();
			preparedStatement = connection.prepareStatement(DBConstants.UPDATE_PARKING_SPOT);
			preparedStatement.setBoolean(1, parkingSpot.isAvailable());
			preparedStatement.setInt(2, parkingSpot.getId());
			updateRowCount = preparedStatement.executeUpdate();
			dataBaseConfig.closePreparedStatement(preparedStatement);

		} catch (Exception ex) {
			logger.error("Error updating parking info", ex);
			return false;
		} finally {
			this.closeConnection(connection, preparedStatement, null);
		}
		return (updateRowCount == 1);
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

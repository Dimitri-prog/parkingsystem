package com.parkit.parkingsystem.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Cette classe permet d'obtenir la connexion à la base donnée et fermer les
 * objets de connexion, PreparedStatement et ResultSet .
 */
public class DataBaseConfig {

	private static final Logger logger = LogManager.getLogger("DataBaseConfig");

	/**
	 * Cette méthode permet d'ouvrir la connexion de la base de données.
	 * 
	 * @return L'ouverture de la connexion.
	 * @throws ClassNotFoundException exception levé en cas d'évènements imprévus.
	 * @throws SQLException           exception levé en cas d'évènements imprévus.
	 */
	public Connection getConnection() throws ClassNotFoundException, SQLException {
		ResourceBundle bundle = ResourceBundle.getBundle("application");

		String driver = bundle.getString("sgbd.driver");
		String login = bundle.getString("sgbd.login");
		String password = bundle.getString("sgbd.password");
		logger.info("Create DB connection");
		Class.forName("com.mysql.cj.jdbc.Driver");

		return DriverManager.getConnection(driver, login, password);

	}

	/**
	 * Cette méthode permet de fermer la connexion à la base de données.
	 * 
	 * @param con Nom de la connexion passer en paramètre.
	 */
	public void closeConnection(Connection connection) {
		if (connection != null) {
			try {
				connection.close();
				logger.info("Closing DB connection");
			} catch (SQLException e) {
				logger.error("Error while closing connection", e);
			}
		}
	}

	/**
	 * Cette méthode permet de fermer les instructions PreparedStatement.
	 * 
	 * @param ps Nom du preparedStatement passer en paramètre.
	 */
	public void closePreparedStatement(PreparedStatement preparedStatement) {
		if (preparedStatement != null) {
			try {
				preparedStatement.close();
				logger.info("Closing Prepared Statement");
			} catch (SQLException e) {
				logger.error("Error while closing prepared statement", e);
			}
		}
	}

	/**
	 * Cette méthode permet de fermer les ResultSet objet.
	 * 
	 * @param rs Nom du resulset passer en paramètre.
	 */
	public void closeResultSet(ResultSet resultSet) {
		if (resultSet != null) {
			try {
				resultSet.close();
				logger.info("Closing Result Set");
			} catch (SQLException e) {
				logger.error("Error while closing result set", e);
			}
		}
	}
}

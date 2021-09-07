package com.parkit.parkingsystem.util;

import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class InputReaderUtil {

	private static Scanner scan = new Scanner(System.in, "UTF-8");
	private static final Logger logger = LogManager.getLogger("InputReaderUtil");

	/**
	 * Cette méthode permet à l'utilisateur d'interagir avec l'application.
	 *
	 * @return La commande que l'utilisateur a entrée.
	 */
	public int readSelection() {
		try {
			int input = Integer.parseInt(scan.nextLine());
			return input;
		} catch (Exception e) {
			logger.error("Error while reading user input from Shell", e);
			System.out.println("Error reading input. Please enter valid number for proceeding further");
			return -1;
		}
	}

	/**
	 * Cette méthode permet à l'utilisateur d'entrer le numéro d'immatriculation du
	 * véhicule
	 * 
	 * @return vehicleRegNumber Numéro d'immatriculation du véhicule.
	 * @throws Exception Exception levée en cas d'évènements imprévus.
	 */
	public String readVehicleRegistrationNumber() throws Exception {
		try {
			String vehicleRegNumber = scan.nextLine();
			if (vehicleRegNumber == null || vehicleRegNumber.trim().length() == 0) {
				throw new IllegalArgumentException("Invalid input provided");
			}
			return vehicleRegNumber;
		} catch (Exception e) {
			logger.error("Error while reading user input from Shell", e);
			System.out.println("Error reading input. Please enter a valid string for vehicle registration number");
			throw e;
		}
	}

}

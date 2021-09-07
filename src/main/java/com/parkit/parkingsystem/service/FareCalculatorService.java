package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

/**
 * Cette classe fournit les services de calcul des tarifs.
 */
public class FareCalculatorService {

	public static final int FREE_DURATION = 30;
	public static final double DISCOUNT = 0.05;

	public void calculateFare(Ticket ticket) {
		calculateFare(ticket, false);
	}

	/**
	 * Cette méthode permet de calculer le prix d'un ticket, de stationner
	 * gratuitement les trente premières minutes et offre une réduction de 5 % pour
	 * les utilisateurs récurrents.
	 * 
	 * @param ticket      Nom du ticket à passer en paramètre.
	 *
	 * @param isRecurrent Nom de l'objet à passer en paramètre permettant de
	 *                    vérifier la récurrence d'un utilisateur.
	 */
	public void calculateFare(Ticket ticket, boolean isRecurrent) {
		if ((ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime()))) {
			throw new IllegalArgumentException("Out time provided is incorrect:" + ticket.getOutTime().toString());
		}

		Long arrivalDateTimestamp = ticket.getInTime().getTime();
		Long departureDateTimestamp = ticket.getOutTime().getTime();
		long durationInMinutes = (departureDateTimestamp - arrivalDateTimestamp) / 1000 / 60;

		if (durationInMinutes <= FREE_DURATION) {
			ticket.setPrice(0);
			return;
		}

		switch (ticket.getParkingSpot().getParkingType()) {
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
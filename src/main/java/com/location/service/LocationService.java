package com.location.service;

import com.location.model.Vehicule;
import com.location.model.Client;
import com.location.model.Location;
import java.time.LocalDate;

public class LocationService {

    public double calculerPrix(Vehicule v, int nbJours) {
        if (nbJours <= 0) {
            throw new IllegalArgumentException("Nombre de jours invalide");
        }
        double total = v.getPrixParJour() * nbJours;
        if (nbJours >= 7) {
             total *= 0.9; // remise de 10 % à partir de 7 jours
            // total *= 0.5;
        }
        return total;
    }

    public void louer(Vehicule v) {
        if (!v.isDisponible()) {
            throw new IllegalStateException("Véhicule indisponible");
        }
        v.setDisponible(false);
    }

    public void retourner(Vehicule v) {
        v.setDisponible(true);
    }


    public Location creerLocation(Client client, Vehicule v, LocalDate debut, LocalDate fin) {
        Location location = new Location(client, v, debut, fin); // valide les dates d'abord
        louer(v);                                                // puis réserve le véhicule
        return location;
    }

    public double calculerPrix(Location location) {
        return calculerPrix(location.getVehicule(), location.getNbJours());
    }

    public static final double TAUX_PENALITE = 1.5;

    public void terminer(Location location, LocalDate dateRetour) {
        location.terminer(dateRetour);
        retourner(location.getVehicule());
    }

    public double calculerPenalite(Location location) {
        return location.getJoursDeRetard()
                * location.getVehicule().getPrixParJour()
                * TAUX_PENALITE;
    }

    public double calculerTotal(Location location) {
        return calculerPrix(location) + calculerPenalite(location);
    }

}
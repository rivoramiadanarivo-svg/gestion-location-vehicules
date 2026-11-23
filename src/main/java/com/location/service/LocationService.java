package com.location.service;

import com.location.model.Vehicule;

public class LocationService {

    public double calculerPrix(Vehicule v, int nbJours) {
        if (nbJours <= 0) {
            throw new IllegalArgumentException("Nombre de jours invalide");
        }
        double total = v.getPrixParJour() * nbJours;
        if (nbJours >= 7) {
            total *= 0.9; // remise de 10 % à partir de 7 jours
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
}
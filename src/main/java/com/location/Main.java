package com.location;

import com.location.model.Client;
import com.location.model.Location;
import com.location.model.Vehicule;
import com.location.service.LocationService;
import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        LocationService service = new LocationService();
        Client client = new Client("C1", "Rakoto", "Jean", "0340000000");
        Vehicule v = new Vehicule("AB-123-CD", "Toyota Corolla", 80);

        Location l = service.creerLocation(client, v,
                LocalDate.of(2026, 12, 1), LocalDate.of(2026, 12, 4));

        System.out.println("Client : " + l.getClient().getNomComplet());
        System.out.println("Véhicule : " + v.getMarque());
        System.out.println("Durée : " + l.getNbJours() + " jours");
        System.out.println("Prix total : " + service.calculerPrix(l));
    }
}
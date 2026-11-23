package com.location;

import com.location.model.Client;
import com.location.model.Location;
import com.location.model.Vehicule;
import com.location.service.Flotte;
import com.location.service.LocationService;

import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        LocationService service = new LocationService();
        Flotte flotte = new Flotte();
        flotte.ajouter(new Vehicule("AB-123-CD", "Toyota Corolla", 80));
        flotte.ajouter(new Vehicule("EF-456-GH", "Renault Clio", 50));
        flotte.ajouter(new Vehicule("IJ-789-KL", "Peugeot 208", 60));

        System.out.println("Véhicules disponibles : " + flotte.disponibles().size());

        Client client = new Client("C1", "Rakoto", "Jean", "0340000000");
        Vehicule choisi = flotte.rechercher("AB-123-CD").orElseThrow();
        Location l = service.creerLocation(client, choisi,
                LocalDate.of(2026, 12, 1), LocalDate.of(2026, 12, 4));

        System.out.println("Client : " + l.getClient().getNomComplet());
        System.out.println("Véhicule : " + choisi.getMarque());
        System.out.println("Prix total : " + service.calculerPrix(l));
        System.out.println("Véhicules disponibles après location : " + flotte.disponibles().size());
        service.terminer(l, LocalDate.of(2026, 12, 6));
        System.out.println("Jours de retard : " + l.getJoursDeRetard());
        System.out.println("Pénalité : " + service.calculerPenalite(l));
        System.out.println("Total à payer : " + service.calculerTotal(l));
    }
}
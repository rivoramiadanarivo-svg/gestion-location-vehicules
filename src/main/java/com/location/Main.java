package com.location;

import com.location.model.Vehicule;
import com.location.service.LocationService;

public class Main {
    public static void main(String[] args) {
        Vehicule v = new Vehicule("AB-123-CD", "Toyota Corolla", 80);
        LocationService service = new LocationService();
        // System.out.println("Tarif pour 3 jours : " + service.calculerPrix(v, 3));
        System.out.println("Simulation ngrok coupé : " + service.calculerPrix(v, 3));
    }
}
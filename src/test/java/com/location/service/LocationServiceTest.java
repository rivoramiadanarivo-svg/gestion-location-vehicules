package com.location.service;

import com.location.model.Vehicule;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import com.location.model.Client;
import com.location.model.Location;
import java.time.LocalDate;

class LocationServiceTest {

    private final LocationService service = new LocationService();

    @Test
    void prixSansRemise() {
        Vehicule v = new Vehicule("A", "Clio", 50);
        assertEquals(150, service.calculerPrix(v, 3), 0.01);
    }

    @Test
    void prixAvecRemise() {
        Vehicule v = new Vehicule("A", "Clio", 100);
        assertEquals(630, service.calculerPrix(v, 7), 0.01);
    }

    @Test
    void louerVehiculeIndisponibleLeveException() {
        Vehicule v = new Vehicule("A", "Clio", 50);
        service.louer(v);
        assertThrows(IllegalStateException.class, () -> service.louer(v));
    }

    @Test
    void retournerRendLeVehiculeDisponible() {
        Vehicule v = new Vehicule("A", "Clio", 50);
        service.louer(v);
        service.retourner(v);
        assertTrue(v.isDisponible());
    }

    @Test
    void nombreDeJoursInvalideLeveException() {
        Vehicule v = new Vehicule("A", "Clio", 50);
        assertThrows(IllegalArgumentException.class, () -> service.calculerPrix(v, 0));
    }

    private final Client client = new Client("C1", "Rakoto", "Jean", "0340000000");

    @Test
    void creerLocationRendLeVehiculeIndisponible() {
        Vehicule v = new Vehicule("A", "Clio", 50);
        service.creerLocation(client, v, LocalDate.of(2026, 12, 1), LocalDate.of(2026, 12, 4));
        assertFalse(v.isDisponible());
    }

    @Test
    void creerLocationSurVehiculeDejaLoueLeveException() {
        Vehicule v = new Vehicule("A", "Clio", 50);
        service.creerLocation(client, v, LocalDate.of(2026, 12, 1), LocalDate.of(2026, 12, 4));
        assertThrows(IllegalStateException.class, () ->
                service.creerLocation(client, v, LocalDate.of(2026, 12, 5), LocalDate.of(2026, 12, 8)));
    }

    @Test
    void terminerLocationRendLeVehiculeDisponible() {
        Vehicule v = new Vehicule("A", "Clio", 50);
        Location l = service.creerLocation(client, v, LocalDate.of(2026, 12, 1), LocalDate.of(2026, 12, 4));
        service.terminer(l);
        assertTrue(v.isDisponible());
        assertTrue(l.isTerminee());
    }

    @Test
    void prixDUneLocationDeSeptJoursAvecRemise() {
        Vehicule v = new Vehicule("A", "Clio", 100);
        Location l = service.creerLocation(client, v, LocalDate.of(2026, 12, 1), LocalDate.of(2026, 12, 8));
        assertEquals(630, service.calculerPrix(l), 0.01);
    }
}
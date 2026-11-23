package com.location.service;

import com.location.model.Vehicule;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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
}
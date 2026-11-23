package com.location.model;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class LocationTest {

    private final Client client = new Client("C1", "Rakoto", "Jean", "0340000000");
    private final Vehicule vehicule = new Vehicule("AB-123-CD", "Clio", 50);

    @Test
    void nombreDeJoursEstCalcule() {
        Location l = new Location(client, vehicule,
                LocalDate.of(2026, 12, 1), LocalDate.of(2026, 12, 4));
        assertEquals(3, l.getNbJours());
    }

    @Test
    void dateFinAvantDebutLeveException() {
        assertThrows(IllegalArgumentException.class, () -> new Location(client, vehicule,
                LocalDate.of(2026, 12, 4), LocalDate.of(2026, 12, 1)));
    }

    @Test
    void dateFinEgaleDebutLeveException() {
        assertThrows(IllegalArgumentException.class, () -> new Location(client, vehicule,
                LocalDate.of(2026, 12, 1), LocalDate.of(2026, 12, 1)));
    }

    @Test
    void joursDeRetardEstZeroTantQueLaLocationNEstPasTerminee() {
        Location l = new Location(client, vehicule,
                LocalDate.of(2026, 12, 1), LocalDate.of(2026, 12, 4));
        assertEquals(0, l.getJoursDeRetard());
    }
}
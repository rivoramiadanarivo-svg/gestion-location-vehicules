package com.location.service;

import com.location.model.Vehicule;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FlotteTest {

    private final Flotte flotte = new Flotte();

    @Test
    void ajouterAugmenteLaTaille() {
        flotte.ajouter(new Vehicule("AB-1", "Clio", 50));
        assertEquals(1, flotte.taille());
    }

    @Test
    void ajouterUneImmatriculationDejaPresenteLeveException() {
        flotte.ajouter(new Vehicule("AB-1", "Clio", 50));
        assertThrows(IllegalArgumentException.class,
                () -> flotte.ajouter(new Vehicule("ab-1", "Polo", 60)));
    }

    @Test
    void rechercherTrouveLeVehicule() {
        flotte.ajouter(new Vehicule("AB-1", "Clio", 50));
        assertTrue(flotte.rechercher("AB-1").isPresent());
        assertEquals("Clio", flotte.rechercher("AB-1").get().getMarque());
    }

    @Test
    void rechercherUnVehiculeInconnuRetourneVide() {
        assertTrue(flotte.rechercher("ZZ-9").isEmpty());
    }

    @Test
    void disponiblesExclutLesVehiculesLoues() {
        Vehicule libre = new Vehicule("AB-1", "Clio", 50);
        Vehicule loue = new Vehicule("AB-2", "Polo", 60);
        loue.setDisponible(false);
        flotte.ajouter(libre);
        flotte.ajouter(loue);

        assertEquals(1, flotte.disponibles().size());
        assertEquals("AB-1", flotte.disponibles().get(0).getImmatriculation());
    }

    @Test
    void ajouterNullLeveException() {
        assertThrows(IllegalArgumentException.class, () -> flotte.ajouter(null));
    }

    @Test
    void tousRetourneTousLesVehicules() {
        flotte.ajouter(new Vehicule("AB-1", "Clio", 50));
        Vehicule loue = new Vehicule("AB-2", "Polo", 60);
        loue.setDisponible(false);
        flotte.ajouter(loue);
        assertEquals(2, flotte.tous().size());
    }
}
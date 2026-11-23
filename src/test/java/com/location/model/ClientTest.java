package com.location.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ClientTest {

    @Test
    void nomCompletConcateneLePrenomEtLeNom() {
        Client c = new Client("C1", "Rakoto", "Jean", "0340000000");
        assertEquals("Jean Rakoto", c.getNomComplet());
    }

    @Test
    void nomVideLeveException() {
        assertThrows(IllegalArgumentException.class,
                () -> new Client("C1", " ", "Jean", "0340000000"));
    }
    @Test
    void prenomVideLeveException() {
        assertThrows(IllegalArgumentException.class,
                () -> new Client("C1", "Rakoto", " ", "0340000000"));
    }

    @Test
    void gettersRetournentLesValeurs() {
        Client c = new Client("C1", "Rakoto", "Jean", "0340000000");
        assertEquals("C1", c.getId());
        assertEquals("Rakoto", c.getNom());
        assertEquals("Jean", c.getPrenom());
        assertEquals("0340000000", c.getTelephone());
    }
}
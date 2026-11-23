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
}
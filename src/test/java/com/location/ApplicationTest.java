package com.location;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class ApplicationTest {

    private String executer(String saisies) {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(buffer, true, StandardCharsets.UTF_8);
        Application app = new Application(new Scanner(saisies), out);
        app.demarrer();
        return buffer.toString(StandardCharsets.UTF_8);
    }

    @Test
    void quitterAfficheAuRevoir() {
        String sortie = executer("0\n");
        assertTrue(sortie.contains("Au revoir."));
    }

    @Test
    void choixInvalideAfficheUnMessage() {
        String sortie = executer("9\n0\n");
        assertTrue(sortie.contains("Choix invalide."));
    }

    @Test
    void listeLesTroisVehiculesDisponibles() {
        String sortie = executer("1\n0\n");
        assertTrue(sortie.contains("AB-123-CD"));
        assertTrue(sortie.contains("EF-456-GH"));
        assertTrue(sortie.contains("IJ-789-KL"));
    }

    @Test
    void creerUneLocationAfficheLePrix() {
        String saisies = String.join("\n",
                "2", "Rakoto", "Jean", "0340000000", "AB-123-CD",
                "2026-12-01", "2026-12-04", "0") + "\n";
        String sortie = executer(saisies);
        assertTrue(sortie.contains("Location créée. Prix prévu : 240.0"));
    }

    @Test
    void vehiculeLoueNApparaitPlusDansLesDisponibles() {
        String saisies = String.join("\n",
                "2", "Rakoto", "Jean", "0340000000", "AB-123-CD",
                "2026-12-01", "2026-12-04", "1", "0") + "\n";
        String sortie = executer(saisies);
        String apresLocation = sortie.substring(sortie.indexOf("Location créée"));
        assertFalse(apresLocation.contains("AB-123-CD - Toyota"));
    }

    @Test
    void dateInvalideAfficheUneErreurSansPlanter() {
        String saisies = String.join("\n",
                "2", "Rakoto", "Jean", "0340000000", "AB-123-CD",
                "pas-une-date", "0") + "\n";
        String sortie = executer(saisies);
        assertTrue(sortie.contains("Date invalide"));
    }

    @Test
    void terminerAvecRetardAfficheLeTotal() {
        String saisies = String.join("\n",
                "2", "Rakoto", "Jean", "0340000000", "AB-123-CD",
                "2026-12-01", "2026-12-04",
                "3", "1", "2026-12-06", "0") + "\n";
        String sortie = executer(saisies);
        assertTrue(sortie.contains("Jours de retard : 2"));
        assertTrue(sortie.contains("Total à payer : 480.0"));
    }
    @Test
    void finDeSaisieSansQuitterNePlantePas() {
        assertDoesNotThrow(() -> executer(""));
    }

    @Test
    void vehiculeIntrouvableAfficheUnMessage() {
        String saisies = String.join("\n",
                "2", "Rakoto", "Jean", "0340000000", "ZZ-999", "0") + "\n";
        assertTrue(executer(saisies).contains("Véhicule introuvable."));
    }

    @Test
    void listeDesLocationsApresCreation() {
        String saisies = String.join("\n",
                "2", "Rakoto", "Jean", "0340000000", "AB-123-CD",
                "2026-12-01", "2026-12-04", "4", "0") + "\n";
        assertTrue(executer(saisies).contains("(en cours)"));
    }

    @Test
    void terminerSansLocationAfficheUnMessage() {
        assertTrue(executer("3\n0\n").contains("Aucune location en cours."));
    }
}
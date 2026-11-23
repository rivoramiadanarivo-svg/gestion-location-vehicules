package com.location;

import com.location.model.Client;
import com.location.model.Location;
import com.location.model.Vehicule;
import com.location.service.Flotte;
import com.location.service.LocationService;

import java.io.PrintStream;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Application {

    private final Flotte flotte = new Flotte();
    private final LocationService service = new LocationService();
    private final List<Location> locations = new ArrayList<>();
    private final Scanner scanner;
    private final PrintStream out;

    public Application(Scanner scanner, PrintStream out) {
        this.scanner = scanner;
        this.out = out;
        flotte.ajouter(new Vehicule("AB-123-CD", "Toyota Corolla", 80));
        flotte.ajouter(new Vehicule("EF-456-GH", "Renault Clio", 50));
        flotte.ajouter(new Vehicule("IJ-789-KL", "Peugeot 208", 60));
    }

    public void demarrer() {
        boolean continuer = true;
        while (continuer) {
            afficherMenu();
            String choix = scanner.nextLine().trim();
            switch (choix) {
                case "1" -> listerDisponibles();
                case "2" -> creerLocation();
                case "3" -> terminerLocation();
                case "4" -> listerLocations();
                case "0" -> {
                    out.println("Au revoir.");
                    continuer = false;
                }
                default -> out.println("Choix invalide.");
            }
        }
    }

    private void afficherMenu() {
        out.println();
        out.println("=== Gestion de location de véhicules ===");
        out.println("1. Véhicules disponibles");
        out.println("2. Nouvelle location");
        out.println("3. Terminer une location");
        out.println("4. Liste des locations");
        out.println("0. Quitter");
        out.print("Votre choix : ");
    }

    private void listerDisponibles() {
        List<Vehicule> dispo = flotte.disponibles();
        if (dispo.isEmpty()) {
            out.println("Aucun véhicule disponible.");
            return;
        }
        for (Vehicule v : dispo) {
            out.println(v.getImmatriculation() + " - " + v.getMarque()
                    + " - " + v.getPrixParJour() + " /jour");
        }
    }

    private void creerLocation() {
        try {
            out.print("Nom du client : ");
            String nom = scanner.nextLine();
            out.print("Prénom du client : ");
            String prenom = scanner.nextLine();
            out.print("Téléphone : ");
            String tel = scanner.nextLine();
            Client client = new Client("C" + (locations.size() + 1), nom, prenom, tel);

            out.print("Immatriculation du véhicule : ");
            Optional<Vehicule> v = flotte.rechercher(scanner.nextLine().trim());
            if (v.isEmpty()) {
                out.println("Véhicule introuvable.");
                return;
            }

            out.print("Date de début (AAAA-MM-JJ) : ");
            LocalDate debut = LocalDate.parse(scanner.nextLine().trim());
            out.print("Date de fin (AAAA-MM-JJ) : ");
            LocalDate fin = LocalDate.parse(scanner.nextLine().trim());

            Location l = service.creerLocation(client, v.get(), debut, fin);
            locations.add(l);
            out.println("Location créée. Prix prévu : " + service.calculerPrix(l));
        } catch (DateTimeParseException e) {
            out.println("Date invalide, utilisez le format AAAA-MM-JJ.");
        } catch (IllegalArgumentException | IllegalStateException e) {
            out.println("Erreur : " + e.getMessage());
        }
    }

    private void terminerLocation() {
        List<Location> enCours = locations.stream().filter(l -> !l.isTerminee()).toList();
        if (enCours.isEmpty()) {
            out.println("Aucune location en cours.");
            return;
        }
        for (int i = 0; i < enCours.size(); i++) {
            Location l = enCours.get(i);
            out.println((i + 1) + ". " + l.getClient().getNomComplet()
                    + " - " + l.getVehicule().getMarque());
        }
        try {
            out.print("Numéro de la location : ");
            int n = Integer.parseInt(scanner.nextLine().trim());
            if (n < 1 || n > enCours.size()) {
                out.println("Numéro invalide.");
                return;
            }
            out.print("Date de retour (AAAA-MM-JJ) : ");
            LocalDate retour = LocalDate.parse(scanner.nextLine().trim());
            Location l = enCours.get(n - 1);
            service.terminer(l, retour);
            out.println("Jours de retard : " + l.getJoursDeRetard());
            out.println("Total à payer : " + service.calculerTotal(l));
        } catch (NumberFormatException e) {
            out.println("Numéro invalide.");
        } catch (DateTimeParseException e) {
            out.println("Date invalide, utilisez le format AAAA-MM-JJ.");
        } catch (IllegalArgumentException e) {
            out.println("Erreur : " + e.getMessage());
        }
    }

    private void listerLocations() {
        if (locations.isEmpty()) {
            out.println("Aucune location enregistrée.");
            return;
        }
        for (Location l : locations) {
            out.println(l.getClient().getNomComplet() + " - " + l.getVehicule().getMarque()
                    + " - du " + l.getDateDebut() + " au " + l.getDateFin()
                    + (l.isTerminee() ? " (terminée)" : " (en cours)"));
        }
    }
}
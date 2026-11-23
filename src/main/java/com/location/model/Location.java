package com.location.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Location {
    private final Client client;
    private final Vehicule vehicule;
    private final LocalDate dateDebut;
    private final LocalDate dateFin;
    private boolean terminee = false;

    private LocalDate dateRetourEffective;

    public Location(Client client, Vehicule vehicule, LocalDate dateDebut, LocalDate dateFin) {
        if (client == null || vehicule == null) {
            throw new IllegalArgumentException("Client et véhicule obligatoires");
        }
        if (dateDebut == null || dateFin == null) {
            throw new IllegalArgumentException("Dates obligatoires");
        }
        if (!dateFin.isAfter(dateDebut)) {
            throw new IllegalArgumentException("La date de fin doit être après la date de début");
        }
        this.client = client;
        this.vehicule = vehicule;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
    }

    public int getNbJours() {
        return (int) ChronoUnit.DAYS.between(dateDebut, dateFin);
    }

    //public void terminer() { this.terminee = true; }
    public void terminer(LocalDate dateRetour) {
        if (dateRetour == null) {
            throw new IllegalArgumentException("Date de retour obligatoire");
        }
        if (dateRetour.isBefore(dateDebut)) {
            throw new IllegalArgumentException("Retour avant le début de la location");
        }
        this.dateRetourEffective = dateRetour;
        this.terminee = true;
    }

    public int getJoursDeRetard() {
        if (dateRetourEffective == null || !dateRetourEffective.isAfter(dateFin)) {
            return 0;
        }
        return (int) ChronoUnit.DAYS.between(dateFin, dateRetourEffective);
    }

    public LocalDate getDateRetourEffective() { return dateRetourEffective; }

    public Client getClient() { return client; }
    public Vehicule getVehicule() { return vehicule; }
    public LocalDate getDateDebut() { return dateDebut; }
    public LocalDate getDateFin() { return dateFin; }
    public boolean isTerminee() { return terminee; }
}
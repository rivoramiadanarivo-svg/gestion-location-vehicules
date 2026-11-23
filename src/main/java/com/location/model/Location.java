package com.location.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Location {
    private final Client client;
    private final Vehicule vehicule;
    private final LocalDate dateDebut;
    private final LocalDate dateFin;
    private boolean terminee = false;

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

    public void terminer() { this.terminee = true; }

    public Client getClient() { return client; }
    public Vehicule getVehicule() { return vehicule; }
    public LocalDate getDateDebut() { return dateDebut; }
    public LocalDate getDateFin() { return dateFin; }
    public boolean isTerminee() { return terminee; }
}
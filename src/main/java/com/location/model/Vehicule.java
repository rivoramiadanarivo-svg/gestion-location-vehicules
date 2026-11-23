package com.location.model;

public class Vehicule {
    private final String immatriculation;
    private final String marque;
    private final double prixParJour;
    private boolean disponible = true;

    public Vehicule(String immatriculation, String marque, double prixParJour) {
        this.immatriculation = immatriculation;
        this.marque = marque;
        this.prixParJour = prixParJour;
    }

    public String getImmatriculation() { return immatriculation; }
    public String getMarque() { return marque; }
    public double getPrixParJour() { return prixParJour; }
    public boolean isDisponible() { return disponible; }
    public void setDisponible(boolean disponible) { this.disponible = disponible; }
}
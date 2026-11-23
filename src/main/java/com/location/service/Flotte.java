package com.location.service;

import com.location.model.Vehicule;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Flotte {
    private final List<Vehicule> vehicules = new ArrayList<>();

    public void ajouter(Vehicule v) {
        if (v == null) {
            throw new IllegalArgumentException("Véhicule obligatoire");
        }
        if (rechercher(v.getImmatriculation()).isPresent()) {
            throw new IllegalArgumentException("Immatriculation déjà enregistrée : " + v.getImmatriculation());
        }
        vehicules.add(v);
    }

    public Optional<Vehicule> rechercher(String immatriculation) {
        return vehicules.stream()
                .filter(v -> v.getImmatriculation().equalsIgnoreCase(immatriculation))
                .findFirst();
    }

    public List<Vehicule> disponibles() {
        return vehicules.stream()
                .filter(Vehicule::isDisponible)
                .toList();
    }

    public int taille() {
        return vehicules.size();
    }
}
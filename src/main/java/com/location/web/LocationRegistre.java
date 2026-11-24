package com.location.web;

import com.location.model.Location;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class LocationRegistre {
    private final List<Location> locations = new ArrayList<>();

    public void ajouter(Location l) { locations.add(l); }

    public List<Location> toutes() { return List.copyOf(locations); }

    public List<Location> enCours() {
        return locations.stream().filter(l -> !l.isTerminee()).toList();
    }
    public Optional<Location> trouver(int index) {
        if (index < 0 || index >= locations.size()) {
            return Optional.empty();
        }
        return Optional.of(locations.get(index));
    }
}
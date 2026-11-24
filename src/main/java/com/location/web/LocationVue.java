package com.location.web;

import java.time.LocalDate;

public record LocationVue(
        int index,
        String client,
        String vehicule,
        LocalDate debut,
        LocalDate fin,
        int nbJours,
        double prix,
        boolean terminee,
        int joursRetard,
        double penalite,
        double total,
        boolean enRetard) {
}
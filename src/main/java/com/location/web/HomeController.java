package com.location.web;

import com.location.model.Location;
import com.location.service.Flotte;
import com.location.service.LocationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
public class HomeController {

    private final Flotte flotte;
    private final LocationRegistre registre;
    private final LocationService service;

    public HomeController(Flotte flotte, LocationRegistre registre, LocationService service) {
        this.flotte = flotte;
        this.registre = registre;
        this.service = service;
    }

    @GetMapping("/")
    public String accueil(Model model) {
        List<Location> toutes = registre.toutes();

        double chiffreAffaires = toutes.stream()
                .filter(Location::isTerminee)
                .mapToDouble(service::calculerTotal)
                .sum();

        long enRetard = registre.enCours().stream()
                .filter(l -> l.getDateFin().isBefore(LocalDate.now()))
                .count();

        List<Location> recentes = new ArrayList<>(toutes);
        Collections.reverse(recentes);

        model.addAttribute("page", "accueil");
        model.addAttribute("nbVehicules", flotte.taille());
        model.addAttribute("nbDisponibles", flotte.disponibles().size());
        model.addAttribute("nbEnCours", registre.enCours().size());
        model.addAttribute("nbEnRetard", enRetard);
        model.addAttribute("chiffreAffaires", chiffreAffaires);
        model.addAttribute("recentes", recentes.stream().limit(5).toList());
        return "index";
    }
}
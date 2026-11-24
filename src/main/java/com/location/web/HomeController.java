package com.location.web;

import com.location.service.Flotte;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final Flotte flotte;
    private final LocationRegistre registre;

    public HomeController(Flotte flotte, LocationRegistre registre) {
        this.flotte = flotte;
        this.registre = registre;
    }

    @GetMapping("/")
    public String accueil(Model model) {
        model.addAttribute("page", "accueil");
        model.addAttribute("nbVehicules", flotte.taille());
        model.addAttribute("nbDisponibles", flotte.disponibles().size());
        model.addAttribute("nbEnCours", registre.enCours().size());
        return "index";
    }
}
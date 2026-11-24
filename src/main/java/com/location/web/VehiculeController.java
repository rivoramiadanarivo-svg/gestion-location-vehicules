package com.location.web;

import com.location.model.Vehicule;
import com.location.service.Flotte;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;

@Controller
@RequestMapping("/vehicules")
public class VehiculeController {

    private final Flotte flotte;

    public VehiculeController(Flotte flotte) {
        this.flotte = flotte;
    }

    @GetMapping
    public String liste(@RequestParam(defaultValue = "") String q,
                        @RequestParam(defaultValue = "tous") String statut,
                        Model model) {
        String recherche = q.trim().toLowerCase();

        List<Vehicule> resultat = flotte.tous().stream()
                .filter(v -> recherche.isEmpty()
                        || v.getMarque().toLowerCase().contains(recherche)
                        || v.getImmatriculation().toLowerCase().contains(recherche))
                .filter(v -> switch (statut) {
                    case "disponible" -> v.isDisponible();
                    case "loue" -> !v.isDisponible();
                    default -> true;
                })
                .toList();

        model.addAttribute("page", "vehicules");
        model.addAttribute("vehicules", resultat);
        model.addAttribute("q", q);
        model.addAttribute("statut", statut);
        return "vehicules";
    }

    @PostMapping
    public String ajouter(@RequestParam String immatriculation,
                          @RequestParam String marque,
                          @RequestParam double prixParJour,
                          RedirectAttributes redirect) {
        try {
            if (immatriculation.isBlank() || marque.isBlank()) {
                throw new IllegalArgumentException("Tous les champs sont obligatoires");
            }
            if (prixParJour <= 0) {
                throw new IllegalArgumentException("Le prix par jour doit être positif");
            }
            Vehicule v = new Vehicule(immatriculation.trim().toUpperCase(), marque.trim(), prixParJour);
            flotte.ajouter(v);
            redirect.addFlashAttribute("succes", "Véhicule ajouté : " + v.getMarque());
        } catch (IllegalArgumentException e) {
            redirect.addFlashAttribute("erreur", e.getMessage());
        }
        return "redirect:/vehicules";
    }
}
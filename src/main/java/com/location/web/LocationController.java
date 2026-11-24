package com.location.web;

import com.location.model.Client;
import com.location.model.Location;
import com.location.model.Vehicule;
import com.location.service.Flotte;
import com.location.service.LocationService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/locations")
public class LocationController {

    private final Flotte flotte;
    private final LocationService service;
    private final LocationRegistre registre;

    public LocationController(Flotte flotte, LocationService service, LocationRegistre registre) {
        this.flotte = flotte;
        this.service = service;
        this.registre = registre;
    }

    @GetMapping
    public String liste(@RequestParam(defaultValue = "tous") String statut, Model model) {
        List<LocationVue> filtrees = vues().stream()
                .filter(v -> switch (statut) {
                    case "en_cours" -> !v.terminee();
                    case "terminees" -> v.terminee();
                    default -> true;
                })
                .toList();

        model.addAttribute("page", "locations");
        model.addAttribute("disponibles", flotte.disponibles());
        model.addAttribute("locations", filtrees);
        model.addAttribute("statut", statut);
        model.addAttribute("aujourdhui", LocalDate.now().toString());
        return "locations";
    }

    @PostMapping
    public String creer(@RequestParam String nom,
                        @RequestParam String prenom,
                        @RequestParam String telephone,
                        @RequestParam String immatriculation,
                        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate debut,
                        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin,
                        RedirectAttributes redirect) {
        try {
            Vehicule vehicule = flotte.rechercher(immatriculation.trim())
                    .orElseThrow(() -> new IllegalArgumentException("Véhicule introuvable"));
            Client client = new Client("C" + (registre.toutes().size() + 1),
                    nom.trim(), prenom.trim(), telephone.trim());
            Location location = service.creerLocation(client, vehicule, debut, fin);
            registre.ajouter(location);
            redirect.addFlashAttribute("succes", "Location créée pour " + client.getNomComplet()
                    + ". Prix prévu : " + service.calculerPrix(location));
        } catch (IllegalArgumentException | IllegalStateException e) {
            redirect.addFlashAttribute("erreur", e.getMessage());
        }
        return "redirect:/locations";
    }

    @PostMapping("/{index}/terminer")
    public String terminer(@PathVariable int index,
                           @RequestParam("dateRetour") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateRetour,
                           RedirectAttributes redirect) {
        try {
            Location location = registre.trouver(index)
                    .orElseThrow(() -> new IllegalArgumentException("Location introuvable"));
            if (location.isTerminee()) {
                throw new IllegalArgumentException("Cette location est déjà terminée");
            }
            service.terminer(location, dateRetour);
            redirect.addFlashAttribute("succes", "Location terminée. Jours de retard : "
                    + location.getJoursDeRetard() + ". Total à payer : " + service.calculerTotal(location));
        } catch (IllegalArgumentException e) {
            redirect.addFlashAttribute("erreur", e.getMessage());
        }
        return "redirect:/locations";
    }

    private List<LocationVue> vues() {
        List<Location> toutes = registre.toutes();
        List<LocationVue> vues = new ArrayList<>();
        for (int i = 0; i < toutes.size(); i++) {
            Location l = toutes.get(i);
            boolean enRetard = !l.isTerminee() && l.getDateFin().isBefore(LocalDate.now());
            vues.add(new LocationVue(i,
                    l.getClient().getNomComplet(),
                    l.getVehicule().getMarque(),
                    l.getDateDebut(),
                    l.getDateFin(),
                    l.getNbJours(),
                    service.calculerPrix(l),
                    l.isTerminee(),
                    l.getJoursDeRetard(),
                    service.calculerPenalite(l),
                    service.calculerTotal(l),
                    enRetard));
        }
        return vues;
    }
}
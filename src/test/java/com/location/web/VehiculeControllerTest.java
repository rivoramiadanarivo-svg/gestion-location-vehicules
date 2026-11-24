package com.location.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class VehiculeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void listeAfficheLesVehiculesDeDepart() throws Exception {
        mockMvc.perform(get("/vehicules"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Toyota Corolla")));
    }

    @Test
    void ajouterUnVehiculeValideLeFaitApparaitre() throws Exception {
        mockMvc.perform(post("/vehicules")
                        .param("immatriculation", "ZZ-111-AA")
                        .param("marque", "Kia Rio")
                        .param("prixParJour", "45"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/vehicules"))
                .andExpect(flash().attributeExists("succes"));

        mockMvc.perform(get("/vehicules"))
                .andExpect(content().string(containsString("Kia Rio")));
    }

    @Test
    void immatriculationDejaPresenteAfficheUneErreur() throws Exception {
        mockMvc.perform(post("/vehicules")
                        .param("immatriculation", "AB-123-CD")
                        .param("marque", "Doublon")
                        .param("prixParJour", "50"))
                .andExpect(redirectedUrl("/vehicules"))
                .andExpect(flash().attributeExists("erreur"));
    }

    @Test
    void prixNegatifAfficheUneErreur() throws Exception {
        mockMvc.perform(post("/vehicules")
                        .param("immatriculation", "ZZ-222-BB")
                        .param("marque", "Peugeot 308")
                        .param("prixParJour", "-10"))
                .andExpect(flash().attributeExists("erreur"));
    }

    @Test
    void rechercheParMarqueFiltreLaListe() throws Exception {
        mockMvc.perform(get("/vehicules").param("q", "corolla"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Toyota Corolla")))
                .andExpect(content().string(org.hamcrest.Matchers.not(containsString("Peugeot 208"))));
    }

    @Test
    void rechercheSansResultatAfficheUnMessage() throws Exception {
        mockMvc.perform(get("/vehicules").param("q", "introuvable123"))
                .andExpect(content().string(containsString("Aucun véhicule ne correspond")));
    }
}
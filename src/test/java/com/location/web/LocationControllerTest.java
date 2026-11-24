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
class LocationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private LocationRegistre registre;

    @Test
    void pageLocationsSAffiche() throws Exception {
        mockMvc.perform(get("/locations"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Nouvelle location")));
    }

    @Test
    void creerUneLocationValideLAjouteALHistorique() throws Exception {
        mockMvc.perform(post("/locations")
                        .param("nom", "Rakoto")
                        .param("prenom", "Jean")
                        .param("telephone", "0340000000")
                        .param("immatriculation", "EF-456-GH")
                        .param("debut", "2026-12-01")
                        .param("fin", "2026-12-04"))
                .andExpect(redirectedUrl("/locations"))
                .andExpect(flash().attributeExists("succes"));

        mockMvc.perform(get("/locations"))
                .andExpect(content().string(containsString("Jean Rakoto")));
    }

    @Test
    void dateDeFinAvantLeDebutAfficheUneErreur() throws Exception {
        mockMvc.perform(post("/locations")
                        .param("nom", "Rabe")
                        .param("prenom", "Paul")
                        .param("telephone", "0341111111")
                        .param("immatriculation", "AB-123-CD")
                        .param("debut", "2026-12-10")
                        .param("fin", "2026-12-05"))
                .andExpect(redirectedUrl("/locations"))
                .andExpect(flash().attributeExists("erreur"));
    }

    @Test
    void terminerAvecRetardAfficheLeTotal() throws Exception {
        mockMvc.perform(post("/locations")
                .param("nom", "Rasoa")
                .param("prenom", "Marie")
                .param("telephone", "0342222222")
                .param("immatriculation", "IJ-789-KL")
                .param("debut", "2026-12-01")
                .param("fin", "2026-12-04"));

        int index = registre.toutes().size() - 1;

        // 3 jours x 60 = 180, plus 2 jours de retard x 60 x 1,5 = 180, soit 360
        mockMvc.perform(post("/locations/" + index + "/terminer")
                        .param("dateRetour", "2026-12-06"))
                .andExpect(redirectedUrl("/locations"))
                .andExpect(flash().attribute("succes", containsString("360")));
    }

    @Test
    void terminerUneLocationInconnueAfficheUneErreur() throws Exception {
        mockMvc.perform(post("/locations/999/terminer")
                        .param("dateRetour", "2026-12-06"))
                .andExpect(flash().attributeExists("erreur"));
    }

    @Test
    void filtreTermineesRepondCorrectement() throws Exception {
        mockMvc.perform(get("/locations").param("statut", "terminees"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Historique")));
    }

    @Test
    void uneLocationDepasseeEstSignaleeEnRetard() throws Exception {
        mockMvc.perform(post("/locations")
                .param("nom", "Randria")
                .param("prenom", "Luc")
                .param("telephone", "0343333333")
                .param("immatriculation", "MN-012-OP")
                .param("debut", "2020-01-01")
                .param("fin", "2020-01-05"));

        mockMvc.perform(get("/locations"))
                .andExpect(content().string(containsString("En retard")));
    }
}
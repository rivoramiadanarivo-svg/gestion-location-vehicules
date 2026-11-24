package com.location.web;

import com.location.model.Vehicule;
import com.location.service.Flotte;
import com.location.service.LocationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public Flotte flotte() {
        Flotte flotte = new Flotte();
        flotte.ajouter(new Vehicule("AB-123-CD", "Toyota Corolla", 80));
        flotte.ajouter(new Vehicule("EF-456-GH", "Renault Clio", 50));
        flotte.ajouter(new Vehicule("IJ-789-KL", "Peugeot 208", 60));
        flotte.ajouter(new Vehicule("MN-012-OP", "Hyundai Tucson", 110));
        return flotte;
    }

    @Bean
    public LocationService locationService() {
        return new LocationService();
    }
}
# Gestion de location de véhicules

Application Java (console) de gestion de location de véhicules, avec une chaîne CI/CD complète : GitHub, Jenkins et ngrok.

## Fonctionnalités

- Gestion de la flotte (ajout, recherche, véhicules disponibles)
- Création et clôture de locations
- Calcul du prix avec remise de 10 % dès 7 jours
- Pénalité de retard : 150 % du prix journalier par jour de retard
- Menu console interactif

## Technologies

- Java 17
- Maven
- JUnit 5
- Jenkins (pipeline déclaratif)
- GitHub (webhook)
- ngrok (tunnel public vers Jenkins)

## Structure

```
src/main/java/com/location
├── Main.java
├── Application.java
├── model/    (Vehicule, Client, Location)
└── service/  (LocationService, Flotte)
src/test/java/com/location
└── tests unitaires (JUnit 5)
Jenkinsfile
```

## Lancer l'application

```bash
mvn clean package
java -jar target/gestion-location-vehicules-1.0.0.jar
```

## Lancer les tests

```bash
mvn test
```

## Pipeline CI/CD

```
git push -> GitHub -> webhook -> ngrok -> Jenkins
```

Étapes du pipeline : Checkout, Build, Tests, Package, Archive, Run.

Chaque push sur `main` déclenche automatiquement un build. Un test qui échoue ou une erreur de compilation bloque le pipeline avant la création du jar.
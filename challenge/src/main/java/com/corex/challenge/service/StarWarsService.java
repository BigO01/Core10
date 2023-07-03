package com.corex.challenge.service;

import com.corex.challenge.clients.SWAPIClient;
import com.corex.challenge.model.*;
import com.corex.challenge.utils.Common;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.CompletableFuture;


@Service
public class StarWarsService {

    private static final String PEOPLE_URI = "/people";
    private static final String LUKE_SKYWALKER_NAME = "Luke Skywalker";

    private final SWAPIClient swapiClient;

    private static final Logger logger = LoggerFactory.getLogger(StarWarsService.class);

    @Autowired
    public StarWarsService(SWAPIClient swapiClient) {
        this.swapiClient = swapiClient;
    }
    private SkyWalker getLukeSkywalker() {
        String lukeSkywalkerUri = PEOPLE_URI + "/?search="+LUKE_SKYWALKER_NAME;
        ResponseEntity<SkyWalkersResponse> response = swapiClient.get(lukeSkywalkerUri, SkyWalkersResponse.class);
        if (!response.hasBody()) {
            throw new RuntimeException("SWAPI People API Failed");
        }
        SkyWalker lukeSkywalker = response.getBody().getResults().stream()
                .filter(skywalker -> LUKE_SKYWALKER_NAME.equals(skywalker.getName()))
                .findFirst()
                .orElse(null);
        if (lukeSkywalker == null) {
            throw new RuntimeException("Luke Skywalker not found");
        }
        return lukeSkywalker;
    }

    private List<Starship> getStarshipsOfSkywalker(List<String> starshipUrls) {
        List<Starship> starships = new ArrayList<>();
        for (String starshipUrl : starshipUrls) {
            ResponseEntity<Starship> response = swapiClient.get(Common.getRelativeUrl(starshipUrl), Starship.class);
            if (response.hasBody()) {
                starships.add(response.getBody());
            }
        }
        return starships;
    }

    public List<Starship> getLukeStarships() {
        logger.info("Fetching Luke Skywalker's starships");
        SkyWalker lukeSkywalker = getLukeSkywalker();
        return getStarshipsOfSkywalker(lukeSkywalker.getStarships());
    }
    public HashSet<String> getEpisode1Species() {
        logger.info("Fetching species classifications from Episode 1");
        ResponseEntity<Film> response = swapiClient.get("/films/1/", Film.class);
        if (!response.hasBody()) {
            throw new RuntimeException("SWAPI Films API Failed");
        }

        Film film = response.getBody();
        HashSet<String> speciesClassifications = new HashSet<>();
        List<CompletableFuture<ResponseEntity<Species>>> speciesFutures = new ArrayList<>();

        for (String speciesUrl : film.getSpecies()) {
            CompletableFuture<ResponseEntity<Species>> speciesFuture = CompletableFuture.supplyAsync(() -> swapiClient.get(Common.getRelativeUrl(speciesUrl), Species.class));
            speciesFutures.add(speciesFuture);
        }

        CompletableFuture<Void> allSpeciesFutures = CompletableFuture.allOf(speciesFutures.toArray(new CompletableFuture[0]));

        try {
            allSpeciesFutures.join(); // Wait for all species requests to complete
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch species classifications", e);
        }

        for (CompletableFuture<ResponseEntity<Species>> speciesFuture : speciesFutures) {
            try {
                ResponseEntity<Species> speciesResponse = speciesFuture.get();
                if (speciesResponse.hasBody()) {
                    Species species = speciesResponse.getBody();
                    speciesClassifications.add(species.getClassification());
                }
            } catch (Exception e) {
                throw new RuntimeException("Failed to retrieve species classification", e);
            }
        }

        return speciesClassifications;
    }

    public long getTotalPopulation() {
        logger.info("Calculating total population of all planets");
        ResponseEntity<PlanetsResponse> response = swapiClient.get("/planets/", PlanetsResponse.class);
        if (!response.hasBody()) {
            throw new RuntimeException("SWAPI Planets API Failed");
        }

        PlanetsResponse planetsResponse = response.getBody();
        long totalPopulation = 0;

        do{

            for (Planet planet : planetsResponse.getResults()) {
                String population = planet.getPopulation();
                if (!population.isEmpty() && !population.equals("unknown")) {
                    totalPopulation += Long.parseLong(population);
                }
            }
            response = swapiClient.get(Common.getRelativeUrl(planetsResponse.getNext()), PlanetsResponse.class);
            if (!response.hasBody()) {
                throw new RuntimeException("SWAPI Planets API Failed");
            }
            planetsResponse = response.getBody();
        }while(planetsResponse.getNext()!=null);



        return totalPopulation;
    }


}


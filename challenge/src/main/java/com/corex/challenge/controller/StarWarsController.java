package com.corex.challenge.controller;
import com.corex.challenge.model.Starship;
import com.corex.challenge.service.StarWarsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@RequestMapping("/api")
public class StarWarsController {

    private static final Logger logger = LoggerFactory.getLogger(StarWarsController.class);

    private final StarWarsService starWarsService;

    @Autowired
    public StarWarsController(StarWarsService starWarsService) {
        this.starWarsService = starWarsService;
    }

    @GetMapping("/luke-starships")
    public ResponseEntity<Map<String, List<Starship>>> getLukeStarships() {
        logger.info("GET /api/luke-starships");
        List<Starship> lukeStarships = starWarsService.getLukeStarships();
        Map<String, List<Starship>> response = new HashMap<>();
        response.put("lukeStarships", lukeStarships);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/episode-1-species")
    public ResponseEntity<Map<String, HashSet<String>>> getEpisode1SpeciesClassification() {
        logger.info("GET /api/episode-1-species");
        HashSet<String> episode1Species = starWarsService.getEpisode1Species();

        Map<String, HashSet<String>> response = new HashMap<>();
        response.put("episode1Species", episode1Species);

        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/total-population")
    public ResponseEntity<Map<String, Long>> getTotalPopulation() {
        logger.info("GET /api/total-population");
        long totalPopulation = starWarsService.getTotalPopulation();

        Map<String, Long> response = new HashMap<>();
        response.put("totalPopulation", totalPopulation);

        return ResponseEntity.ok(response);
    }
}
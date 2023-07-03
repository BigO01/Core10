package com.corex.challenge.service;

import com.corex.challenge.clients.SWAPIClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import com.corex.challenge.model.*;
import org.mockito.InjectMocks;
import java.util.Collections;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;

public class StarWarsServiceTest {

    @InjectMocks
    private StarWarsService starWarsService;

    @Mock
    private SWAPIClient swapiClient;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetLukeStarships_Success() {
        // Mock response entity for getLukeSkywalker
        ResponseEntity<SkyWalkersResponse> skywalkersResponse = ResponseEntity.ok(createSkyWalkersResponse());

        // Mock response entity for getStarshipsOfSkywalker
        ResponseEntity<Starship> starshipResponse = ResponseEntity.ok(createStarship());

        // Mock SWAPIClient for getLukeSkywalker
        when(swapiClient.get(anyString(), eq(SkyWalkersResponse.class))).thenReturn(skywalkersResponse);

        // Mock SWAPIClient for getStarshipsOfSkywalker
        when(swapiClient.get(anyString(), eq(Starship.class))).thenReturn(starshipResponse);

        // Perform test
        List<Starship> result = starWarsService.getLukeStarships();

        // Verify
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Starship 1", result.get(0).getName());
    }

    @Test
    public void testGetLukeStarships_Failure_NoLukeSkywalker() {
        // Mock response entity for getLukeSkywalker with empty results
        SkyWalkersResponse emptyResponse = new SkyWalkersResponse();
        emptyResponse.setResults(Collections.emptyList());
        ResponseEntity<SkyWalkersResponse> skywalkersResponse = ResponseEntity.ok(emptyResponse);

        // Mock SWAPIClient for getLukeSkywalker
        when(swapiClient.get(anyString(), eq(SkyWalkersResponse.class))).thenReturn(skywalkersResponse);

        // Perform test and verify exception
        assertThrows(RuntimeException.class, () -> starWarsService.getLukeStarships());
    }

    @Test
    public void testGetLukeStarships_Failure_NoStarships() {
        // Mock response entity for getLukeSkywalker
        ResponseEntity<SkyWalkersResponse> skywalkersResponse = ResponseEntity.ok(createSkyWalkersEmptyResponse());

        // Mock response entity for getStarshipsOfSkywalker with no body
        ResponseEntity<Starship> starshipResponse = ResponseEntity.ok().build();

        // Mock SWAPIClient for getLukeSkywalker
        when(swapiClient.get(anyString(), eq(SkyWalkersResponse.class))).thenReturn(skywalkersResponse);

        // Mock SWAPIClient for getStarshipsOfSkywalker
        when(swapiClient.get(anyString(), eq(Starship.class))).thenReturn(starshipResponse);

        // Perform test and verify exception
        assertThrows(RuntimeException.class, () -> starWarsService.getLukeStarships());
    }

    private SkyWalkersResponse createSkyWalkersResponse() {
        SkyWalkersResponse response = new SkyWalkersResponse();
        SkyWalker lukeSkywalker = new SkyWalker();
        lukeSkywalker.setStarships(new ArrayList<>());
        lukeSkywalker.getStarships().add("DummyURL/forDummy/");

        lukeSkywalker.setName("Luke Skywalker");
        response.setResults(Collections.singletonList(lukeSkywalker));
        return response;
    }


    private SkyWalkersResponse createSkyWalkersEmptyResponse() {
        SkyWalkersResponse response = new SkyWalkersResponse();
        SkyWalker lukeSkywalker = new SkyWalker();

        lukeSkywalker.setName("Luke Skywalker");
        response.setResults(Collections.singletonList(lukeSkywalker));
        return response;
    }

    private Starship createStarship() {
        Starship starship = new Starship();
        starship.setName("Starship 1");
        return starship;
    }
}
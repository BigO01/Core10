package com.corex.challenge.clients;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class SWAPIClient {

    private final String SWAPI_BASE_URL ;

    private final RestTemplate restTemplate;

    private static final Logger logger = LoggerFactory.getLogger(SWAPIClient.class);

    public SWAPIClient(@Value("${swapi.base-url}") String swapiBaseUrl) {
        SWAPI_BASE_URL = swapiBaseUrl;
        restTemplate = new RestTemplate();
    }

    public <T> ResponseEntity<T> get(String uri, Class<T> responseType) {
        String url = SWAPI_BASE_URL + uri;
        logger.info("Making GET request to: {}", url);
        return restTemplate.getForEntity(url, responseType);
    }
}

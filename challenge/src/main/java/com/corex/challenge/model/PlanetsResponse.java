package com.corex.challenge.model;

import lombok.Data;

import java.util.List;

@Data
public class PlanetsResponse {
    private Integer count;
    private String next;
    private String previous;
    private List<Planet> results;
}
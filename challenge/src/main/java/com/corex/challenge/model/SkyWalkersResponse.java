package com.corex.challenge.model;

import lombok.Data;

import java.util.List;

@Data
public class SkyWalkersResponse {
    private Integer count;
    private String next;
    private String previous;
    private List<SkyWalker> results;
}

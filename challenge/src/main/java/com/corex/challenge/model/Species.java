package com.corex.challenge.model;


import lombok.Data;

import java.util.List;

@Data
public class Species {
    private String name;
    private String classification;
    private String designation;
    private String average_height;
    private String average_lifespan;
    private String eye_colors;
    private String hair_colors;
    private String skin_colors;
    private String language;
    private String homeworld;
    private List<String> people;
    private List<String> films;
    private String created;
    private String edited;
    private String url;
}




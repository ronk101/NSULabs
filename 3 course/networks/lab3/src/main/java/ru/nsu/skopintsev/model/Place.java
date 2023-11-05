package ru.nsu.skopintsev.model;

import lombok.Data;

@Data
public class Place {
    private Double lat;
    private Double lng;
    private String name;
    private String address;
    private String imageUrl;

    private String description;
}
package ru.nsu.skopintsev.model;

import lombok.Data;

@Data
public class Location {
    private double lat;
    private double lng;

    private String name;
    private String address;

    @Override
    public String toString() {
        return name + "\n\t" +
                address + " [" +
                lat + "; " +
                lng + "]";
    }
}

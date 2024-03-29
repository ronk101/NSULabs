package ru.nsu.skopintsev.controller;

import ru.nsu.skopintsev.model.*;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;

public class MainController {
    public void start() {
        Scanner scanner = new Scanner(System.in);
        String locationName = askLocationName(scanner);

        LocationController locationController = new LocationController();
        CompletableFuture<Location[]> locationFuture = locationController.searchLocations(locationName);

        locationFuture.thenAccept(locations -> {
            try {
                Location selectedLocation = chooseLocation(locations, scanner);
                if (selectedLocation != null) {
                    WeatherController weatherController = new WeatherController();
                    CompletableFuture<Weather> weatherFuture = weatherController.getWeatherByCoords(
                            selectedLocation.getLat(), selectedLocation.getLng());

                    PlacesController placesController = new PlacesController();
                    CompletableFuture<String[]> placesFuture = placesController.getInterestingPlaces(
                            selectedLocation.getLat(), selectedLocation.getLng());

                    placesFuture.thenAccept(xids -> {
                        PlaceDescriptionController placeDescriptionController = new PlaceDescriptionController();
                        ArrayList<Place> places = placeDescriptionController.getPlacesDescriptions(xids);
                        try {
                            Result result = new Result(selectedLocation, weatherFuture.get(), places);

                            showFinalResults(result);
                        } catch (Exception e) {
                            System.err.println(e.getMessage());

                            showFinalResults(new Result(selectedLocation, places));
                        }
                    });
                } else {
                    System.err.println("Места не найдены");
                }
            } catch (Exception e) {
                System.err.println(e.getCause().getMessage());
            }
        }).exceptionally(ex -> {
            System.err.println("ERROR " + ex.getCause().getMessage());
            return null;
        });
    }

    private String askLocationName(Scanner scanner) {
        System.out.print("Введите название локации: ");

        return scanner.nextLine();
    }

    private Location chooseLocation(Location[] locations,Scanner scanner) {
        if (locations.length > 0) {
            System.out.println("Выберите локацию:");
            for (int i = 0; i < locations.length; i++) {
                System.out.println(i + 1 + ". " + locations[i]);
            }

            System.out.print("Введите номер выбранной локации: ");
            int selectedLocationIndex = Integer.parseInt(scanner.nextLine()) - 1;
            System.out.println();

            return locations[selectedLocationIndex];
        }
        return null;
    }

    private void showFinalResults(Result result) {
        System.out.println("Погода в " + result.getLocation().getName() + ":");
        System.out.println(result.getWeather());

        for (Place place : result.getPlaces()) {
            System.out.println("\n" + place);
            System.out.println();
        }

        System.out.println("THE END");
    }
}

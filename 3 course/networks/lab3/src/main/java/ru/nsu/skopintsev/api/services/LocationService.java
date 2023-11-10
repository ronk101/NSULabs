package ru.nsu.skopintsev.api.services;

import okhttp3.Request;
import okhttp3.ResponseBody;
import com.google.gson.Gson;
import ru.nsu.skopintsev.api.responses.LocationResponse;

import java.io.IOException;

public class LocationService {
    private static final String API_URL = "https://graphhopper.com/api/1/geocode";

    public Request getRequest(String query) {
        
        String apiUrl = API_URL + "?q=" + query + "&key=" + apiKey;

        Request request = new Request.Builder()
                .url(apiUrl)
                .get()
                .build();

        return request;
    }

    public LocationResponse responseBodyToLocationResponse(ResponseBody responseBody) throws IOException {
        String json = responseBody.string();

        Gson gson = new Gson();
        LocationResponse locationResponse = gson.fromJson(json, LocationResponse.class);

        return locationResponse;
    }
}

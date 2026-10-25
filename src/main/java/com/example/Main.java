package com.example;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


public class Main {
    private static final String OPENSKY_URL =
            "https://opensky-network.org/api/states/all?lamin=59.00&lomin=17.00&lamax=60.50&lomax=19.00";

    public static void main(String[] args) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(OPENSKY_URL))
                .GET()
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode root = mapper.readTree(response.body());
                JsonNode states = root.get("states");

                if (states != null && states.isArray()) {
                    for (JsonNode state : states) {
                        String callsign = state.get(1).asText("N/A").trim();
                        String country = state.get(2).asText("N/A").trim();
                        System.out.println("Πτήση: " + callsign +
                                " | Χώρα: " + country);
                    }
                } else {
                    System.out.println("No airplanes");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }}
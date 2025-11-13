package com.illia.carrental.payments.service;

import com.illia.carrental.payments.config.CarRentalCoreConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Component
@RequiredArgsConstructor
public class CarRentalCoreNotificator {

    private final HttpClient httpClient;
    private final CarRentalCoreConfig coreConfig;

    public void notifyReservationCompleted(Long reservationId) {
        try {
            var url = buildUrl(reservationId);
            System.out.println("Url: " + url);
            var request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.noBody())
                    .build();

            var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() >= 400) {
                System.err.println("Core notification failed with status: " + response.statusCode());
            }

        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    private String buildUrl(Long reservationId) {
        return String.format(coreConfig.confirmReservationUrl(), reservationId);
    }
}
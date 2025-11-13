package com.illia.carrental.payments.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "com.illia.carrental.core")
public record CarRentalCoreConfig(String confirmReservationUrl) {

}

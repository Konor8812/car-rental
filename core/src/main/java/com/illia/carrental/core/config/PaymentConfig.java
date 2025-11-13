package com.illia.carrental.core.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "com.illia.carrental.payment")
public record PaymentConfig(String createPaymentLinkUrl,
                            String clientSecret) {

    public String getCreatePaymentLinkUrl() {
        return createPaymentLinkUrl;
    }
}

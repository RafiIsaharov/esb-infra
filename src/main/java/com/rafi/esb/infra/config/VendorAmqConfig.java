package com.rafi.esb.infra.config;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.Getter;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.util.Optional;

@Getter
@ApplicationScoped
public class VendorAmqConfig {
    @ConfigProperty(name = "finastra.camel.vendor.amq.username")
    String username;
    @ConfigProperty(name = "finastra.camel.vendor.amq.password")
    String password;
    @ConfigProperty(name = "finastra.camel.vendor.amq.brokerURL")
    String brokerURL;
    @ConfigProperty(name = "finastra.camel.vendor.amq.keyStorePath", defaultValue = "")
    Optional<String> keyStorePath;
    @ConfigProperty(name = "finastra.camel.vendor.amq.keyStorePassword")
    String keyStorePassword;
    @ConfigProperty(name = "finastra.camel.vendor.amq.trustStorePath", defaultValue = "")
    Optional<String> trustStorePath;
    @ConfigProperty(name = "finastra.camel.vendor.amq.trustStorePassword")
    String trustStorePassword;

}

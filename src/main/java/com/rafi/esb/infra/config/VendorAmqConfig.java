package com.rafi.esb.infra.config;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.Getter;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.util.Optional;

@Getter
@ApplicationScoped
public class VendorAmqConfig {
    @ConfigProperty(name = "esb.infra.vendor.amq.username")
    String username;
    @ConfigProperty(name = "esb.infra.vendor.amq.password")
    String password;
    @ConfigProperty(name = "esb.infra.vendor.amq.brokerURL")
    String brokerURL;
    @ConfigProperty(name = "esb.infra.vendor.amq.keyStorePath", defaultValue = "")
    Optional<String> keyStorePath;
    @ConfigProperty(name = "esb.infra.vendor.amq.keyStorePassword")
    String keyStorePassword;
    @ConfigProperty(name = "esb.infra.vendor.amq.trustStorePath", defaultValue = "")
    Optional<String> trustStorePath;
    @ConfigProperty(name = "esb.infra.vendor.amq.trustStorePassword")
    String trustStorePassword;

}

package com.rafi.esb.infra.config;


import jakarta.enterprise.context.ApplicationScoped;
import lombok.Getter;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.util.Optional;
@Getter
@ApplicationScoped
public class VendorIbmqConfig {
    @ConfigProperty(name = "finastra.camel.vendor.ibmmq.user")
    Optional<String> username;
    @ConfigProperty(name = "finastra.camel.vendor.ibmmq.password")
    Optional<String> password;
    @ConfigProperty(name = "finastra.camel.vendor.ibmmq.hostName")
    String hostName;
    @ConfigProperty(name = "finastra.camel.vendor.ibmmq.port")
    int port;
    @ConfigProperty(name = "finastra.camel.vendor.ibmmq.queueManager")
    String queueManager;
    @ConfigProperty(name = "finastra.camel.vendor.ibmmq.channel")
    String channel;
    @ConfigProperty(name = "finastra.camel.vendor.ibmmq.cipher")
    Optional<String> cipher;
    @ConfigProperty(name = "finastra.camel.vendor.ibmmq.keyStorePath", defaultValue = "")
    Optional<String> keyStorePath;
    @ConfigProperty(name = "finastra.camel.vendor.ibmmq.keyStorePassword")
    String keyStorePassword;
    @ConfigProperty(name = "finastra.camel.vendor.ibmmq.trustStorePath", defaultValue = "")
    Optional<String> trustStorePath;
    @ConfigProperty(name = "finastra.camel.vendor.ibmmq.trustStorePassword")
    String trustStorePassword;

}

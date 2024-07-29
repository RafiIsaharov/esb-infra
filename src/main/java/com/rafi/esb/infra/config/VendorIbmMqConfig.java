package com.rafi.esb.infra.config;


import jakarta.enterprise.context.ApplicationScoped;
import lombok.Getter;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.util.Optional;
@Getter
@ApplicationScoped
public class VendorIbmMqConfig {
    @ConfigProperty(name = "esb.infra.vendor.ibmmq.user")
    Optional<String> username;
    @ConfigProperty(name = "esb.infra.vendor.ibmmq.password")
    Optional<String> password;
    @ConfigProperty(name = "esb.infra.vendor.ibmmq.hostName")
    String hostName;
    @ConfigProperty(name = "esb.infra.vendor.ibmmq.port")
    int port;
    @ConfigProperty(name = "esb.infra.vendor.ibmmq.queueManager")
    String queueManager;
    @ConfigProperty(name = "esb.infra.vendor.ibmmq.channel")
    String channel;
    @ConfigProperty(name = "esb.infra.vendor.ibmmq.cipher")
    Optional<String> cipher;
    @ConfigProperty(name = "esb.infra.vendor.ibmmq.keyStorePath", defaultValue = "")
    Optional<String> keyStorePath;
    @ConfigProperty(name = "esb.infra.vendor.ibmmq.keyStorePassword")
    String keyStorePassword;
    @ConfigProperty(name = "esb.infra.vendor.ibmmq.trustStorePath", defaultValue = "")
    Optional<String> trustStorePath;
    @ConfigProperty(name = "esb.infra.vendor.ibmmq.trustStorePassword")
    String trustStorePassword;

}

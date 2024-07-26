package com.rafi.esb.infra.config;


import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.util.Optional;
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

    public Optional<String> getUsername() {
        return username;
    }
    public Optional<String> getPassword() {
        return password;
    }
    public String getHostName() {
        return hostName;
    }
    public int getPort() {
        return port;
    }
    public String getQueueManager() {
        return queueManager;
    }
    public String getChannel() {
        return channel;
    }
    public Optional<String> getCipher() {
        return cipher;
    }
    public Optional<String> getKeyStorePath() {
        return keyStorePath;
    }
    public String getKeyStorePassword() {
        return keyStorePassword;
    }
    public Optional<String> getTrustStorePath() {
        return trustStorePath;
    }
    public String getTrustStorePassword() {
        return trustStorePassword;
    }
}

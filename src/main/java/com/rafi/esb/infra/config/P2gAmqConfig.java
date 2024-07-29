package com.rafi.esb.infra.config;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.Getter;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.util.Optional;


@Getter
@ApplicationScoped
public class P2gAmqConfig {
    @ConfigProperty(name = "esb.infra.p2g.amq.username")
    String username;
    @ConfigProperty(name = "esb.infra.p2g.amq.password")
    String password;
    @ConfigProperty(name = "esb.infra.p2g.amq.brokerURL")
    String brokerURL;
    @ConfigProperty(name = "esb.infra.p2g.amq.keyStorePath", defaultValue = "")
    Optional<String> keyStorePath;
    @ConfigProperty(name = "esb.infra.p2g.amq.keyStorePassword")
    String keyStorePassword;
    @ConfigProperty(name = "esb.infra.p2g.amq.trustStorePath", defaultValue = "")
    Optional<String> trustStorePath;
    @ConfigProperty(name = "esb.infra.p2g.amq.trustStorePassword")
    String trustStorePassword;

}

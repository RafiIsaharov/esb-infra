package com.rafi.esb.infra.components.amq;

import com.rafi.esb.infra.config.P2gAmqConfig;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.apache.activemq.ActiveMQSslConnectionFactory;

import java.util.Optional;

@Named("amqConnectionFactoryP2G")
@ApplicationScoped
public class P2GActiveMQConnectionFactoryConfig extends ActiveMQSslConnectionFactory {

    @Inject
    public P2GActiveMQConnectionFactoryConfig(
            P2gAmqConfig p2gAmqConfig) throws Exception {
        setBrokerURL(p2gAmqConfig.getBrokerURL());
        setUserName(p2gAmqConfig.getUsername());
        setPassword(p2gAmqConfig.getPassword());
        Optional<String> trustStorePath = p2gAmqConfig.getTrustStorePath();
        if(trustStorePath.isPresent() && !trustStorePath.get().isEmpty()){
            // Set the SSL properties
            setTrustStore(trustStorePath.get());
            setTrustStorePassword(p2gAmqConfig.getTrustStorePassword());
            setTrustStoreType("PKCS12");
        }
        Optional<String> keyStorePath = p2gAmqConfig.getKeyStorePath();
        if(keyStorePath.isPresent() && !keyStorePath.get().isEmpty()) {
            setKeyStore(keyStorePath.get());
            setKeyStorePassword(p2gAmqConfig.getKeyStorePassword());
        }
    }
    @SuppressWarnings("unused")
    public P2GActiveMQConnectionFactoryConfig() {
        // Default constructor
    }
}
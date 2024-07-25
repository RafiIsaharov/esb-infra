package com.rafi.esb.infra.components.amq;

import com.rafi.esb.infra.config.VendorAmqConfig;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.apache.activemq.ActiveMQSslConnectionFactory;

import java.util.Optional;

@Named("amqConnectionFactoryVendor")
@ApplicationScoped
public class VendorActiveMQConnectionFactoryConfig extends ActiveMQSslConnectionFactory {

    @Inject
    public VendorActiveMQConnectionFactoryConfig(
            VendorAmqConfig vendorAmqConfig) throws Exception {
        setBrokerURL(vendorAmqConfig.getBrokerURL());
        setUserName(vendorAmqConfig.getUsername());
        setPassword(vendorAmqConfig.getPassword());
        Optional<String> trustStorePath = vendorAmqConfig.getTrustStorePath();
        if(trustStorePath.isPresent() && !trustStorePath.get().isEmpty()){
            // Set the SSL properties
            setTrustStore(trustStorePath.get());
            setTrustStorePassword(vendorAmqConfig.getTrustStorePassword());
            setTrustStoreType("PKCS12");
        }
        Optional<String> keyStorePath = vendorAmqConfig.getKeyStorePath();
        if(keyStorePath.isPresent() && !keyStorePath.get().isEmpty()) {
            setKeyStore(keyStorePath.get());
            setKeyStorePassword(vendorAmqConfig.getKeyStorePassword());
        }
    }
    @SuppressWarnings("unused")
    public VendorActiveMQConnectionFactoryConfig() {
        // Default constructor
    }
}

package com.rafi.esb.infra.components.ibmmq;

import com.rafi.esb.infra.config.VendorIbmMqConfig;
        import com.ibm.mq.jakarta.jms.MQQueueConnectionFactory;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.jms.JMSException;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.Optional;

import static com.ibm.msg.client.jakarta.wmq.common.CommonConstants.*;

@Named("ibmConnectionFactoryVendor")
@ApplicationScoped
public class VendorIbmMQConnectionFactoryConfig extends MQQueueConnectionFactory {

    public VendorIbmMQConnectionFactoryConfig() {
    }
    @Inject
    public VendorIbmMQConnectionFactoryConfig(
            VendorIbmMqConfig vendorIbmMqConfig) throws JMSException, KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException, UnrecoverableKeyException, KeyManagementException {
        setHostName(vendorIbmMqConfig.getHostName());
        setPort(vendorIbmMqConfig.getPort());
        setQueueManager(vendorIbmMqConfig.getQueueManager());
        setChannel(vendorIbmMqConfig.getChannel());
        setTransportType(WMQ_CM_CLIENT);
        Optional<String> username = vendorIbmMqConfig.getUsername();
        // User name and password
        if (username.isPresent() && !username.get().isEmpty()) {
            setStringProperty(USERID, username.get());
        }
        Optional<String> password = vendorIbmMqConfig.getPassword();
        if (password.isPresent() && !password.get().isEmpty()) {
            setStringProperty(PASSWORD, password.get());
        }
        Optional<String> cipher = vendorIbmMqConfig.getCipher();
        Optional<String> trustStorePath = vendorIbmMqConfig.getTrustStorePath();
        // Create a custom SSL socket factory, condition for SSL: Cipher is present
        if (cipher.isPresent() && !cipher.get().isEmpty() && trustStorePath.isPresent() && !trustStorePath.get().isEmpty()){
            configureSSLContext(trustStorePath.get(), vendorIbmMqConfig.getTrustStorePassword(),
                    vendorIbmMqConfig.getKeyStorePath(), vendorIbmMqConfig.getKeyStorePassword(),
                    cipher.get());
        }
    }
    private void configureSSLContext(String trustStorePath, String trustStorePassword, Optional<String> keyStorePath, String keyStorePassword, String cipher) throws NoSuchAlgorithmException, KeyStoreException, IOException, CertificateException, UnrecoverableKeyException, KeyManagementException {
        SSLContext sslContext = SSLContext.getInstance("TLS");
        setSSLCipherSuite(cipher);
        // Trust store is basic for TLS
        KeyStore trustStore = KeyStore.getInstance("PKCS12");
        try (InputStream trustStoreStream = new FileInputStream(trustStorePath)) {
            trustStore.load(trustStoreStream, trustStorePassword.toCharArray());
        }
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(trustStore);
        // In cse of MTLS, key store is also needed
        KeyManager[] keyManagers = null;
        if (keyStorePath.isPresent() && !keyStorePath.get().isEmpty()) {
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            try (InputStream keyStoreStream = new FileInputStream(keyStorePath.get())) {
                keyStore.load(keyStoreStream, keyStorePassword.toCharArray());
            }
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(keyStore, keyStorePassword.toCharArray());
            keyManagers = keyManagerFactory.getKeyManagers();
        }
        sslContext.init(keyManagers, trustManagerFactory.getTrustManagers(), null);
        // Set the custom SSL socket factory
        setSSLSocketFactory(sslContext.getSocketFactory());
    }
}

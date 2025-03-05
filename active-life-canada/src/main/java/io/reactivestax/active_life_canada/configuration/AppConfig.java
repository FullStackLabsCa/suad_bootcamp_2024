package io.reactivestax.active_life_canada.configuration;

import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.socket.ConnectionSocketFactory;
import org.apache.hc.client5.http.socket.PlainConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.NoopHostnameVerifier;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.core5.http.config.Registry;
import org.apache.hc.core5.http.config.RegistryBuilder;
import org.apache.hc.core5.ssl.SSLContextBuilder;
import org.apache.hc.core5.util.TimeValue;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

@Configuration
public class AppConfig {

    @Bean
    public RestTemplate restTemplate() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException, IOException, UnrecoverableKeyException, CertificateException {
        RestTemplateBuilder builder = new RestTemplateBuilder();

        // Create an SSLContext that bypasses SSL validation
//        SSLContext sslContext = SSLContextBuilder.create()
//                .loadTrustMaterial((X509Certificate[] chain, String authType) -> true) // Trust all certificates
//                .build();

        SSLContext sslContext = SSLContextBuilder
                .create()
                .loadKeyMaterial(
                        new ClassPathResource("active-life-canada-keystore.jks").getFile(),
                        "changeit".toCharArray(),
                        "changeit".toCharArray())
                .loadTrustMaterial(
                        new ClassPathResource("active-life-canada-truststore.jks").getFile(),
                        "changeit".toCharArray())
                .build();

        // Create SSLConnectionSocketFactory with the SSLContext and NoopHostnameVerifier
        SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(
                sslContext, NoopHostnameVerifier.INSTANCE);

        // Create a registry of custom connection socket factories for both HTTP and HTTPS
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.INSTANCE)
                .register("https", sslSocketFactory)
                .build();

        // Create a connection manager using the custom registry
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        connectionManager.setMaxTotal(100);  // Set max total connections
        connectionManager.setDefaultMaxPerRoute(20);  // Set max connections per route
        connectionManager.setValidateAfterInactivity(TimeValue.ofSeconds(30));  // Validate connections after 30 seconds of inactivity

        // Create the CloseableHttpClient with the custom connection manager
        CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionManager(connectionManager)
                .evictIdleConnections(TimeValue.ofMinutes(5))  // Evict idle connections after 5 minutes
                .build();

        // Use HttpComponentsClientHttpRequestFactory to integrate the custom HttpClient with RestTemplate
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(httpClient);

        // Build the RestTemplate using the custom request factory
        return builder
                .requestFactory(() -> factory)
                .build();
    }

//    @Bean
//    public RestTemplate restTemplate(){
//        return new RestTemplate(new SkipSslFactory());
//    }
}




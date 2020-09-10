package com.example.resttemplateworkship;

import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.TrustStrategy;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.concurrent.ForkJoinPool;

@Configuration
public class ResttemplateConfiguration {

    @Value("${rest.connection.connectTimeout:30000}")
    private int connectTimeout;
    @Value("${rest.connection.readTimeout:30000}")
    private int readTimeout;
    @Value("${rest.disableCookieManagement:true}")
    private boolean disableCookieManagement;

    @Bean @Lazy
    ForkJoinPool threadPool() {
        return new ForkJoinPool(100);
    }

    @Bean
    @Qualifier("defaultRestTemplate")
    public RestTemplate defaultRestTemplate() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {

        TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;
        SSLContext sslContext = org.apache.http.ssl.SSLContexts.custom().loadTrustMaterial(null, acceptingTrustStrategy).build();
        SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext);
        HttpClientBuilder httpClientBuilder = HttpClients.custom().setSSLSocketFactory(csf);
        if (disableCookieManagement) {
            httpClientBuilder.disableCookieManagement();
        }
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setHttpClient(httpClientBuilder.build());
        requestFactory.setConnectTimeout(connectTimeout);
        requestFactory.setReadTimeout(readTimeout);

        return new RestTemplate(requestFactory);
    }

    @Bean
    AsyncRestTemplate asyncRestTemplate() {
        return new AsyncRestTemplate();
    }

    @Bean
    @Qualifier("advancedRestTemplate")
    public RestTemplate advancedRestTemplate() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {

        TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;
        SSLContext sslContext = org.apache.http.ssl.SSLContexts.custom().loadTrustMaterial(null, acceptingTrustStrategy).build();
        SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext);

        PoolingHttpClientConnectionManager connManager
                = new PoolingHttpClientConnectionManager();
        connManager.setDefaultMaxPerRoute(1000);
        connManager.setMaxTotal(1000);

        HttpClientBuilder httpClientBuilder = HttpClients.custom()
                .setSSLSocketFactory(csf)
                .setConnectionManager(connManager);
        if (disableCookieManagement) {
            httpClientBuilder.disableCookieManagement();
        }

        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setHttpClient(httpClientBuilder.build());
        requestFactory.setConnectTimeout(connectTimeout);
        requestFactory.setReadTimeout(readTimeout);

        return new RestTemplate(requestFactory);
    }
}

package com.hugme.plz.config;

import java.net.Socket;
import java.security.cert.X509Certificate; // 인증서 타입

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext; // SSLContext 생성
import javax.net.ssl.SSLEngine;
import javax.net.ssl.TrustManager; // TrustManager 인터페이스
import javax.net.ssl.X509ExtendedTrustManager;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import io.openvidu.java.client.OpenVidu;

@Configuration
@PropertySource("classpath:config/openvidu-config.properties")
public class OpenViduConfig {
    @Value("${openvidu.url}")
    private String URL;
    
    @Value("${openvidu.secret}")
    private String SECRET;

    @Bean
    public OpenVidu openViduBean() {
        try {
            // JVM 레벨에서 SSL 설정
            System.setProperty("jdk.tls.client.enableSessionTicketExtension", "false");
            System.setProperty("javax.net.ssl.trustStore", "");
            System.setProperty("javax.net.ssl.trustStorePassword", "");
            System.setProperty("com.sun.net.ssl.checkRevocation", "false");
            System.setProperty("sun.security.ssl.allowUnsafeRenegotiation", "true");
            
            // SSL 검증 비활성화
            disableSSLVerification();
            
            return new OpenVidu(URL, SECRET);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("OpenVidu 초기화 실패", e);
        }
    }
    
    private void disableSSLVerification() throws Exception {
        TrustManager[] trustAllCerts = new TrustManager[] {
            new X509ExtendedTrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType, Socket socket) {}
                
                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType, Socket socket) {}
                
                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType, SSLEngine engine) {}
                
                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType, SSLEngine engine) {}
                
                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType) {}
                
                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType) {}
                
                @Override
                public X509Certificate[] getAcceptedIssuers() { 
                    return new X509Certificate[0]; 
                }
            }
        };
        
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
        
        HostnameVerifier allHostsValid = (hostname, session) -> true;
        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
    }
}

////
//// Source code recreated from a .class file by IntelliJ IDEA
//// (powered by FernFlower decompiler)
////
//
//package io.reactivestax.active_life_canada.configuration;
//
//import java.io.IOException;
//import java.net.HttpURLConnection;
//import java.security.SecureRandom;
//import java.security.cert.X509Certificate;
//import javax.net.ssl.HostnameVerifier;
//import javax.net.ssl.HttpsURLConnection;
//import javax.net.ssl.KeyManager;
//import javax.net.ssl.SSLContext;
//import javax.net.ssl.SSLSession;
//import javax.net.ssl.SSLSocketFactory;
//import javax.net.ssl.TrustManager;
//import javax.net.ssl.X509TrustManager;
//import org.springframework.http.client.SimpleClientHttpRequestFactory;
//
//class SkipSslVerificationHttpRequestFactory extends SimpleClientHttpRequestFactory {
//    SkipSslVerificationHttpRequestFactory() {
//    }
//
//    protected void prepareConnection(HttpURLConnection connection, String httpMethod) throws IOException {
//        if (connection instanceof HttpsURLConnection httpsURLConnection) {
//            this.prepareHttpsConnection(httpsURLConnection);
//        }
//
//        super.prepareConnection(connection, httpMethod);
//    }
//
//    private void prepareHttpsConnection(HttpsURLConnection connection) {
//        connection.setHostnameVerifier(new SkipHostnameVerifier());
//
//        try {
//            connection.setSSLSocketFactory(this.createSslSocketFactory());
//        } catch (Exception var3) {
//        }
//
//    }
//
//    private SSLSocketFactory createSslSocketFactory() throws Exception {
//        SSLContext context = SSLContext.getInstance("TLS");
//        context.init((KeyManager[])null, new TrustManager[]{new SkipX509TrustManager()}, new SecureRandom());
//        return context.getSocketFactory();
//    }
//
//    private static final class SkipHostnameVerifier implements HostnameVerifier {
//        private SkipHostnameVerifier() {
//        }
//
//        public boolean verify(String s, SSLSession sslSession) {
//            return true;
//        }
//    }
//
//    private static final class SkipX509TrustManager implements X509TrustManager {
//        private SkipX509TrustManager() {
//        }
//
//        public X509Certificate[] getAcceptedIssuers() {
//            return new X509Certificate[0];
//        }
//
//        public void checkClientTrusted(X509Certificate[] chain, String authType) {
//        }
//
//        public void checkServerTrusted(X509Certificate[] chain, String authType) {
//        }
//    }
//}

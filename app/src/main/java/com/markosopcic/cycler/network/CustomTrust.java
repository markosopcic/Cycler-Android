package com.markosopcic.cycler.network;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.Arrays;
import java.util.Collection;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.CertificatePinner;
import okhttp3.OkHttpClient;
import okio.Buffer;

public class CustomTrust {

    public static OkHttpClient.Builder GetCustomTrustClient() {
        X509TrustManager trustManager;
        SSLSocketFactory sslSocketFactory;
        try {
            trustManager = trustManagerForCertificates(trustedCertificatesInputStream());
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{trustManager}, null);
            sslSocketFactory = sslContext.getSocketFactory();
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }

        return new OkHttpClient.Builder()
                .sslSocketFactory(sslSocketFactory, trustManager);
    }


    /**
     * Returns an input stream containing one or more certificate PEM files. This implementation just
     * embeds the PEM files in Java strings; most applications will instead read this from a resource
     * file that gets bundled with the application.
     */
    private static InputStream trustedCertificatesInputStream() {
        // PEM files for root certificates of Comodo and Entrust. These two CAs are sufficient to view
        // https://publicobject.com (Comodo) and https://squareup.com (Entrust). But they aren't
        // sufficient to connect to most HTTPS sites including https://godaddy.com and https://visa.com.
        // Typically developers will need to get a PEM file from their organization's TLS administrator.

        String entrustRootCertificateAuthority = "-----BEGIN CERTIFICATE-----\n" +
                "MIIFWTCCBEGgAwIBAgISA5C8+wGlMvQ/yXgKIi5M9WQjMA0GCSqGSIb3DQEBCwUA\n" +
                "MEoxCzAJBgNVBAYTAlVTMRYwFAYDVQQKEw1MZXQncyBFbmNyeXB0MSMwIQYDVQQD\n" +
                "ExpMZXQncyBFbmNyeXB0IEF1dGhvcml0eSBYMzAeFw0yMDA1MTAxMjA3MTVaFw0y\n" +
                "MDA4MDgxMjA3MTVaMBsxGTAXBgNVBAMTEGN5Y2xlci5ob3B0by5vcmcwggEiMA0G\n" +
                "CSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQDNW+nyEK+r2+SzXyXFrVW9/Teg5Fs1\n" +
                "t9JPH8tbrx1rw10hUTa4nQpNXRuqhyGraZi834TVRDAI2SIiW4RgP6v/reho95Wa\n" +
                "DpHlx9+E87nJP1a2dSVKRv2aoTMutp78OagYiVIUJRvyMquDI7HS1YXxvO4Amk+f\n" +
                "N5I17sAAwAKCWUe4z7o1jCKq5p49iJJhLLHlx8bS/MUE0EgQvs0gmgdaOgicgAYK\n" +
                "9pyILjpbMgbYp+l8NdsotS0fe4j+oce63ziB2YmIstwyKq02rj/so1nrHbYxXHMl\n" +
                "QjDaP4FfvRf/HwhzymSkfI4BT36826+GILE8GOpVMeFdp9JfTQMgFkCRAgMBAAGj\n" +
                "ggJmMIICYjAOBgNVHQ8BAf8EBAMCBaAwHQYDVR0lBBYwFAYIKwYBBQUHAwEGCCsG\n" +
                "AQUFBwMCMAwGA1UdEwEB/wQCMAAwHQYDVR0OBBYEFJ1hHXl0V34V2lTwOlr5JgdC\n" +
                "xe05MB8GA1UdIwQYMBaAFKhKamMEfd265tE5t6ZFZe/zqOyhMG8GCCsGAQUFBwEB\n" +
                "BGMwYTAuBggrBgEFBQcwAYYiaHR0cDovL29jc3AuaW50LXgzLmxldHNlbmNyeXB0\n" +
                "Lm9yZzAvBggrBgEFBQcwAoYjaHR0cDovL2NlcnQuaW50LXgzLmxldHNlbmNyeXB0\n" +
                "Lm9yZy8wGwYDVR0RBBQwEoIQY3ljbGVyLmhvcHRvLm9yZzBMBgNVHSAERTBDMAgG\n" +
                "BmeBDAECATA3BgsrBgEEAYLfEwEBATAoMCYGCCsGAQUFBwIBFhpodHRwOi8vY3Bz\n" +
                "LmxldHNlbmNyeXB0Lm9yZzCCAQUGCisGAQQB1nkCBAIEgfYEgfMA8QB2AF6nc/nf\n" +
                "VsDntTZIfdBJ4DJ6kZoMhKESEoQYdZaBcUVYAAABcf6yYIQAAAQDAEcwRQIgL3de\n" +
                "qAgEDWPc6CHcpCtWj8/epXeArbwikjF+yJjhWZYCIQCzwaV4h4Wa/79OH4C7f3j0\n" +
                "C95CbEoIl6BDzQUCO1tS4AB3AAe3XBvlfWj/8bDGHSMVx7rmV3xXlLdq7rxhOhpp\n" +
                "06IcAAABcf6yYK0AAAQDAEgwRgIhANWCtXm3ryo3qVnW33RMyHvULEdktOlGLzjM\n" +
                "vhTNvpOYAiEA3UYv1TA9zwiOhAAhwcgcFoj0wPxpv/ypa0uGYALMtUkwDQYJKoZI\n" +
                "hvcNAQELBQADggEBAFOou9yRH00jhuMJl6uMMHx7x0NMDpz6z9nXjdY8OiizRiwp\n" +
                "iNeGJP7WBTms8ndNheuuVuhUtIkPy+YwVZJnFY5QS7gQNyi000yQiszQE10+Dw8v\n" +
                "fRbZkRsT1YhVABc7c9XrSciqCas189r87yIZy9YWea1Gp5bK5CpcOZ1b9DIZJoTC\n" +
                "tQLURd5OpCXQB4ilXj6qcLL1XN0jhvll8kIbXeOtpQoz1MHOxoLowgCWIbuujgqm\n" +
                "7dVYL5Fplbxswptz3JO7M4asrYNiAIqhmIqYuIKQwQtHFWwwTVkalsxdEot7OIzI\n" +
                "VWWY3LdEM0PMYc2eRge75TALefK6GOP/CY4Fpno=\n" +
                "-----END CERTIFICATE-----";

        return new Buffer()
                .writeUtf8(entrustRootCertificateAuthority)
                .inputStream();
    }

    /**
     * Returns a trust manager that trusts {@code certificates} and none other. HTTPS services whose
     * certificates have not been signed by these certificates will fail with a {@code
     * SSLHandshakeException}.
     *
     * <p>This can be used to replace the host platform's built-in trusted certificates with a custom
     * set. This is useful in development where certificate authority-trusted certificates aren't
     * available. Or in production, to avoid reliance on third-party certificate authorities.
     *
     * <p>See also {@link CertificatePinner}, which can limit trusted certificates while still using
     * the host platform's built-in trust store.
     *
     * <h3>Warning: Customizing Trusted Certificates is Dangerous!</h3>
     *
     * <p>Relying on your own trusted certificates limits your server team's ability to update their
     * TLS certificates. By installing a specific set of trusted certificates, you take on additional
     * operational complexity and limit your ability to migrate between certificate authorities. Do
     * not use custom trusted certificates in production without the blessing of your server's TLS
     * administrator.
     */
    private static X509TrustManager trustManagerForCertificates(InputStream in)
            throws GeneralSecurityException {
        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
        Collection<? extends Certificate> certificates = certificateFactory.generateCertificates(in);
        if (certificates.isEmpty()) {
            throw new IllegalArgumentException("expected non-empty set of trusted certificates");
        }

        // Put the certificates a key store.
        char[] password = "password".toCharArray(); // Any password will work.
        KeyStore keyStore = newEmptyKeyStore(password);
        int index = 0;
        for (Certificate certificate : certificates) {
            String certificateAlias = Integer.toString(index++);
            keyStore.setCertificateEntry(certificateAlias, certificate);
        }

        // Use it to build an X509 trust manager.
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(
                KeyManagerFactory.getDefaultAlgorithm());
        keyManagerFactory.init(keyStore, password);
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(
                TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(keyStore);
        TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
        if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
            throw new IllegalStateException("Unexpected default trust managers:"
                    + Arrays.toString(trustManagers));
        }
        return (X509TrustManager) trustManagers[0];
    }

    private static KeyStore newEmptyKeyStore(char[] password) throws GeneralSecurityException {
        try {
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            InputStream in = null; // By convention, 'null' creates an empty key store.
            keyStore.load(in, password);
            return keyStore;
        } catch (IOException e) {
            throw new AssertionError(e);
        }
    }
}
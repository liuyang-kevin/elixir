package com.echoesnet.crowdfunding.net;


import com.echoesnet.crowdfunding.ElixirApplication;

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
import okio.Buffer;

public final class HttpsTrustManager {

    public final X509TrustManager trustManager;
    public final SSLSocketFactory sslSocketFactory;

    public HttpsTrustManager() {
        try {
//            trustManager = trustManagerForCertificates(
////                    ElixirApplication.getInstance().getAssets().open("file.crt")
//                    ElixirApplication.getInstance().getAssets().open("cChat.crt")
//            );
            trustManager = trustManagerForCertificates(trustedCertificatesInputStream());
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{trustManager}, null);
            sslSocketFactory = sslContext.getSocketFactory();
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns an input stream containing one or more certificate PEM files. This implementation just
     * embeds the PEM files in Java strings; most applications will instead read this from a resource
     * file that gets bundled with the application.
     */
    private InputStream trustedCertificatesInputStream() {
        String myCertificationAuthority = "-----BEGIN CERTIFICATE-----\n" +
                "MIICxzCCAa+gAwIBAgIEOksB4jANBgkqhkiG9w0BAQsFADAUMRIwEAYDVQQDEwlsb2NhbGhvc3Qw\n" +
                "HhcNMTcxMjExMDUxNTI3WhcNMTgxMjExMDUxNTI3WjAUMRIwEAYDVQQDEwlsb2NhbGhvc3QwggEi\n" +
                "MA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQCAqtDmZ2DDEEb3hSjonl0kAxoWO4TTHdetmAic\n" +
                "DHm87/XW5gXHUjMxfaMl5XiyY+vBcfD05empsFQluk/Niv+4RCNOYcQES0DD8WmBQu2mluJUukVV\n" +
                "NIDkJ2vfbitjtUNbsPCX6TUTSs323CIYoRQHqXmtap9ZDtbHuiwQ3uoFYMiTIYxknn85qlwHHW3W\n" +
                "sC1GFUl+FDeYh7oWyYbfssCkYe4oJl0FDwfNJ9LR0NEoGZHYgXpldsP2dSpcgOipgZnm0IIbYHvf\n" +
                "MCfqspsr92RwFOehs6dLYXkUXk1jW+x5KUG7gxaliwLsg3GG03wh17TghrZZ+dLqz7dPieuE2Y2/\n" +
                "AgMBAAGjITAfMB0GA1UdDgQWBBRwbfBBWdVPb7lc2I4+c/EVaI/z4TANBgkqhkiG9w0BAQsFAAOC\n" +
                "AQEACwGgUhE8OmzkiqNRUodzqr3w9i0xMWGH8tW4QoZmzARzfVkwamg8yqRdiTT2dvgSze/FLIj+\n" +
                "+uYaMDBDN8FmXm2rxpG/D/rISqV3cV+jV5r9bfI8IaT/7hrWGPvW8srHemER5kdJtWTC09g+PaBz\n" +
                "kThwBdpyxEVkiWXQUtR8UaISzqIQU9qHlJZQesDoDi+pYCcaiwcgIajUBoKQvNiZSnEdRk2HhoxN\n" +
                "zpvYyBAWh7Y+bXxpmIv/IzjYE1XzwezDF89GOiq0I1/PdKDAta5TTmYWiAoMoH6vldHAG0klzv/n\n" +
                "2EyDehy7ro5x2z0L5jpn3SwN+sdErcssm9riV/CflQ==\n" +
                "-----END CERTIFICATE-----";
        return new Buffer()
                .writeUtf8(myCertificationAuthority)
                .inputStream();
    }

    /**
     * Returns a trust manager that trusts {@code certificates} and none other. HTTPS services whose
     * certificates have not been signed by these certificates will fail with a {@code
     * SSLHandshakeException}.
     * <p>
     * <p>This can be used to replace the host platform's built-in trusted certificates with a custom
     * set. This is useful in development where certificate authority-trusted certificates aren't
     * available. Or in production, to avoid reliance on third-party certificate authorities.
     * <p>
     * <p>See also {@link CertificatePinner}, which can limit trusted certificates while still using
     * the host platform's built-in trust store.
     * <p>
     * <h3>Warning: Customizing Trusted Certificates is Dangerous!</h3>
     * <p>
     * <p>Relying on your own trusted certificates limits your server team's ability to update their
     * TLS certificates. By installing a specific set of trusted certificates, you take on additional
     * operational complexity and limit your ability to migrate between certificate authorities. Do
     * not use custom trusted certificates in production without the blessing of your server's TLS
     * administrator.
     */
    private X509TrustManager trustManagerForCertificates(InputStream in)
            throws GeneralSecurityException {
        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
        Collection<? extends Certificate> certificates = certificateFactory.generateCertificates(in);
        if (certificates.isEmpty()) {
            throw new IllegalArgumentException("expected non-empty set of trusted certificates");
        }

        // Put the certificates a key store.
//        char[] password = "password".toCharArray(); // Any password will work.
        char[] password = "123456".toCharArray(); // Any password will work.
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

    private KeyStore newEmptyKeyStore(char[] password) throws GeneralSecurityException {
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

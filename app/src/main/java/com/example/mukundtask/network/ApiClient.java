package com.example.mukundtask.network;

import java.security.cert.CertificateException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Mukund, mkndmail@gmail.com on 04-06-2020.
 */
public class ApiClient {

    public static volatile Retrofit mRetrofit;
    //Creates a Thread safe retrofit client if it's null else create one
    public static Retrofit getApiClient() {
        if (mRetrofit == null) {
            synchronized (ApiClient.class) {
                String BASE_URL = "https://devfrontend.gscmaven.com/wmsweb/webapi/";
                mRetrofit = new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .client(createClient().build())
                        .addConverterFactory(GsonConverterFactory
                                .create())
                        .build();
            }
        }
        return mRetrofit;
    }


//    This provides a certificate for Handshake over ssl.
    public static OkHttpClient.Builder createClient() {

        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
            builder.hostnameVerifier((hostname, session) -> true);
            return builder;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

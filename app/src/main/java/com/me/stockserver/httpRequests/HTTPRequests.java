package com.me.stockserver.httpRequests;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManagerFactory;


public class HTTPRequests extends AsyncTask<HTTPRequestParams, Void, HTTPRequestParams[]> {
    public static SSLContext sslContext;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    protected HTTPRequestParams[] doInBackground(HTTPRequestParams...params) {
        try {
            URL url = new URL("https://192.168.0.21:443" +params[0].url);
            //URL url = new URL("https://73.161.247.253:443" +params[0].url);
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setSSLSocketFactory(sslContext.getSocketFactory());
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json; utf-8");
            connection.setRequestProperty("Accept", "application/json");
            connection.setConnectTimeout(50000);
            connection.setReadTimeout(50000);
            connection.setHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    if (hostname.equals("73.161.247.253") || hostname.equals("192.168.0.21")){
                        return true;
                    } else {
                        return false;
                    }
                    //return hv.verify("73.161.247.253:443", session);
                }
            });

            if (params[0].send != null) {
                String jsonInputString = params[0].send.toString();
                try (OutputStream os = connection.getOutputStream()) {
                    byte[] input = jsonInputString.getBytes("utf-8");
                    os.write(input, 0, input.length);
                }
            }
            try(BufferedReader br = new BufferedReader(
                    new InputStreamReader(connection.getInputStream(), "utf-8"))) {
                        StringBuilder response = new StringBuilder();
                        String responseLine = null;
                        while ((responseLine = br.readLine()) != null) {
                            response.append(responseLine.trim());
                        }
                        params[0].ret = response.toString();
            }

            return params;
        } catch (Exception e) {
            params[0].ret = e.toString();
            return params;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    protected void onPostExecute(HTTPRequestParams[] result) {
        // this is executed on the main thread after the process is over
        // update your UI here
        if (result[0].f != null) {
            result[0].f.accept(result[0]);
        }
    }
}

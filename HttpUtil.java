package com.dolnikova;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class HttpUtil {

    public static String sendRequest(@NotNull String url, @Nullable Map<String, String> headers, @Nullable String body) {

        String result = null;
        HttpURLConnection urlConnection = null;

        try {
            URL requestUrl = new URL(url);
            urlConnection = (HttpURLConnection) requestUrl.openConnection();

            urlConnection.setConnectTimeout(20000);
            urlConnection.setReadTimeout(20000);

            urlConnection.setRequestMethod("GET");

            if (body != null && body.length() > 0) {
                urlConnection.setDoOutput(true);
                urlConnection.setRequestMethod("POST");
                DataOutputStream outputStream = new DataOutputStream(urlConnection.getOutputStream());
                outputStream.writeBytes(body);
                outputStream.flush();
                outputStream.close();
            }

            if (headers != null) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    urlConnection.addRequestProperty(entry.getKey(), entry.getValue());
                }
            }

            int status = urlConnection.getResponseCode();

            if (status == HttpURLConnection.HTTP_OK) {

                StringBuilder res = new StringBuilder();
                res.append("USD");

                String strBody = getStringFromStream(urlConnection.getInputStream());

                int indexFirst = strBody.indexOf("USD");
                int indexFirstValue = strBody.indexOf(":", indexFirst);
                int indexLastValue = strBody.indexOf(",", indexFirstValue);

                for (int i = indexFirstValue; i <= indexLastValue - 1; i++) {
                    res.append(strBody.charAt(i));
                }

                result = res.toString();
            }
        } catch (Exception e) {
            System.out.println("sendRequest failed " + e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        return result;
    }

    public static String getStringFromStream(InputStream inputStream) throws IOException {
        final int BUFFER_SIZE = 4096;
        ByteArrayOutputStream resultStream = new ByteArrayOutputStream(BUFFER_SIZE);
        byte[] buffer = new byte[BUFFER_SIZE];
        int length;
        while ((length = inputStream.read(buffer)) != -1) {
            resultStream.write(buffer, 0, length);
        }
        return resultStream.toString("UTF-8");
    }
}


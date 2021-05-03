package de.spanier.microcar.client;

import org.glassfish.jersey.server.Uri;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.Charset;

public class RestClientToMicrocontroller {

    public RestClientToMicrocontroller() {
    }

    public void putForward(boolean isForward) {
        try {
            URL url = new URL("http://localhost:800");
            JSONObject jsonObject = new JSONObject(isForward);
            //TODO build json file properly
            sendJSON(null, url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public void putPosition(int position) {
        try {
            URL url = new URL("http://localhost:800");
            JSONObject jsonObject = new JSONObject(position);
            //TODO build json file properly
            sendJSON(null, url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }


    private void sendJSON(String jsonText, URL url) {
        HttpURLConnection httpURLConnection = null;
        try {
            //URL url = new URL(getString(R.string.base_url) + "/geoData");
            httpURLConnection = (HttpURLConnection) url.openConnection();
            // Verbindung konfigurieren
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestMethod("POST");
            byte[] data = jsonText.getBytes();
            httpURLConnection.setRequestProperty("Content-Type",
                    "application/json; charset="
                            + Charset.defaultCharset().name());
            httpURLConnection.setFixedLengthStreamingMode(data.length);
            // Daten senden
            httpURLConnection.getOutputStream().write(data);
            httpURLConnection.getOutputStream().flush();
            String contentType = httpURLConnection.getContentType();

            int responseCode = httpURLConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {

            } else {
                //Log.d("TAG", "responseCode: " + responseCode);
            }


        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }
    }
}

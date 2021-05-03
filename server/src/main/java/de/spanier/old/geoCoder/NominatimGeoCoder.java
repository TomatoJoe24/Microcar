package de.spanier.old.geoCoder;

import de.spanier.old.model.GeoData;
import de.spanier.old.interfaces.GeoCoder;
import de.spanier.old.interfaces.JSONParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Implementierung des GeoCoder Interfaces mithilfe der Nominatim API
 */
public class NominatimGeoCoder implements GeoCoder {

    private static final String USER_AGENT = "Mozilla/5.0";

    @Override
    public GeoData reverseEncode(GeoData geoData) {
        //TODO: EMail raus nehmen
        String reverseGeoCodeUrl = "https://nominatim.openstreetmap.org/reverse?format=jsonv2&lat="+ geoData.getLat() + "&lon=" + geoData.getLng() + "&email=thomas.spanier24@gmail.com";

        try {
            // Creating an object of URL Class
            URL obj = new URL(reverseGeoCodeUrl);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            // optional default is GET
            con.setRequestMethod("GET");

            // add request header
            con.setRequestProperty("User-Agent", USER_AGENT);

            // Print this response code if you are facing any issues
            int responseCode = con.getResponseCode();
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            // Getting the response in String
            String jsonobj = response.toString();

            JSONParser parser = new NominatimJSONParser();
            geoData.setAddress(parser.parseGeoData(jsonobj));
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return geoData;

    }
}

package de.spanier.old.geoCoder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import de.spanier.old.model.GeoData;
import de.spanier.old.interfaces.GeoCoder;
import de.spanier.old.interfaces.JSONParser;

public class HereGeoCoder implements GeoCoder {
    private final String appID = "0lScSI5DzNhwh4ZBBWJc";
    private final String appCode = "cqrQ5l7kU22-vc15nBQ4CQ";
    private static final String USER_AGENT = "Mozilla/5.0";

    public HereGeoCoder(){

    }

    public GeoData reverseEncode(GeoData geoData) {
        try {
            //AppID und AppCode raus nehmen
            String reverseGeoCodeUrl = "https://reverse.geocoder.api.here.com/6.2/reversegeocode.json?prox=" + geoData.getLat()
                    + "," + geoData.getLng() + "&mode=retrieveAddresses" + "&app_id=" + appID
                    + "&app_code=" + appCode;

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
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            // Getting the response in String
            String jsonobj = response.toString();
            JSONParser parser = new HereJSONParser();
            geoData.setAddress(parser.parseGeoData(jsonobj));

            return geoData;
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            return geoData;
        }
    }
}

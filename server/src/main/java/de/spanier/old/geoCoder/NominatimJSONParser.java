package de.spanier.old.geoCoder;

import org.json.JSONObject;

import de.spanier.old.interfaces.JSONParser;

public class NominatimJSONParser implements JSONParser {

    @Override
    public String parseGeoData(String jsonObject) {
        JSONObject json = new JSONObject(jsonObject);
        JSONObject address = json.getJSONObject("address");
        String name = json.getString("display_name");

        return name;
    }
}

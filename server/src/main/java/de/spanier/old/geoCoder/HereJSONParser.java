package de.spanier.old.geoCoder;

import org.json.JSONArray;
import org.json.JSONObject;

import de.spanier.old.interfaces.JSONParser;

public class HereJSONParser implements JSONParser {
    @Override
    public String parseGeoData(String jsonObject){

        JSONObject json = new JSONObject(jsonObject);
        JSONObject response1 = json.getJSONObject("Response");
        JSONArray View = response1.getJSONArray("View");
        JSONObject obj1 = View.getJSONObject(0);
        JSONArray result = obj1.getJSONArray("Result");
        JSONObject obj2 = result.getJSONObject(0);
        JSONObject location = obj2.getJSONObject("Location");
        JSONObject address = location.getJSONObject("Address");

        String label = address.getString("Label");

        // print result
        //System.out.println(label);
        return label;
    }
}

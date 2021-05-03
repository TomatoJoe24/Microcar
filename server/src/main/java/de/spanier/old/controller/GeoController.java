package de.spanier.old.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import de.spanier.old.model.GeoData;
import de.spanier.old.database.Cluster;
import de.spanier.old.database.SQLiteConnector;
import de.spanier.old.interfaces.DBConnector;
import de.spanier.old.interfaces.GeoCoder;
import de.spanier.old.geoCoder.HereGeoCoder;
import de.spanier.old.model.GeoDataString;

@RestController
    public class GeoController {


    /**
     * REST-PUT Aufruf, um geoData zu empfangen.
     */
    @RequestMapping(value = "geoData", consumes = "application/json")
    public void putGeoData(@RequestBody GeoDataString gds){
        GeoData geoData = gds.toGeoData();
        GeoCoder geoCoder = new HereGeoCoder();
        geoData = geoCoder.reverseEncode(geoData);
        DBConnector connector = new SQLiteConnector();
        connector.insertGeoData(geoData);
        System.out.println("Saved Data: " + geoData.getAddress());
    }
    /*@RequestMapping(value = "geoData", consumes = "application/json")
    public void putGeoData(@RequestBody GeoData geoData){
        //GeoData geoData = gds.toGeoData();
        GeoCoder geoCoder = new NominatimGeoCoder();
        geoData = geoCoder.reverseEncode(geoData);
        DBConnector connector = new SQLiteConnector();
        connector.insertGeoData(geoData);
        System.out.println("Saved Data: " + geoData.getAddress());
    }*/


    @GetMapping("/avgPPM")
    public float getAveragePPM() {
        DBConnector connector = new SQLiteConnector();
        return connector.getAveragePPM();
    }

    @GetMapping("/minPPM")
    public float getMinPPM() {
        DBConnector connector = new SQLiteConnector();
        return connector.getMinPPM();
    }

    @GetMapping("/maxPPM")
    public float getMaxPPM() {
        DBConnector connector = new SQLiteConnector();
        return connector.getMaxPPM();
    }

    /**
     * REST-GET Aufruf, um die geoDaten zum Client zu senden
     * @return eine Liste an geoDaten
     */
    @GetMapping(value="values")
    public List<GeoData> getAllValues(){
        return new SQLiteConnector().getAllValues();
    }


    @RequestMapping("/localValues")
    public List<GeoData> getLocalValues(@RequestParam float lat, float lng, int zoom, int accuracy) {
        return Cluster.getClusteredList( lat, lng, zoom, accuracy);
    }


}

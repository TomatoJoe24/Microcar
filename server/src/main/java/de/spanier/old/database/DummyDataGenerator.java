package de.spanier.old.database;

import java.util.Date;
import java.util.Random;

import de.spanier.old.model.GeoData;

/**
 * Klasse, um zufällige GeoData zu erzeugen.
 * Nur für Testzwecke
 */
public class DummyDataGenerator {

    private final static String path = "jdbc:sqlite:Data/data.db";

    public static void generateRandomData(int number) {
        final float minLat = 47.36831f, minLng = 5.992315f, maxLat = 54.884793f, maxLng= 14.73377f;
        float randomLat, randomLng;

        SQLiteConnector connector = new SQLiteConnector();
        connector.connect(path);

        Random r = new Random();
        for(int i = 0; i < number; i++){
            randomLat = minLat + r.nextFloat() * (maxLat - minLat);
            randomLng = minLng + r.nextFloat() * (maxLng - minLng);
            GeoData rndGeoData = new GeoData(randomLat, randomLng, new Date(), 2, "mac");
            connector.insertGeoDataWithoutDisconnecting(rndGeoData);

        }
        connector.disconnect();
    }
}

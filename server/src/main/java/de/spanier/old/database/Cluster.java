package de.spanier.old.database;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import de.spanier.old.model.GeoData;
import de.spanier.old.interfaces.DBConnector;


/**
 * Dient zum Clustern der Daten
 * Damit nicht alle Daten immer zum Client gesendet werden
 */
public class Cluster {

    public static List<GeoData> getClusteredList(float centerLat, float centerLng, int zoom, int accuracy){
        DBConnector connector = new SQLiteConnector();
        List<GeoData> clusteredList = new LinkedList<>();
        List<GeoData> localValues;//= new LinkedList<>();
        float deltaLat = 0, deltaLng = 0, minLat, minLng, localMinLat, localMinLng, localMaxLat, localMaxLng; //maxLat, maxLng,

        switch (zoom) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
                break;
            case 5:
                deltaLat = 12;
                deltaLng = 42;
                break;
            case 6:
                deltaLat = 6;
                deltaLng = 21;
                break;
            case 7:
                deltaLat = 3;
                deltaLng = 11;
                break;
            case 8:
                deltaLat = 1.5f;
                deltaLng = 6;
                break;
            case 9:
                deltaLat = 0.8f;
                deltaLng = 3;
                break;
            case 10:
                deltaLat = 0.4f;
                deltaLng = 1.5f;
                break;
            case 11:
                deltaLat = 0.2f;
                deltaLng = 0.7f;
                break;
            case 12:
                deltaLat = 0.1f;
                deltaLng = 0.35f;
                break;
            case 13:
                deltaLat = 0.05f;
                deltaLng = 0.2f;
                break;
            case 14:
                deltaLat = 0.03f;
                deltaLng = 0.1f;
                break;
            case 15:
                deltaLat = 0.015f;
                deltaLng = 0.05f;
                break;
            case 16:
            case 17:
            case 18:
            case 19:
            case 20:
                deltaLat = 0.008f;
                deltaLng = 0.03f;
                break;
            default:
                deltaLat = 0;
                deltaLng = 0;
        }

        minLat = centerLat - deltaLat;
        //maxLat = centerLat + deltaLat;
        minLng = centerLng - deltaLng;
        //maxLng = centerLng + deltaLng;
        //TODO: testen!! (und in eigene Methode auslagern, Rückgabe als Array "bounds")

        localMinLat = minLat;
        localMinLng = minLng;
        //Baue Matrix auf und bestimme für jede Zelle einen Cluster-Wert
        for(int i = 1; i <= accuracy; i++) {
            localMaxLat = minLat + (2 * deltaLat) * i / accuracy;
            for(int j = 1; j <=accuracy; j++) {
                localMaxLng = minLng + (2 * deltaLng) * j / accuracy;

                localValues = connector.getValuesInRange(localMinLat, localMaxLat, localMinLng,localMaxLng);
                /*
                    TODO: Testen!
                    temp -> Cluster the Values:
                    Lat, Lng = 1. Wert,
                    Datum = neuster Wert
                    ppm = Mittelwert

                 */

                if( localValues.size() > 0 ) {
                    //System.out.println(localValues.size());
                    GeoData clusteredGeoData = localValues.get(0);
                    AtomicReference<Float> allPPM = new AtomicReference<>(0f);

                    localValues.forEach(g -> {
                        //TODO refresh allValues, Date
                        //System.out.println(g.getPpm());
                        allPPM.set(allPPM.get() + g.getPpm());
                        //System.out.println("c: " + allPPM);
                    });
                    clusteredGeoData.setPpm(allPPM.get() / localValues.size());


                    //clusteredGeoData.setPpm(clusteredGeoData.getPpm() - localValues.get(0).getPpm());

                    //clusteredGeoData.setPpm(clusteredGeoData.getPpm() / localValues.size() );
                    //Falls mehr als 1 Value in Zelle der Matrix, wird er als clustered markiert
                    if(localValues.size() > 1) {
                        clusteredGeoData.setClustered(true);
                    }

                    clusteredList.add(clusteredGeoData);

                }

                localMinLng = localMaxLng;
            }
            localMinLat = localMaxLat;

        }


        return clusteredList;
    }


}

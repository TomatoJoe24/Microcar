package de.spanier.old.interfaces;

import java.util.List;

import de.spanier.old.model.GeoData;

public interface DBConnector {
    public boolean connect(String path);
    public boolean insertGeoData(GeoData geodata);
    public List<GeoData> getAllValues();
    public List<GeoData> getValuesInRange(float minLat, float maxLat, float minLng, float maxLng);
    public float getAveragePPM();
    public float getMinPPM();
    public float getMaxPPM();
}

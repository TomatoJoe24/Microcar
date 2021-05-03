package de.spanier.old.interfaces;

import de.spanier.old.model.GeoData;

public interface GeoCoder {
    /**
     * Dient zum Finden der Adresse anhand gegebener Koordinaten
     * @param geoData ohne Adresse
     * @return GeoData mit Adresse
     */
    public GeoData reverseEncode(GeoData geoData);
}

package de.spanier.old.model;

import de.spanier.common.Utils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
public class GeoDataString {
    @NonNull
    float lat;
    @NonNull
    float lng;
    @NonNull
    String date;
    @NonNull
    float ppm;
    String address;
    @NonNull
    String mac;
    boolean deprecatedValue = false;
    @NonNull
    boolean clustered = false;

    public GeoData toGeoData() {
        GeoData geoData = new GeoData(lat, lng, Utils.stringToDate(date), ppm,  mac);

        return geoData;
    }


}

package de.spanier.old.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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
public class GeoData {

    public GeoData(float lat, float lng, String dateString, float ppm, String mac) {
        this.lat = lat;
        this.lng = lng;
        this.date = Utils.stringToDate(dateString);
        this.ppm = ppm;
        this.mac = mac;
    }

    @NonNull
    float lat;
    @NonNull
    float lng;
    @NonNull
    Date date;
    @NonNull
    float ppm;
    String address;
    @NonNull
    String mac;
    boolean deprecatedValue = false;
    @NonNull
    boolean clustered = false;

    public String dateToString() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss", Locale.GERMAN);
        return dateFormat.format(date);
    }

    public String getAddress() {
        return address;
    }
}

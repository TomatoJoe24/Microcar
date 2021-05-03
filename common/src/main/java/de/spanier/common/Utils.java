package de.spanier.common;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Utils {

    public static float distance (float lat1, float lon1, float lat2, float lon2, String unit) {
        if ((lat1 == lat2) && (lon1 == lon2)) {
            return 0;
        }
        else {
            float theta = lon1 - lon2;
            double dist = Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2)) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(theta));
            dist = Math.acos(dist);
            dist = Math.toDegrees(dist);
            dist = dist * 60 * 1.1515;
            if (unit == "K") {
                dist = dist * 1.609344;
            } else if (unit == "N") {
                dist = dist * 0.8684;
            }
            return (float) dist;
        }
    }

    public static Date stringToDate(String dateString) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss", Locale.GERMAN);
        try {
            Date date = formatter.parse(dateString);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

}

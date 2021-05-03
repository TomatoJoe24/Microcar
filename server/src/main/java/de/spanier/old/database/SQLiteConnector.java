package de.spanier.old.database;

import de.spanier.common.Utils;
import de.spanier.old.model.GeoData;
import de.spanier.old.interfaces.DBConnector;
import java.sql.*;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Schnittstelle zur SQLite Datenbank
 */
public class SQLiteConnector implements DBConnector {

    private Connection conn;
    private final String path = "jdbc:sqlite:Data/data.db";

    /**
     * Öffnet eine Datenbank-Verbindung
     * @param path der Pfad zur Datenbank
     * @return Rückmeldung, ob die Verbindung fehlerlos funktioniert
     */
    @Override
    public boolean connect(String path){
        try{
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:Data/data.db");

            return true;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;

    }

    /**
     * Beendet die bestehende Verbindung
     * @return Rückmeldung, ob die Verbindung beendet werden konnte
     */
    public boolean disconnect(){
        try {
            if(conn != null) {
                conn.close();
                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            return false;
        }
    }

    /**
     * Dient um die geoData aus der Datenbank zu laden
     * @return eine Liste mit allen in der Datenbank gespeicherten geoData
     */
    @Override
    public List<GeoData> getAllValues() {
        List<GeoData> list = new LinkedList<GeoData>();

        try {
            //Baue Verbindung auf
            connect(path);
            Statement stmt = conn.createStatement();
            conn.setAutoCommit(false);

            String sql = "SELECT * FROM aqData WHERE deprecatedValue = 'false'";
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()){
                //Speichere Rohdaten in GeoData
                int id  = rs.getInt("id");
                float lat = rs.getFloat("lat");
                float lng = rs.getFloat("lng");
                Date date = Utils.stringToDate(rs.getString("date"));
                float ppm = rs.getFloat("ppm");
                String address = rs.getString("address");
                String mac = rs.getString("mac");

                GeoData geoData = new GeoData(lat, lng, date, ppm, mac);
                geoData.setAddress(address);

                //Füge Daten zur Liste hinzu
                list.add(geoData);
            }
            //Führe Statement aus und beende die Verbindung
            rs.close();
            stmt.close();
            conn.commit();
            disconnect();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;

    }



    /**
     * Dient um geoData innerhalb eines Feldes aus der Datenbank zu laden
     * @return eine Liste mit allen in der Datenbank gespeicherten geoData
     */
    @Override
    public List<GeoData> getValuesInRange(float minLat, float maxLat, float minLng, float maxLng) {
        List<GeoData> list = new LinkedList<GeoData>();

        try {
            //Baue Verbindung auf
            connect(path);
            //TODO: Umbauen, um Copy-Paste zu verhindern

            Statement stmt =conn.createStatement();
            conn.setAutoCommit(false);

            StringBuilder sb = new StringBuilder();
            sb.append("SELECT * FROM aqData WHERE (lat BETWEEN ").append(minLat).append(" AND ").append(maxLat)
                    .append(") AND (lng BETWEEN ").append(minLng).append(" AND ").append(maxLng).append(") AND deprecatedValue = 'false'");


            String sql = sb.toString();

            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()){
                //Speichere Rohdaten in GeoData
                //int id  = rs.getInt("id");
                float lat = rs.getFloat("lat");
                float lng = rs.getFloat("lng");
                Date date = Utils.stringToDate(rs.getString("date"));
                float ppm = rs.getFloat("ppm");
                String address = rs.getString("address");
                String mac = rs.getString("mac");

                GeoData geoData = new GeoData(lat, lng, date, ppm, mac);
                geoData.setAddress(address);

                //Füge Daten zur Liste hinzu
                list.add(geoData);
            }
            //Führe Statement aus und beende die Verbindung
            rs.close();
            stmt.close();
            conn.commit();
            disconnect();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;

    }



    /**
     * Speichere GeoData in der Datenbank
     * @param geodata die zu speichernde GeoData
     * @return Rückmeldung, ob die GeoData in der Datenbank gespeichert werden konnte
     */
    @Override
    public boolean insertGeoData(GeoData geodata) {

        try {
            connect(path);
            Statement stmt = conn.createStatement();
            conn.setAutoCommit (false);
            //TODO: Use preparedStatement
            String sql = "INSERT INTO aqData (lat, lng, date, ppm, address, mac, deprecatedValue) VALUES (" +
                    geodata.getLat() + ", " +
                    geodata.getLng() + ", '" +
                    geodata.dateToString() + "', " +
                    geodata.getPpm() + ", '" +
                    geodata.getAddress() + "', '" +
                    geodata.getMac() + "', '" +
                    geodata.isDeprecatedValue() + "'); ";
            stmt.executeUpdate(sql);
            stmt.close();
            conn.commit();
            disconnect();

            return true;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return false;

        }
    }

    /**
     * Only for testing
     * @param geodata
     * @return
     */
    public boolean insertGeoDataWithoutDisconnecting(GeoData geodata) {

        try {
            //connect(path);
            Statement stmt = conn.createStatement();
            conn.setAutoCommit (false);

            String sql = "INSERT INTO aqData (lat, lng, date, ppm, address, mac, deprecatedValue) VALUES (" +
                    geodata.getLat() + ", " +
                    geodata.getLng() + ", '" +
                    geodata.dateToString() + "', " +
                    geodata.getPpm() + ", '" +
                    geodata.getAddress() + "', '" +
                    geodata.getMac() + "', '" +
                    geodata.isDeprecatedValue() + "'); ";

            stmt.executeUpdate(sql);
            stmt.close();
            conn.commit();
            //disconnect();

            return true;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return false;

        }
    }

    public float getAveragePPM() {
        float average = 0;
        String sql = "SELECT avg(ppm) FROM aqData";
        try {
            connect(path);
            Statement stmt = conn.createStatement();
            conn.setAutoCommit (false);
            ResultSet rs = stmt.executeQuery(sql);
           if(rs.next()){
                average = rs.getFloat(1);
            }
            stmt.close();
            conn.commit();
            disconnect();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return average;
    }

    public float getMinPPM() {
        float min = 0;
        String sql = "SELECT min(ppm) FROM aqData";
        try {
            connect(path);
            Statement stmt = conn.createStatement();
            conn.setAutoCommit (false);
            ResultSet rs = stmt.executeQuery(sql);
            if(rs.next()){
                min = rs.getFloat(1);
            }
            stmt.close();
            conn.commit();
            disconnect();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return min;
    }

    public float getMaxPPM() {
        float max = 0;
        String sql = "SELECT max(ppm) FROM aqData";
        try {
            connect(path);
            Statement stmt = conn.createStatement();
            conn.setAutoCommit (false);
            ResultSet rs = stmt.executeQuery(sql);
            if(rs.next()){
                max = rs.getFloat(1);
            }
            stmt.close();
            conn.commit();
            disconnect();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return max;
    }

}

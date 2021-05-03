package de.spanier.microcar.connection.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.util.Log;

import de.spanier.microcar.connection.IControllerConnection;
import de.spanier.microcar.util.LightColor;

public class ControllerConnection implements IControllerConnection {

    private LightColor color;
    private BluetoothAdapter adapter;
    private BluetoothDevice btDevice;
    public static String deviceName = "";
    public static String deviceMac = "";

    public ControllerConnection() {
        super();
        adapter = BluetoothAdapter.getDefaultAdapter();

        color = LightColor.OFF;
    }

    @Override
    public boolean connect(String btName) {
        if (deviceName == "") {
            return false;
        }
        btDevice = adapter.getRemoteDevice(deviceMac);
        //adapter.
        return false;
    }

    @Override
    public boolean disconnect() {
        return false;
    }

    @Override
    public int getTurnPosition() {
        Log.e("Controller", "Got Position");
        return 90;
    }

    @Override
    public void setTurnPosition(int position) {
        Log.e("Controller", "Turned to "+ position);
    }

    @Override
    public void start() {
        Log.e("Controller", "Motor started");
    }

    @Override
    public void stop() {
        Log.e("Controller", "Motor stopped");
    }

    public void backwards() {
        Log.e("Controller", "Motor started backwards");
    }

    @Override
    public void setFlashLight(LightColor color) {
        Log.e("Controller", "Changed Light to " + color.name());
        this.color = color;
    }

    @Override
    public LightColor getFlashLight() {
        Log.e("Controller", "Get Light Color");
        return color;
    }

    @Override
    public float getTemperature() {
        return 20;
    }

    @Override
    public float getHumidity() {
        return 20;
    }
}

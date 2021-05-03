package de.spanier.microcar.connection.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.util.UUID;

public class MyBluetoothService {
    private BluetoothAdapter adapter;
    private BluetoothDevice btDevice;
    private BluetoothSocket socket;
    public static String deviceName = "";
    public static String deviceMac = "";


    //region Initialization
    //==============================================================================================
    public MyBluetoothService() {
        initialize();
    }

    public void initialize() {
        adapter = BluetoothAdapter.getDefaultAdapter();
        if(deviceMac != "") {
            btDevice = adapter.getRemoteDevice(deviceMac);
            try {
                socket = btDevice.createInsecureRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
                adapter.cancelDiscovery();
                socket.connect();
                Log.e("Connection: ", "Connected");
            } catch (IOException e) {
                Log.e("Connection: ", "Failed to connect");
                e.printStackTrace();
            }
        } else {
            Log.e("Connection: ", "No Device Selected");
        }
    }

    //region Connection
    //==============================================================================================
    public void connect() {

    }

    public void disconnect() {
        try {
            socket.close();
            adapter.startDiscovery();
            Log.e("Connection", "Disconnected");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isConnected() {
        if(socket == null) {
            return false;
        }
        return socket.isConnected();
    }

    //region send/receive
    //==============================================================================================
    public void sendToDevice(String msg) {

    }

    public String receiveFromDevice() {
        return "";
    }

}

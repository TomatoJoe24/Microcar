package de.spanier.microcar.ui.home

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Intent
import androidx.core.app.ActivityCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import de.spanier.microcar.connection.bluetooth.MyBluetoothService
import java.util.*


class HomeViewModel : ViewModel() {

    var adapter = BluetoothAdapter.getDefaultAdapter()
    var connection = MyBluetoothService()

    //region LivaData
    //==============================================================================================

    val btDevices: MutableLiveData<List<BluetoothDevice>> = MutableLiveData()//.apply {

    //region Getter/Setter
    //==============================================================================================
    // Called by the activity to request that we refresh the list of paired devices
    fun refreshPairedDevices() {
        btDevices.postValue(getPairedDevicesList() as List<BluetoothDevice>?)
    }

    // Getter method for the activity to use.
    fun getPairedDeviceList(): MutableLiveData<List<BluetoothDevice>> {
        return btDevices
    }

    private fun getPairedDevicesList(): List<BluetoothDevice?>? {
        return if (isBluetoothEnabled()) {
            ArrayList(adapter.bondedDevices)
        } else null
    }

    //region Init
    //==============================================================================================
    init {
        isBluetoothEnabled()
    }


    //region Helper
    //==============================================================================================
    fun isBluetoothEnabled(): Boolean {
        var enabled = false
        adapter = BluetoothAdapter.getDefaultAdapter()
        val tmp: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
        if (tmp?.isEnabled == false) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            //startActivityForResult(context, enableBtIntent, REQUEST_ENABLE_BT)
        }

        if (adapter != null) {
            enabled = adapter.isEnabled
            if (!enabled) {
//               val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
//               startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
            }
        }
        return enabled
    }

}
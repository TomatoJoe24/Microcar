package de.spanier.microcar.ui.home

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import de.spanier.microcar.R
import de.spanier.microcar.connection.bluetooth.ControllerConnection
import de.spanier.microcar.connection.bluetooth.MyBluetoothService
import java.util.*

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var listViewBTDevices: RecyclerView

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        val textView: TextView = root.findViewById(R.id.textView2)
        textView.setOnClickListener { v: View? ->
            textView.text = MyBluetoothService.deviceMac
            if(homeViewModel.connection.isConnected) {
                homeViewModel.connection.disconnect()
            } else {
                homeViewModel.connection.initialize()
            }

        }
//        homeViewModel.text.observe(viewLifecycleOwner, Observer {
//            textView.text = it
//        })
        listViewBTDevices = root.findViewById(R.id.rv_bt_devices)
        // Setup the RecyclerView

        // Setup the RecyclerView
        listViewBTDevices.layoutManager = LinearLayoutManager(context)
        val adapter = DeviceAdapter()
        listViewBTDevices.adapter = adapter

        homeViewModel.btDevices.observe(viewLifecycleOwner, Observer { list -> adapter.updateList(list) })//Observer {
            //listViewBTDevices.ada
        //})
        homeViewModel.refreshPairedDevices()

        if(!homeViewModel.isBluetoothEnabled()) {
            val enableIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableIntent, 3)
        }

        return root
    }

    // A class to hold the data in the RecyclerView
    private class DeviceViewHolder internal constructor(view: View) : RecyclerView.ViewHolder(view) {
        private val layout: RelativeLayout = view.findViewById(R.id.list_item)
        private val text1: TextView
        private val text2: TextView
        fun setupView(device: BluetoothDevice) {
            text1.text = device.name
            text2.text = device.address
            layout.setOnClickListener { view: View? ->
                //TODO openCommunicationsActivity(device.name, device.address)
                ControllerConnection.deviceName = device.name
                ControllerConnection.deviceMac = device.address
                MyBluetoothService.deviceName = device.name
                MyBluetoothService.deviceMac = device.address

            }
        }

        init {
            text1 = view.findViewById(R.id.list_item_text1)
            text2 = view.findViewById(R.id.list_item_text2)
        }
    }

    // A class to adapt our list of devices to the RecyclerView
    private class DeviceAdapter : RecyclerView.Adapter<DeviceViewHolder>() {
        private var deviceList: List<BluetoothDevice> = ArrayList()
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeviceViewHolder {
            return DeviceViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false))
        }

        override fun onBindViewHolder(holder: DeviceViewHolder, position: Int) {
            holder.setupView(deviceList[position])
        }

        override fun getItemCount(): Int {
            return deviceList.size
        }

        //TODO: BluetoothDevice List in Bluetooth Connector
        fun updateList(deviceList: List<BluetoothDevice>) {
            this.deviceList = deviceList
            notifyDataSetChanged()
        }
    }
}

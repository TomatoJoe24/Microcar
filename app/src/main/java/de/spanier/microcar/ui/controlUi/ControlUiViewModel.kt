package de.spanier.microcar.ui.controlUi

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import de.spanier.microcar.connection.bluetooth.ControllerConnection
import de.spanier.microcar.util.LightColor
import de.spanier.microcar.util.MotorState

class ControlUiViewModel(private val controller: ControllerConnection, private val fragment: ControlUiFragment?) : ViewModel() {

    val position = MutableLiveData<Int>().apply {
        value = controller.turnPosition
    }

    val text: MutableLiveData<String> = MutableLiveData<String>().apply {
        value = "This is gallery Fragment"
    }
    val lightColor: MutableLiveData<LightColor> = MutableLiveData<LightColor>().apply {
        value = LightColor.OFF
    }

    val motorState: MutableLiveData<MotorState> = MutableLiveData<MotorState>().apply {
        value = MotorState.STOPPED
        controller.stop()
    }

    val temperature = MutableLiveData<Float>().apply {
        value = controller.temperature
    }

    val humidity = MutableLiveData<Float>().apply {
        value = controller.humidity
    }

}
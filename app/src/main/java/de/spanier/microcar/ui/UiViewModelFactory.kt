package de.spanier.microcar.ui

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import de.spanier.microcar.connection.IControllerConnection
import de.spanier.microcar.connection.bluetooth.ControllerConnection
import de.spanier.microcar.ui.controlUi.ControlUiFragment

class UiViewModelFactory(private val controller: IControllerConnection? = null, private val fragment: Fragment? = null) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        //return super.create(modelClass)
        return when (fragment) {
            is ControlUiFragment -> modelClass.getConstructor(ControllerConnection::class.java, ControlUiFragment::class.java).newInstance(controller, fragment)
            else -> modelClass.newInstance()
        }
    }
}
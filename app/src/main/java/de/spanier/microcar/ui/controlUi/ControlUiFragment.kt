package de.spanier.microcar.ui.controlUi

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.slider.Slider
import de.spanier.microcar.R
import de.spanier.microcar.connection.bluetooth.ControllerConnection
import de.spanier.microcar.ui.UiViewModelFactory
import de.spanier.microcar.util.LightColor
import de.spanier.microcar.util.MotorState

class ControlUiFragment : Fragment() {

    private lateinit var controlUiViewModel: ControlUiViewModel
    private var controller = ControllerConnection()

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        controlUiViewModel =
                ViewModelProviders.of(this, UiViewModelFactory(controller, this)).get(ControlUiViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_control_ui, container, false)

        //region Textfield
        //==============================================================================================

        val positionTextView: TextView = root.findViewById(R.id.text_gallery)
        val lightTextView: TextView = root.findViewById(R.id.text_gallery2)
        val motorTextView: TextView = root.findViewById(R.id.text_gallery3)
        val temperatureTextView: TextView = root.findViewById(R.id.text_gallery6)
        val humidityTextView: TextView = root.findViewById(R.id.text_gallery7)

        //region Slider
        //==============================================================================================

        val sbTurn: Slider = root.findViewById(R.id.slider_turn)
        val pos = controlUiViewModel.position.value?.toFloat()
        if (pos != null) {
            sbTurn.value = pos
        }
        sbTurn.addOnChangeListener { _, value, _ ->
            controlUiViewModel.position.value = value.toInt()
            controller.turnPosition = value.toInt()
        }


        //region Buttons
        //==============================================================================================

        val btnForward: ImageButton = root.findViewById(R.id.btnForward)
        btnForward.setOnTouchListener { v, event ->
            when (event?.action) {
                MotionEvent.ACTION_DOWN -> {
                    Log.e("Fragment", "Button Forward down")
                    controlUiViewModel.motorState.value = MotorState.FORWARD
                    controller.start()
                }
                MotionEvent.ACTION_UP   -> {
                    Log.e("Fragment", "Button Forward up")
                    controlUiViewModel.motorState.value = MotorState.STOPPED
                    controller.stop()
                }
            }
            v?.onTouchEvent(event) ?: true
        }

        val btnBackward: ImageButton = root.findViewById(R.id.btnBackward)
        btnBackward.setOnTouchListener { v, event ->
            when (event?.action) {
                MotionEvent.ACTION_DOWN -> {
                    Log.e("Fragment", "Button Backward down")
                    controlUiViewModel.motorState.value = MotorState.BACKWARD
                    controller.backwards()
                }
                MotionEvent.ACTION_UP   -> {
                    Log.e("Fragment", "Button Backward up")
                    controlUiViewModel.motorState.value = MotorState.STOPPED
                    controller.stop()
                }
            }
            v?.onTouchEvent(event) ?: true
        }

        val btnFlashLight: ImageButton = root.findViewById(R.id.btnFlashLight)
        btnFlashLight.setOnClickListener {
            if(controlUiViewModel.lightColor.value == LightColor.OFF) {
                controlUiViewModel.lightColor.value = LightColor.WHITE
            } else {
                controlUiViewModel.lightColor.value = LightColor.OFF
            }
            controller.flashLight = controlUiViewModel.lightColor.value
        }


        //region Observer
        //==============================================================================================

        controlUiViewModel.text.observe(viewLifecycleOwner, Observer {
            positionTextView.text = it
        })

        controlUiViewModel.position.observe(viewLifecycleOwner, Observer<Int> { tmpInt ->
            Log.e("Test", "Changed in Fragment to $tmpInt")
            positionTextView.text = tmpInt.toString()
        })

        controlUiViewModel.lightColor.observe(viewLifecycleOwner, Observer<LightColor> {
            Log.e("Observer", "Changed in ViewModel to ${it.name}")
            lightTextView.text = it.name
        })

        controlUiViewModel.motorState.observe(viewLifecycleOwner, Observer<MotorState> {
            Log.e(" Motor Observer", "Changed in ViewModel to ${it.name}")
            motorTextView.text = it.name
        })

        controlUiViewModel.temperature.observe(viewLifecycleOwner, Observer<Float> { tmpFloat ->
            Log.e("Test", "Changed in Fragment to $tmpFloat")
            temperatureTextView.text = tmpFloat.toString()
        })

        controlUiViewModel.humidity.observe(viewLifecycleOwner, Observer<Float> { tmpFloat ->
            Log.e("Test", "Changed in Fragment to $tmpFloat")
            humidityTextView.text = tmpFloat.toString()
        })



        return root
    }
}


package com.cililing.harvbox.thingsapp.ui.views

import android.content.Context
import android.util.AttributeSet
import android.widget.CompoundButton
import android.widget.FrameLayout
import android.widget.Switch
import android.widget.TextView
import com.cililing.harvbox.thingsapp.R

class SingleRelayView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    init {
        inflate(getContext(), R.layout.single_relay_view, this)
    }

    private val label: TextView = findViewById(R.id.single_relay_label)
    private val switch: Switch = findViewById(R.id.single_relay_switch)

    var labelText: String
        get() = label.text.toString()
        set(value) {
            label.text = value
        }

    var isChecked: Boolean
        get() = switch.isChecked
        set(value) {
            switch.isChecked = value
        }

    fun setOnCheckedChangeListener(listener: CompoundButton.OnCheckedChangeListener) {
        switch.setOnCheckedChangeListener(listener)
    }
}
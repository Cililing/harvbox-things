package com.cililing.harvbox.thingsapp.customViews

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import com.cililing.harvbox.thingsapp.R
import org.jetbrains.anko.find

class LightSwitchButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    init {
        inflate(context, R.layout.view_dashboard_on_off_button, this)
    }

    private val label = find<TextView>(R.id.on_off_button_label)
    private val switch = find<SwitchCompat>(R.id.on_off_button_switch)
    private val warning = find<TextView>(R.id.not_in_required_state_warning)

    private var listener: ((Boolean) -> Unit)? = null

    fun init(
        labelText: String,
        initialState: Boolean
    ) {
        label.text = labelText
        switch.isChecked = initialState
    }

    fun setOnCheckedListener(listener: (Boolean) -> Unit) {
        this.listener = listener
        switch.setOnCheckedChangeListener { _, isChecked ->
            listener.invoke(isChecked)
        }
    }

    fun fixChecked(isChecked: Boolean) {
        switch.setOnCheckedChangeListener(null)
        switch.isChecked = isChecked
        switch.setOnCheckedChangeListener { _, isChecked ->
            listener?.invoke(isChecked)
        }
    }

    fun showWarning(show: Boolean) {
        warning.visibility = if (show) View.VISIBLE else View.GONE
    }
}
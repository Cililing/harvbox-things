package com.cililing.harvbox.thingsapp.customViews

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.TextView
import com.cililing.harvbox.thingsapp.R
import org.jetbrains.anko.find

class RealValueView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    init {
        inflate(context, R.layout.real_value_view, this)
    }

    private val label = find<TextView>(R.id.real_value_view_label)
    private val value = find<TextView>(R.id.real_value_view_value)

    fun init(labelText: String, value: String) {
        this.label.text = labelText
        this.value.text = value
    }

    fun updateValue(value: String) {
        this.value.text = value
    }
}
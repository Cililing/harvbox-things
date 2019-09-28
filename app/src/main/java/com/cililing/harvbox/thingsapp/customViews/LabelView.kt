package com.cililing.harvbox.thingsapp.customViews

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.TextView
import com.cililing.harvbox.thingsapp.R
import org.jetbrains.anko.find

class LabelView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    init {
        inflate(context, R.layout.view_dashboard_label, this)
    }

    private val text = find<TextView>(R.id.label_view_text)

    fun setLabel(text: String) {
        this.text.text = text
    }
}
package com.cililing.harvbox.thingsapp.customViews

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import com.cililing.harvbox.thingsapp.R
import org.jetbrains.anko.find

class OnOffButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    init {
        inflate(context, R.layout.on_off_button, this)
    }

    private val label = find<TextView>(R.id.on_off_button_label)
    private val icon = find<ImageView>(R.id.on_off_button_image)
    private val switch = find<SwitchCompat>(R.id.on_off_button_switch)

    private var listener: ((Boolean) -> Unit)? = null

    fun init(
        labelText: String,
        initialState: Boolean,
        iconDrawable: Drawable? = null
    ) {
        label.text = labelText
        switch.isChecked = initialState
        iconDrawable?.let { icon.setImageDrawable(iconDrawable) }
    }

    fun setOnCheckedListner(listener: (Boolean) -> Unit) {
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
}
package com.cililing.harvbox.thingsapp.customViews

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.TextView
import com.cililing.harvbox.thingsapp.R
import org.jetbrains.anko.find
import org.jetbrains.anko.sdk27.coroutines.onClick

class EditLongView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    init {
        inflate(context, R.layout.view_labeled_edit_long_val, this)
    }

    private val title = find<TextView>(R.id.labeled_edit_text_title)
    private val desc = find<TextView>(R.id.labeled_edit_text_desc)
    private val edit = find<EditText>(R.id.labeled_edit_text_input)
    private val okButton = find<Button>(R.id.labeled_edit_text_ok)

    init {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.EditLongView)

        val titleString = ta.getString(R.styleable.EditLongView_htitle)
        val descString = ta.getString(R.styleable.EditLongView_hdesc)

        ta.recycle()

        title.text = titleString
        desc.text = descString
    }

    fun setOnApplyListener(listener: (Long?) -> Unit) {
        okButton.onClick {
            edit.text.toString().toLongOrNull().let(listener)
        }
    }

    fun setValue(value: Long) {
        edit.setText(value.toString())
    }
}
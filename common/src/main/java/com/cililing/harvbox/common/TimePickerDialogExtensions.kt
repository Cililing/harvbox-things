package com.cililing.harvbox.common

import android.app.TimePickerDialog
import android.content.Context

fun timePickerDialog24(
    context: Context?,
    hourOfDay: Int,
    minute: Int,
    listener: TimePickerDialog.OnTimeSetListener
) = context?.let { TimePickerDialog(
        context,
        listener,
        hourOfDay,
        minute,
        true
) }
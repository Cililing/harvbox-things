package com.cililing.harvbox.thingsapp.thingscontroller

import android.graphics.Bitmap
import android.content.Context
import android.graphics.BitmapFactory
import androidx.annotation.IdRes
import java.io.ByteArrayOutputStream


internal val mockedPhotos = listOf(
    R.drawable.e0,
    R.drawable.e1,
    R.drawable.e2
)

internal fun Context.getByteArrayDrawable(@IdRes drawableRes: Int): ByteArray? {
    val bitmap = BitmapFactory.decodeResource(resources, drawableRes)
    val stream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
    return stream.toByteArray()
}

package com.cililing.direct.firebase

import android.net.Uri
import com.cililing.harvbox.common.Clock
import com.google.firebase.FirebaseApp
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference


internal interface FirebaseAppStorage {
    fun putPhoto(bytePhotoArray: ByteArray, onSuccess: (Uri) -> Unit)
}

class FirebaseAppStorageImpl(
    firebaseApp: FirebaseApp,
    private val clock: Clock
) : FirebaseAppStorage {

    private val firebaseStorage: FirebaseStorage = FirebaseStorage.getInstance(firebaseApp)
    private val photosReference: StorageReference = firebaseStorage.getReference("pictures")

    override fun putPhoto(bytePhotoArray: ByteArray, onSuccess: (Uri) -> Unit) {
        val uniquePath = photosReference.child("${clock.milis()}")
        uniquePath.putBytes(bytePhotoArray)
            .addOnSuccessListener { _ ->
                uniquePath.downloadUrl.addOnSuccessListener {
                    onSuccess(it)
                }
            }
    }
}
package com.cililing.direct.firebase

import android.net.Uri
import com.cililing.harvbox.common.Clock
import com.cililing.harvbox.common.FirebaseConstans
import com.google.firebase.FirebaseApp
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference


internal interface FirebaseCloudDatabase {
    fun putPhoto(bytePhotoArray: ByteArray, onSuccess: (Uri) -> Unit)
}

class FirebaseCloudDatabaseImpl(
    firebaseApp: FirebaseApp,
    private val clock: Clock
) : FirebaseCloudDatabase {

    private val firebaseStorage: FirebaseStorage = FirebaseStorage.getInstance(firebaseApp)
    private val photosReference: StorageReference = firebaseStorage.getReference(FirebaseConstans.Cloud.photos)

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
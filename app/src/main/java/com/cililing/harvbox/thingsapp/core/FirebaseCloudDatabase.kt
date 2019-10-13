package com.cililing.harvbox.thingsapp.core

import com.cililing.harvbox.common.FirebaseConstans
import com.google.firebase.FirebaseApp
import com.google.firebase.storage.FirebaseStorage

interface FirebaseCloudDatabase {

}

class FirebaseCloudDatabaseImpl(
    firebaseApp: FirebaseApp
): FirebaseCloudDatabase {

    private val firebaseStorage = FirebaseStorage.getInstance(firebaseApp)

    private val photoContainer = firebaseStorage.getReference(FirebaseConstans.Cloud.photos)

    suspend fun getPhotoUris() {
        // TODO
    }
}
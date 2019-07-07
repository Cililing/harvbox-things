package com.cililing.harvbox.thingsapp.firebase

import com.google.firebase.database.*

object FirebaseDatabaseHelper {

    private val firebaseDatabase: FirebaseDatabase by lazy {
        FirebaseDatabase.getInstance()
    }

    fun obtainMainReference(): DatabaseReference {
        return firebaseDatabase.reference
    }
}
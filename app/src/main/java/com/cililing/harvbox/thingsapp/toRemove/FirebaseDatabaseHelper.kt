package com.cililing.harvbox.thingsapp.toRemove

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

object FirebaseDatabaseHelper {

    private val firebaseDatabase: FirebaseDatabase by lazy {
        FirebaseDatabase.getInstance()
    }

    fun obtainMainReference(): DatabaseReference {
        return firebaseDatabase.reference
    }
}
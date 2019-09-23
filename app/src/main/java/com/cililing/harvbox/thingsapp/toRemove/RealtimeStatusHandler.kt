package com.cililing.harvbox.thingsapp.toRemove

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import java.lang.Exception

class RealtimeStatusHandler {

    class RealtimeStatusTypeMismatchException(msg: String? = null) : Exception(msg)

    private val statusDbReference by lazy {
        FirebaseDatabaseHelper.obtainMainReference()
                .child("thingsapp")
                .child("status")
    }

    fun observeLedStatus(observer: (Boolean) -> Unit) {
        statusDbReference.child("led").addValueEventListener(object : ValueEventListener {

            override fun onCancelled(p0: DatabaseError) {
                // TODO
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                when (dataSnapshot.value) {
                    is Boolean -> observer.invoke(dataSnapshot.value as Boolean)
                    else -> throw RealtimeStatusTypeMismatchException("Expected boolean!")
                }
            }
        })
    }
}
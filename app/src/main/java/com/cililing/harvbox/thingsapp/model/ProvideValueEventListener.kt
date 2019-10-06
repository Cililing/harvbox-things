package com.cililing.harvbox.thingsapp.model

import com.cililing.harvbox.thingsapp.core.CurrentSnapshotProvider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class ProvideValueEventListener<T>(
    private val dataProvider: CurrentSnapshotProvider<T>,
    private val dataGetter: (DataSnapshot) -> T?
) : ValueEventListener {

    override fun onCancelled(p0: DatabaseError) {
    }

    override fun onDataChange(dataSnapshot: DataSnapshot) {
        val value = dataGetter(dataSnapshot) ?: return
        dataProvider.newSnapshotAvailable(value)
    }
}
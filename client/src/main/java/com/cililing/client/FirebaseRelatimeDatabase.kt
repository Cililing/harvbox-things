package com.cililing.client

import com.cililing.harvbox.common.FirebaseConstans
import com.cililing.harvbox.common.SemiblockValueReporter
import com.cililing.harvbox.common.StatusSnapshot
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

internal interface FirebaseRelatimeDatabase {
    fun getSnapshot(): StatusSnapshot?
    fun setLight1(isOn: Boolean)
    fun setLight2(isOn: Boolean)
    fun triggerPhoto()
}

internal class FirebaseRelatimeDatabaseImpl(
    firebaseApp: FirebaseApp
) : FirebaseRelatimeDatabase {

    private val database = FirebaseDatabase.getInstance(firebaseApp)
    private val realtimeStatus = database.reference.child(FirebaseConstans.Realtime.Status.name)

    private val triggers = database.reference.child(FirebaseConstans.Realtime.Triggers.name)
    private val photoTrigger = triggers.child(FirebaseConstans.Realtime.Triggers.photo)

    var currentSnapshot: StatusSnapshot? = null

    private val light1SemiblockReporter = SemiblockValueReporter<Boolean>()
    private val light2SemiblockReporter = SemiblockValueReporter<Boolean>()

    init {
        realtimeStatus.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                val snapshot = StatusSnapshot(
                        light1PowerOn = p0.child(FirebaseConstans.Realtime.Status.light1).value as? Boolean ?: false,
                        light2PowerOn = p0.child(FirebaseConstans.Realtime.Status.light2).value as? Boolean ?: false,
                        timestamp = p0.child(FirebaseConstans.Realtime.Status.timestamp).value.toString(),
                        tempValue = p0.child(FirebaseConstans.Realtime.Status.temp).value as? Double ?: 0.0,
                        humidityValue = p0.child(FirebaseConstans.Realtime.Status.humidity).value as? Double ?: 0.0,
                        proximityValue = p0.child(FirebaseConstans.Realtime.Status.proximity).value as? Double ?: 0.0
                )
                currentSnapshot = snapshot.copy(
                        light1PowerOn = light1SemiblockReporter.obtainValueAndRelease(snapshot.light1PowerOn),
                        light2PowerOn = light2SemiblockReporter.obtainValueAndRelease(snapshot.light2PowerOn)
                )
            }
        })
    }

    override fun getSnapshot(): StatusSnapshot? {
        return currentSnapshot
    }

    override fun setLight1(isOn: Boolean) {
        light1SemiblockReporter.value = isOn
        realtimeStatus.child(FirebaseConstans.Realtime.Status.light1).setValue(isOn)
    }

    override fun setLight2(isOn: Boolean) {
        light2SemiblockReporter.value = isOn
        realtimeStatus.child(FirebaseConstans.Realtime.Status.light2).setValue(isOn)
    }

    override fun triggerPhoto() {
        photoTrigger.setValue(false).addOnSuccessListener {
            photoTrigger.setValue(true)
        }
    }
}
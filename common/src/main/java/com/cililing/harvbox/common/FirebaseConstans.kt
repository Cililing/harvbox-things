package com.cililing.harvbox.common

object FirebaseConstans {

    interface DatabaseContainer {
        val name: String
    }

    object Cloud {
        const val photos = "pictures"
    }

    object Realtime {

        object LightSettings : DatabaseContainer {
            override val name: String = "light_status"
            const val light1 = "light1"
            const val light2 = "light2"
        }

        object AppSettings : DatabaseContainer {
            override val name: String = "app_settings"
            const val elasticCooldown = "elastic_cooldown"
            const val realtimeDbCooldown = "realtime_db_cooldown"
            const val photoCooldown = "photo_cooldown"
        }

        object Status : DatabaseContainer {
            override val name: String = "status"
            const val timestamp = "timestamp"
            const val humidity = "humidity"
            const val light1 = "light1"
            const val light2 = "light2"
            const val proximity = "proximity"
            const val temp = "temp"
        }

        const val lastPhoto = "last_photo"

        object Triggers : DatabaseContainer {
            override val name: String = "triggers"
            const val photo = "photo"
        }
    }
}
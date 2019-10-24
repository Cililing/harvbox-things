package com.cililing.harvbox.thingsapp.core

import android.net.Uri
import com.cililing.harvbox.common.FirebaseConstans
import com.google.firebase.FirebaseApp
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.tasks.asDeferred
import kotlinx.coroutines.tasks.await

interface FirebaseCloudDatabase {
    suspend fun getPhotoUris(): List<Uri>
    fun getPhotoFetcher(): CloudUriFetcher
}

data class CloudItem(
    val uri: Uri,
    val filename: String
)

class CloudUriFetcher(
    private val containerReference: StorageReference
) {

    private var lastToken: String? = null
    private var hasNext: Boolean = true
    private var currentDeferreds: List<Deferred<*>>? = listOf()

    suspend fun next(size: Int): List<CloudItem> = coroutineScope {
        if (!hasNext) {
            return@coroutineScope listOf<CloudItem>()
        }

        val token = lastToken
        val callResult = if (token == null) {
            containerReference.list(size)
        } else {
            containerReference.list(size, token)
        }.await()

        lastToken = callResult.pageToken

        callResult.items.map {
            val deferreds = it.downloadUrl.asDeferred()
            val filenames = it.name

            Pair(deferreds, filenames)
        }.also {
            currentDeferreds = it.map { it.first }
        }.map {
            Pair(
                it.first.await(),
                it.second
            )
        }.also {
            if (it.size < size) {
                hasNext = false
            }
            currentDeferreds = null
        }.map {
            CloudItem(it.first, it.second)
        }
    }

    fun reset() {
        hasNext = true
        try {
            currentDeferreds?.forEach { it.cancel() }
        } catch (ignored: Exception) {
        }
        currentDeferreds = null
        lastToken = null
    }
}

class FirebaseCloudDatabaseImpl(
    firebaseApp: FirebaseApp
) : FirebaseCloudDatabase {

    private val firebaseStorage = FirebaseStorage.getInstance(firebaseApp)

    private val photoContainer = firebaseStorage.getReference(FirebaseConstans.Cloud.photos)

    override suspend fun getPhotoUris() = coroutineScope {
        val listResult = try {
            photoContainer.listAll().await()
        } catch (ex: Exception) {
            return@coroutineScope listOf<Uri>()
        }

        return@coroutineScope listResult.items.mapNotNull {
            return@mapNotNull try {
                it.downloadUrl.await()
            } catch (ex: Exception) {
                null
            }
        }
    }

    override fun getPhotoFetcher(): CloudUriFetcher {
        return CloudUriFetcher(photoContainer)
    }
}
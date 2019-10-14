package com.cililing.harvbox.thingsapp.gallery

import com.cililing.harvbox.thingsapp.core.FirebaseCloudDatabase
import com.cililing.harvbox.thingsapp.core.mvp.BasePresenterImpl
import kotlinx.coroutines.launch

class GalleryPresenter(
    view: GalleryContract.View,
    firebaseCloudDatabase: FirebaseCloudDatabase
) : BasePresenterImpl<GalleryContract.View>(view), GalleryContract.Presenter {

    companion object {
        private const val SIZE = 10
    }


    private val photoFetcher = firebaseCloudDatabase.getPhotoFetcher()

    override fun onDestroy() {
        super.onDestroy()
        photoFetcher.reset()
    }

    override fun requestNewItems() {
        coroutineScope.launch {
            val photos = photoFetcher.next(SIZE)
            view.newItemsReceived(photos.map { GalleryItem(it.toString()) })
        }
    }

    override fun galleryPreviewClicked(item: GalleryItem) {
        view.showPhoto(item)
    }
}
package com.cililing.harvbox.thingsapp.gallery

import com.cililing.harvbox.thingsapp.core.mvp.BaseContract
import com.cililing.harvbox.thingsapp.core.mvp.BasePresenter
import com.cililing.harvbox.thingsapp.core.mvp.BaseView

interface GalleryContract : BaseContract {
    interface View : BaseView<Presenter> {
        fun newItemsReceived(newGalleryItems: List<GalleryItem>)
        fun showPhoto(item: GalleryItem)
    }

    interface Presenter : BasePresenter<View> {
        fun requestNewItems()
        fun galleryPreviewClicked(item: GalleryItem)
    }
}
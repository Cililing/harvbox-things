package com.cililing.harvbox.thingsapp.gallery

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cililing.harvbox.thingsapp.R
import com.cililing.harvbox.thingsapp.core.mvp.BaseFragment
import com.squareup.picasso.Picasso
import org.jetbrains.anko.support.v4.find
import org.koin.android.scope.currentScope
import org.koin.android.ext.android.get

class GalleryFragment : BaseFragment<GalleryContract.Presenter>(), GalleryContract.View {

    companion object {
        fun newInstance() = GalleryFragment()
    }

    private val previewRecyclerView by lazy { find<RecyclerView>(R.id.gallery_preview) }
    private val photoView by lazy { find<ImageView>(R.id.photo_view) }
    private var isLoading = false

    private val galleryPreviewAdapter = GalleryPreviewRecyclerViewAdapter(
        object : GalleryPreviewRecyclerViewAdapter.Callback {
            override fun itemClicked(item: GalleryItem) {
                presenter.galleryPreviewClicked(item)
            }
        }
    )

    override fun getLayoutId(): Int = R.layout.fragment_gallery

    override val presenter: GalleryContract.Presenter by currentScope.inject {
        getPresenterParams(this, get())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter.requestNewItems()

        previewRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        previewRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                handlePreviewScroll(recyclerView)
            }
        })
        previewRecyclerView.adapter = galleryPreviewAdapter
    }

    override fun newItemsReceived(newGalleryItems: List<GalleryItem>) {
        isLoading = false
        galleryPreviewAdapter.addNewItems(newGalleryItems)
    }

    override fun showPhoto(item: GalleryItem) {
        Picasso.get().load(item.uriString).into(photoView)
    }

    private fun handlePreviewScroll(recyclerView: RecyclerView) {
        val layoutManager = recyclerView.layoutManager as? LinearLayoutManager ?: return

        val visibleItemCount = layoutManager.childCount
        val totalItemCount = layoutManager.itemCount
        val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

        if (!isLoading) {
            if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >= 0) {
                isLoading = true
                presenter.requestNewItems()
            }
        }
    }
}
package com.cililing.harvbox.thingsapp.gallery

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.cililing.harvbox.thingsapp.R
import com.squareup.picasso.Picasso
import org.jetbrains.anko.find
import org.jetbrains.anko.sdk27.coroutines.onClick

class GalleryPreviewRecyclerViewAdapter(
    private val callback: Callback
) : RecyclerView.Adapter<GalleryPreviewViewHolder>() {

    interface Callback {
        fun itemClicked(item: GalleryItem)
    }

    private val itemList: MutableList<GalleryItem> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryPreviewViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.view_holder_gallery, parent, false)

        return GalleryPreviewViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.count()
    }

    override fun onBindViewHolder(holder: GalleryPreviewViewHolder, position: Int) {
        holder.bind(itemList[position], callback::itemClicked)
    }

    fun addNewItems(items: Collection<GalleryItem>) {
        itemList.addAll(items)
        notifyDataSetChanged()
    }
}


class GalleryPreviewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val image = itemView.find<ImageView>(R.id.gallery_preview)

    fun bind(galleryItem: GalleryItem, clickCallback: (GalleryItem) -> Unit) {
        itemView.onClick { clickCallback.invoke(galleryItem) }
        Picasso.get().load(galleryItem.uriString).into(image)
    }
}
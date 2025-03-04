package com.example.marvelapp.presentation.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.marvelapp.R
import com.example.marvelapp.databinding.ItemChildDetailBinding
import com.example.marvelapp.framework.imageloader.ImageLoader

class DetailChildAdapter(
    private val detailChildList: List<DetailChildVE>,
    private val imageLoader: ImageLoader,
    private val onItemClick: (imageUrl: String) -> Unit
) : RecyclerView.Adapter<DetailChildAdapter.DetailChildViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailChildViewHolder {
        return DetailChildViewHolder.create(parent, imageLoader, onItemClick)
    }

    override fun onBindViewHolder(holder: DetailChildViewHolder, position: Int) {
        holder.bind(detailChildList[position])
    }

    override fun getItemCount() = detailChildList.size

    class DetailChildViewHolder(
        itemBinding: ItemChildDetailBinding,
        private val imageLoader: ImageLoader,
        private val onItemClick: (imageUrl: String) -> Unit
    ) : RecyclerView.ViewHolder(itemBinding.root) {

        private val imageCategory: ImageView = itemBinding.imageItemCategory

        fun bind(detailChildVE: DetailChildVE) {
            imageLoader.load(imageCategory, detailChildVE.imageUrl)

            itemView.setOnClickListener {
                onItemClick.invoke(detailChildVE.imageUrl)
            }
        }

        companion object {
            fun create(
                parent: ViewGroup,
                imageLoader: ImageLoader,
                onItemClick: (imageUrl: String) -> Unit
            ): DetailChildViewHolder {
                val itemBinding = ItemChildDetailBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false)

                return DetailChildViewHolder(itemBinding, imageLoader, onItemClick)
            }
        }
    }
}
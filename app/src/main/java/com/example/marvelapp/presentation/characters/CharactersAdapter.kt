package com.example.marvelapp.presentation.characters

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

class CharactersAdapter :
    PagingDataAdapter<com.example.core.domain.model.Character, CharacterViewHolder>(diffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        return CharacterViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    companion object {
        private val diffCallback =
            object : DiffUtil.ItemCallback<com.example.core.domain.model.Character>() {
                override fun areItemsTheSame(
                    oldItem: com.example.core.domain.model.Character,
                    newItem: com.example.core.domain.model.Character
                ): Boolean {
                    return oldItem.name == newItem.name
                }

                override fun areContentsTheSame(
                    oldItem: com.example.core.domain.model.Character,
                    newItem: com.example.core.domain.model.Character
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }
}
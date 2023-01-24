package com.example.marvelapp.presentation.characters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.example.marvelapp.databinding.ItemCharactersLoadMoreStateBinding

class CharacterLoadMoreStateViewHolder(
    itemBinding: ItemCharactersLoadMoreStateBinding,
    retry: () -> Unit
) : RecyclerView.ViewHolder(itemBinding.root) {

    private val binding = ItemCharactersLoadMoreStateBinding.bind(itemView)
    private val progressBarLoadingState = binding.progressLoadingMore
    private val textTryAgainMessage = binding.textTryAgain.also {
        it.setOnClickListener {
            retry()
        }
    }

    fun bind(loadState: LoadState) {
        progressBarLoadingState.isVisible = loadState is LoadState.Loading
        textTryAgainMessage.isVisible = loadState is LoadState.Error
    }

    companion object {
        fun create(parent: ViewGroup, retry: () -> Unit): CharacterLoadMoreStateViewHolder {
            val itemBinding = ItemCharactersLoadMoreStateBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )

            return CharacterLoadMoreStateViewHolder(itemBinding, retry)
        }
    }

}
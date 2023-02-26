package com.example.marvelapp.presentation.characters.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.example.marvelapp.databinding.ItemCharactersRefreshStateBinding

class CharactersRefreshViewHolder(
    itemBinding: ItemCharactersRefreshStateBinding,
    retry: () -> Unit
) : RecyclerView.ViewHolder(itemBinding.root) {

    private val binding = ItemCharactersRefreshStateBinding.bind(itemView)
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
        fun create(parent: ViewGroup, retry: () -> Unit): CharactersRefreshViewHolder {
            val itemBinding = ItemCharactersRefreshStateBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )

            return CharactersRefreshViewHolder(itemBinding, retry)
        }
    }

}
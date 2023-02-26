package com.example.marvelapp.presentation.characters.adapters

import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter

class CharacterRefreshAdapter(
    private val retry: () -> Unit,
) : LoadStateAdapter<CharactersRefreshViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): CharactersRefreshViewHolder = CharactersRefreshViewHolder.create(parent, retry)

    override fun onBindViewHolder(
        holder: CharactersRefreshViewHolder,
        loadState: LoadState
    ) = holder.bind(loadState)
}
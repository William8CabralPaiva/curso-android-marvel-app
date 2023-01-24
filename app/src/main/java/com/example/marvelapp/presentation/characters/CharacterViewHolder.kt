package com.example.marvelapp.presentation.characters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.marvelapp.R
import com.example.marvelapp.databinding.ItemCharacterBinding

class CharacterViewHolder(
    binding: ItemCharacterBinding
) : RecyclerView.ViewHolder(binding.root) {

    private val textName = binding.textName
    private val imgView = binding.imageCharacter

    fun bind(character: com.example.core.domain.model.Character) {
        textName.text = character.name
        Glide.with(itemView).load(character.imageUrl).fallback(R.drawable.ic_img_loading_error)
            .into(imgView)
    }

    companion object {
        fun create(parent: ViewGroup): CharacterViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val itemBinding = ItemCharacterBinding.inflate(inflater, parent, false)
            return CharacterViewHolder(itemBinding)
        }
    }
}
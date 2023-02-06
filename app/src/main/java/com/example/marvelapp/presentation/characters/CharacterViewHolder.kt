package com.example.marvelapp.presentation.characters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.core.domain.model.Character
import com.example.marvelapp.R
import com.example.marvelapp.databinding.ItemCharacterBinding
import com.example.marvelapp.framework.imageloader.ImageLoader
import com.example.marvelapp.util.OnCharacterItemClick

class CharacterViewHolder(
    binding: ItemCharacterBinding,
    private val imageLoader: ImageLoader,
    private val onItemClick: OnCharacterItemClick,
) : RecyclerView.ViewHolder(binding.root) {

    private val textName = binding.textName
    private val imgView = binding.imageCharacter

    fun bind(character: Character) {
        textName.text = character.name
        imgView.transitionName =
            character.name // define qual vai ser a animação pelo nome do character,
        //que foi vinculado com o pair no fragment
        //as view tem que ter o msm nome e id

        imageLoader.load(imgView, character.imageUrl)

        itemView.setOnClickListener {
            onItemClick.invoke(character, imgView)
        }
    }

    companion object {
        fun create(
            parent: ViewGroup,
            imageLoader: ImageLoader,
            onItemClick: OnCharacterItemClick
        ): CharacterViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val itemBinding = ItemCharacterBinding.inflate(inflater, parent, false)
            return CharacterViewHolder(itemBinding,imageLoader, onItemClick)
        }
    }
}
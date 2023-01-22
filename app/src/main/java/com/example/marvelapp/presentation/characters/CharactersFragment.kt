package com.example.marvelapp.presentation.characters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.core.domain.model.Character
import com.example.marvelapp.databinding.FragmentCharactersBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CharactersFragment : Fragment() {

    private var _binding: FragmentCharactersBinding? = null
    private val binding get() = _binding!!
    private val charactersAdapter = CharactersAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCharactersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initCharacters()
        charactersAdapter.submitList(
            listOf(
                Character(
                    "Homem Aranha",
                    "https://upload.wikimedia.org/wikipedia/pt/thumb/1/14/Spide-Man_Poster.jpg/250px-Spide-Man_Poster.jpg"
                ),
                Character(
                    "Homem Aranha",
                    "https://upload.wikimedia.org/wikipedia/pt/thumb/1/14/Spide-Man_Poster.jpg/250px-Spide-Man_Poster.jpg"
                ),
                Character(
                    "Homem Aranha",
                    "https://upload.wikimedia.org/wikipedia/pt/thumb/1/14/Spide-Man_Poster.jpg/250px-Spide-Man_Poster.jpg"
                ),
                Character(
                    "Homem Aranha",
                    "https://upload.wikimedia.org/wikipedia/pt/thumb/1/14/Spide-Man_Poster.jpg/250px-Spide-Man_Poster.jpg"
                ),
            )
        )
    }

    private fun initCharacters() {
        binding.recycleCharacters.apply {
            setHasFixedSize(true)
            adapter = charactersAdapter
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            CharactersFragment()
    }
}
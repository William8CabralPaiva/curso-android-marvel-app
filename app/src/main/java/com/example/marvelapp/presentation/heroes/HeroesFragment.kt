package com.example.marvelapp.presentation.heroes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.marvelapp.R
import com.example.marvelapp.databinding.FragmentFavoritesBinding
import com.example.marvelapp.databinding.FragmentHeroesBinding

class HeroesFragment : Fragment() {

    private var _binding: FragmentHeroesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHeroesBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            HeroesFragment()
    }
}
package com.example.marvelapp.presentation.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.marvelapp.R
import com.example.marvelapp.databinding.FragmentCharactersBinding
import com.example.marvelapp.databinding.FragmentFavoritesBinding
import com.example.marvelapp.framework.imageloader.ImageLoader
import com.example.marvelapp.presentation.common.getGenericAdapterOf
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var imageLoader: ImageLoader

    private val viewModel: FavoritesViewModel by viewModels()

    private val favoritesAdapter by lazy {
        getGenericAdapterOf { FavoritesViewHolder.create(it, imageLoader) }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initFavoritesAdapter()
        initObserver()
    }

    private fun initObserver() {

        viewModel.state.observe(viewLifecycleOwner) { uiState->
            binding.flipperFavorites.displayedChild = when (uiState) {
                is FavoritesViewModel.UiState.ShowFavorite -> {
                    favoritesAdapter.submitList(uiState.favoriteItems)
                    FLIPPER_SHOW_FAVORITES
                }
                is FavoritesViewModel.UiState.ShowEmpty -> {
                    favoritesAdapter.submitList(emptyList())
                    FLIPPER_EMPTY_FAVORITES
                }
            }
        }
        viewModel.getAll()
    }

    private fun initFavoritesAdapter() {
        binding.recyclerFavorites.run {
            setHasFixedSize(true)
            adapter = favoritesAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    companion object {
        const val FLIPPER_SHOW_FAVORITES = 0
        const val FLIPPER_EMPTY_FAVORITES = 1
    }
}
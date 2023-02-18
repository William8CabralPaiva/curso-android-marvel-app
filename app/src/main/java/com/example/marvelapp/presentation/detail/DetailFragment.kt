package com.example.marvelapp.presentation.detail

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.marvelapp.databinding.FragmentDetailBinding
import com.example.marvelapp.framework.imageloader.ImageLoader
import com.example.marvelapp.presentation.extensions.showShortToast
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    private val args by navArgs<DetailFragmentArgs>()//nome da tela + args

    private val viewModel: DetailViewModel by viewModels()

    @Inject
    lateinit var imageLoader: ImageLoader

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        val detailViewArg = args.detailViewArg
        initBindings(detailViewArg)

        setSharedElementTransitionOnEnter()
        observeCategoriesUiState(detailViewArg)
        initCheckFavoriteRequest(detailViewArg.characterId)
        observeFavoriteUiState()
    }

    private fun initBindings(detailViewArg: DetailViewArg) {
        binding.run {
            includeErrorView.buttonTryAgain.setOnClickListener {
                viewModel.favorite.update(detailViewArg)
            }

            imageCharacter.run {
                //as view tem que ter o msm nome e id
                transitionName = detailViewArg.name

                imageLoader.load(this, detailViewArg.imageUrl)
            }

            imageFavoriteIcon.setOnClickListener {
                viewModel.favorite.update(detailViewArg)
            }
        }
    }


    private fun observeCategoriesUiState(detailViewArg: DetailViewArg) {
        viewModel.categories.load(detailViewArg.characterId)

        viewModel.categories.state.observe(viewLifecycleOwner) { uiState ->
            //todo setar flipper

            binding.flipperDetail.displayedChild = when (uiState) {
                UiActionStateLiveData.UiState.Loading -> FLIPPER_CHILD_POSITION_LOADING
                is UiActionStateLiveData.UiState.Success -> {
                    binding.recyclerParentDetail.run {
                        setHasFixedSize(true)
                        adapter = DetailParentAdapter(uiState.detailParentList, imageLoader)
                    }
                    FLIPPER_CHILD_POSITION_DETAIL
                }
                UiActionStateLiveData.UiState.Error -> FLIPPER_CHILD_POSITION_ERROR
                is UiActionStateLiveData.UiState.Empty -> FLIPPER_CHILD_POSITION_EMPTY
            }

        }
    }

    private fun observeFavoriteUiState() {
        viewModel.favorite.state.observe(viewLifecycleOwner) {
            binding.flipperFavorite.displayedChild = when (it) {
                FavoriteUiActionStateLiveData.UiState.Loading -> FLIPPER_FAVORITE_POSITION_LOADING
                is FavoriteUiActionStateLiveData.UiState.Icon -> {
                    binding.imageFavoriteIcon.setImageResource(it.icon)
                    FLIPPER_FAVORITE_POSITION_IMAGE
                }
                is FavoriteUiActionStateLiveData.UiState.Error -> {
                    showShortToast(it.messageResId)
                    FLIPPER_FAVORITE_POSITION_IMAGE
                }
            }
        }
    }

    private fun initCheckFavoriteRequest(characterId: Int) {
        viewModel.favorite.checkFavorite(characterId)
    }

    //define tipo da animação para mover
    private fun setSharedElementTransitionOnEnter() {
        TransitionInflater.from(requireContext())
            .inflateTransition(android.R.transition.move).apply {
                sharedElementEnterTransition = this
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val FLIPPER_CHILD_POSITION_LOADING = 0
        private const val FLIPPER_CHILD_POSITION_DETAIL = 1
        private const val FLIPPER_CHILD_POSITION_ERROR = 2
        private const val FLIPPER_CHILD_POSITION_EMPTY = 3
        private const val FLIPPER_FAVORITE_POSITION_IMAGE = 0
        private const val FLIPPER_FAVORITE_POSITION_LOADING = 1
    }

}
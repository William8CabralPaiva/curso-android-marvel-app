package com.example.marvelapp.presentation.characters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.example.core.domain.model.Character
import com.example.marvelapp.databinding.FragmentCharactersBinding
import com.example.marvelapp.framework.imageloader.ImageLoader
import com.example.marvelapp.presentation.detail.DetailViewArg
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class CharactersFragment : Fragment() {

    private var _binding: FragmentCharactersBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CharactersViewModel by viewModels()

    private val charactersAdapter by lazy {
        CharactersAdapter(imageLoader) { character: Character, view: View ->
            val extras = FragmentNavigatorExtras(
                view to character.name// tal view vai ter esse nome
            )

            val directions = CharactersFragmentDirections.actionCharactersFragmentToDetailFragment(
                character.name,
                DetailViewArg(
                    character.id,
                    character.name,
                    character.imageUrl
                )
            )

            findNavController().navigate(directions, extras)
        }
    }

    @Inject
    lateinit var imageLoader: ImageLoader

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
        observeInitialLoadState()
        binding.includeViewCharactersErrorState.buttonTryAgain.setOnClickListener {
            charactersAdapter.retry()
        }

        viewModel.searchCharacter()
        viewModel.state.observe(viewLifecycleOwner) {
            when (it) {
                is CharactersViewModel.UiState.SearchResult -> {
                    charactersAdapter.submitData(
                        viewLifecycleOwner.lifecycle,
                        it.dataCharacter
                    )//vincular com o ciclo de vida do fragmento
                }
            }
        }

//        lifecycleScope.launch {
//            //todo colocar dependencia androidx.lifecycle:lifecycle-runtime-ktx
//            //todo com isso ira acionar apenas se o app estiver em primeiro plano, caso o app esteja em background n ira rodar
//            //evitando assim um crash
//            //caso utilize o flow
//            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
//                viewModel.charactersPagingData("").collect() { pagingData ->
//
//                }
//            }
//        }

    }

    private fun initCharacters() {


        binding.recycleCharacters.apply {
            //scrollToPosition(0)//voltar para posição inicial ao sair da pagina e voltar
            setHasFixedSize(true)
            //todo setar footer pra lista loaidng no final
            adapter = charactersAdapter.withLoadStateFooter(
                footer = CharacterLoadStateAdapter(charactersAdapter::retry)//retentar requisição
            )
        }
    }

    private fun observeInitialLoadState() {
        lifecycleScope.launch {
            charactersAdapter.loadStateFlow.collectLatest { loadState ->
                binding.flipperCharacters.displayedChild = when (loadState.refresh) {
                    is LoadState.Loading -> {
                        setShimmerVisibility(true)
                        FLIPPER_CHILD_LOADING
                    }
                    is LoadState.NotLoading -> {
                        setShimmerVisibility(false)
                        FLIPPER_CHILD_CHARACTERS
                    }
                    else -> {
                        setShimmerVisibility(false)
                        FLIPPER_CHILD_ERROR
                    }
                }
            }
        }
    }

    private fun setShimmerVisibility(visibility: Boolean) {
        binding.includeViewCharacters.shimmerCharacters.run {
            isVisible = visibility
            if (visibility) {
                startShimmer()
            } else {
                stopShimmer()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {

        private const val FLIPPER_CHILD_LOADING = 0
        private const val FLIPPER_CHILD_CHARACTERS = 1
        private const val FLIPPER_CHILD_ERROR = 2

        @JvmStatic
        fun newInstance() =
            CharactersFragment()
    }
}
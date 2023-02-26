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
import com.example.marvelapp.presentation.characters.adapters.CharacterLoadMoreStateAdapter
import com.example.marvelapp.presentation.characters.adapters.CharacterRefreshAdapter
import com.example.marvelapp.presentation.characters.adapters.CharactersAdapter
import com.example.marvelapp.presentation.characters.adapters.CharactersRefreshViewHolder
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

    private val headerAdapter by lazy { CharacterRefreshAdapter(charactersAdapter::retry) }

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
        postponeEnterTransition()//fala qye em algum momento vai ter a animação transição de tela ao voltar para a tela principal
        binding.recycleCharacters.apply {
            //scrollToPosition(0)//voltar para posição inicial ao sair da pagina e voltar
            setHasFixedSize(true)
            //todo setar footer pra lista loaidng no final
            adapter = charactersAdapter.withLoadStateHeaderAndFooter(
                header = headerAdapter,
                footer = CharacterLoadMoreStateAdapter(charactersAdapter::retry)//retentar requisição
            )
            //todo animação ao voltar a tela
            viewTreeObserver.addOnPreDrawListener {
                startPostponedEnterTransition()
                true
            }

        }
    }

    private fun observeInitialLoadState() {
        lifecycleScope.launch {
            charactersAdapter.loadStateFlow.collectLatest { loadState ->
                //todo take it verifica e mostra valor do resfresh
                headerAdapter.loadState = loadState.mediator?.refresh?.takeIf {
                    it is LoadState.Error && charactersAdapter.itemCount > 0
                } ?: loadState.prepend

                binding.flipperCharacters.displayedChild = when {
                    loadState.mediator?.refresh is LoadState.Loading -> {
                        setShimmerVisibility(true)
                        FLIPPER_CHILD_LOADING
                    }
                    //medeator banco externo
                    loadState.mediator?.refresh is LoadState.Error && charactersAdapter.itemCount == 0 -> {
                        setShimmerVisibility(false)
                        FLIPPER_CHILD_ERROR
                    }
                    //source banco de dados local
                    loadState.source.refresh is LoadState.NotLoading ||
                            loadState.mediator?.refresh is LoadState.NotLoading -> {
                        setShimmerVisibility(false)
                        FLIPPER_CHILD_CHARACTERS
                    }

                    else -> {
                        setShimmerVisibility(false)
                        FLIPPER_CHILD_CHARACTERS
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
package com.example.marvelapp.presentation.characters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.example.core.domain.model.Character
import com.example.marvelapp.R
import com.example.marvelapp.databinding.FragmentCharactersBinding
import com.example.marvelapp.framework.imageloader.ImageLoader
import com.example.marvelapp.presentation.characters.adapters.CharacterLoadMoreStateAdapter
import com.example.marvelapp.presentation.characters.adapters.CharacterRefreshAdapter
import com.example.marvelapp.presentation.characters.adapters.CharactersAdapter
import com.example.marvelapp.presentation.detail.DetailViewArg
import com.example.marvelapp.presentation.sort.SortFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class CharactersFragment : Fragment(), SearchView.OnQueryTextListener,
    MenuItem.OnActionExpandListener {

    private var _binding: FragmentCharactersBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CharactersViewModel by viewModels()
    private lateinit var searchView: SearchView

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

    private fun controlBackStack() {
        //https://developer.android.com/guide/navigation/navigation-programmatic#returning_a_result
        // se o sort fragment não fosse um bottomsheet dialog eu poderia usar o currentBackStackEntry

        val navBackStackEntry = findNavController().getBackStackEntry(R.id.charactersFragment)

        val observer = LifecycleEventObserver { _, event ->
            val isSortingApplied =
                navBackStackEntry.savedStateHandle.contains(SortFragment.SORTING_APPLIED_BASK_STACK_KEY)

            if (event == Lifecycle.Event.ON_RESUME && isSortingApplied) {
                viewModel.applySort()

                navBackStackEntry.savedStateHandle.remove<Boolean>(SortFragment.SORTING_APPLIED_BASK_STACK_KEY)
            }
        }

        navBackStackEntry.lifecycle.addObserver(observer)
        viewLifecycleOwner.lifecycle.addObserver(LifecycleEventObserver { _, event ->

            if (event == Lifecycle.Event.ON_DESTROY) {
                navBackStackEntry.lifecycle.removeObserver(observer)
            }

        })
    }

    private fun setupMenu() {
        //todo new menu config
        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
            override fun onPrepareMenu(menu: Menu) {
                // Handle for example visibility of menu items
            }

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.characters_menu_items, menu)

                val searchItem = menu.findItem(R.id.menu_search)
                searchView = searchItem.actionView as SearchView

                searchItem.setOnActionExpandListener(this@CharactersFragment)

                if (viewModel.currentSearchQuery.isNotEmpty()) {
                    //para manter o estado da tela, exemplo se o search estiver aberta e mudar para o tema dark, ela fecharia
                    //com isso ela mantem aberta
                    searchItem.expandActionView()
                    searchView.setQuery(viewModel.currentSearchQuery, false)
                }

                searchView.run {
                    isSubmitButtonEnabled = true
                    setOnQueryTextListener(this@CharactersFragment)
                }
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.menu_sort -> {
                        findNavController().navigate(R.id.action_charactersFragment_to_sortFragment)
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initCharacters()
        observeInitialLoadState()
        controlBackStack()

        setupMenu()
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

    override fun onQueryTextSubmit(query: String?): Boolean {
        return query?.let {
            viewModel.currentSearchQuery = it
            viewModel.searchCharacter()
            true
        } ?: false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return true
    }

    override fun onMenuItemActionExpand(item: MenuItem): Boolean {
        return true
    }

    override fun onMenuItemActionCollapse(item: MenuItem): Boolean {
        //ação de colapsar item
        viewModel.closeSearch()
        viewModel.searchCharacter()
        return true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        searchView.setOnQueryTextListener(null)
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
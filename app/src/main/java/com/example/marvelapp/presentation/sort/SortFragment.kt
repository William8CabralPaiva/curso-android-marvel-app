package com.example.marvelapp.presentation.sort

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.forEach
import androidx.fragment.app.viewModels
import com.example.core.domain.model.SortingType
import com.example.marvelapp.R
import com.example.marvelapp.databinding.FragmentSortBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SortFragment : BottomSheetDialogFragment() {

    private val viewModel: SortViewModel by viewModels()

    private var _binding: FragmentSortBinding? = null
    private val binding: FragmentSortBinding get() = _binding!!

    private var orderBy = SortingType.ORDER_BY_NAME.value
    private var order = SortingType.ORDER_ASCENDING.value

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentSortBinding.inflate(
        inflater,
        container,
        false
    ).apply {
        _binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setChipGroupListeners()
        observeUiState()
    }

    private fun setChipGroupListeners() {
        binding.run {

            chipGroupOrderBy.setOnCheckedStateChangeListener { group, checkedIds ->
                val chip = group.findViewById<Chip>(checkedIds[0])
                orderBy = getOrderByValue(chip.id)
            }

            chipGroupOrder.setOnCheckedStateChangeListener { group, checkedIds ->
                val chip = group.findViewById<Chip>(checkedIds[0])
                order = getOrderValue(chip.id)
            }

            buttonApplySort.setOnClickListener {
                viewModel.applySort(orderBy, order)
            }
        }

    }

    private fun observeUiState() {
        viewModel.state.observe(viewLifecycleOwner) {
            when (it) {
                is SortViewModel.UiState.SortingResult -> {
                    binding.chipGroupOrderBy.checkItem(it.storedSorting.first to it.storedSorting.second)
                    binding.chipGroupOrder.checkItem(it.storedSorting.first to it.storedSorting.second)
                }
                is SortViewModel.UiState.ApplyState.Loading -> {
                    binding.flipperApply.displayedChild = FLIPPER_CHILD_PROGRESS
                }
                is SortViewModel.UiState.ApplyState.Success -> {
                    binding.flipperApply.displayedChild = FLIPPER_CHILD_BUTTON
                }
                is SortViewModel.UiState.ApplyState.Error -> {
                    binding.flipperApply.displayedChild = FLIPPER_CHILD_BUTTON
                }
            }
        }
    }

    private fun ChipGroup.checkItem(pairSorting: Pair<String, String>) {
        val orderBy = pairSorting.first
        val order = pairSorting.second
        this.forEach {
            val chip = it as Chip

            if (this.id == binding.chipGroupOrderBy.id) {
                if (getOrderByValue(it.id) == orderBy) {
                    chip.isChecked = true
                }

            } else {
                if (getOrderValue(it.id) == order) {
                    chip.isChecked = true
                }
            }
        }

    }

    private fun getOrderByValue(chipId: Int): String = when (chipId) {
        R.id.chip_name -> SortingType.ORDER_BY_NAME.value
        R.id.chip_modified -> SortingType.ORDER_BY_MODIFIED.value
        else -> SortingType.ORDER_BY_NAME.value
    }

    private fun getOrderValue(chipId: Int): String = when (chipId) {
        R.id.chip_ascending -> SortingType.ORDER_ASCENDING.value
        R.id.chip_descending -> SortingType.ORDER_DESCENDING.value
        else -> SortingType.ORDER_ASCENDING.value
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val FLIPPER_CHILD_BUTTON = 0
        private const val FLIPPER_CHILD_PROGRESS = 1

    }

}
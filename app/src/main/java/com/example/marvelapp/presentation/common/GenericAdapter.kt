package com.example.marvelapp.presentation.common

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.core.domain.model.Comic


//gera função com menos byte caso a função não seja complexa// caso contrario é melhor n usa
inline fun <T : ListItem, VH : GenericViewHolder<T>> getGenericAdapterOf(
    crossinline createViewHolder: (ViewGroup) -> VH
): ListAdapter<T, VH> {

    val diff = GenericDiffCallback<T>()

    //todo criar objeto
    return object : ListAdapter<T, VH>(diff) {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
            return createViewHolder(parent)
        }

        override fun onBindViewHolder(holder: VH, position: Int) {
            holder.bind(getItem(position))
        }

    }

}
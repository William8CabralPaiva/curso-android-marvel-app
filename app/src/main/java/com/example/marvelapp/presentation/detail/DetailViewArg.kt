package com.example.marvelapp.presentation.detail

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

//para passar no args do navigation
@Keep// avisa pro proguard não ofuscar
@Parcelize
data class DetailViewArg(
    val characterId: Int,
    val name: String,
    val imageUrl: String
) : Parcelable

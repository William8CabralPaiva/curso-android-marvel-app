package com.example.core.data.repository

import com.example.core.domain.model.CharacterPaging
import com.example.core.domain.model.Comic
import com.example.core.domain.model.Event

interface CharacterRemoteDataSource {
    //poderia tipar com o <T> e passar na função ver pr anteriores
    suspend fun fetchCharacters(queries: Map<String, String>): CharacterPaging//idem

    suspend fun fetchComics(characterId: Int): List<Comic>

    suspend fun fetchEvents(characterId: Int): List<Event>
}
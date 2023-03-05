package com.example.marvelapp.presentation.characters

import android.app.Application
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.marvelapp.R
import com.example.marvelapp.extension.asJsonString
import com.example.marvelapp.framework.di.BaseUrlModule
import com.example.marvelapp.framework.di.CoroutinesModule
import com.example.marvelapp.launchFragmentInHiltContainer
import com.example.marvelapp.presentation.characters.adapters.CharacterViewHolder
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest//todo precisa dessa anotação para funcionar a injeção de dependencia do hilt
@UninstallModules(
    BaseUrlModule::class,
    CoroutinesModule::class
)////deinstalar de produção para colocar o de teste
class CharactersFragmentTest {

    @get:Rule
    var hiltRule =
        HiltAndroidRule(this)//todo precisa dessa anotação para funcionar a injeção de dependencia do

    private lateinit var server: MockWebServer

    private val navController = TestNavHostController(
        ApplicationProvider.getApplicationContext()
    )

    @Before
    fun setup() {
        server = MockWebServer().apply {
            start(8080)
        }
        launchFragmentInHiltContainer<CharactersFragment>(
            navHostController = navController
        ) //todo launchFragmentInContainer foi trocada pois estamos usando o Hilt
    }

    @After
    fun tearDown() {
        server.shutdown()
    }

    @Test
    fun shouldShowCharacters_whenViewIsCreated(): Unit = runBlocking {
        server.enqueue(MockResponse().setBody("characters_p1.json".asJsonString()))
        delay(500)//delay para dar tempo fazer a configuração do server
        onView(
            withId(R.id.recycle_characters)// ve se o recycle ta visivel
        ).check(matches(isDisplayed()))
    }

    @Test
    fun shouldLoadMoreCharacters_whenPageIsRequested(): Unit = runBlocking {
        //Arrange
        server.apply {
            enqueue(MockResponse().setBody("characters_p1.json".asJsonString()))
            enqueue(MockResponse().setBody("characters_p2.json".asJsonString()))
        }
        delay(500)

        //Action
        onView(
            withId(R.id.recycle_characters)
        ).perform(
            RecyclerViewActions.scrollToPosition<CharacterViewHolder>(20)
        )

        //Assert
        onView(
            withText("Amora")
        ).check(
            matches(isDisplayed())
        )
    }

    @Test
    fun shouldShowErrorView(): Unit = runBlocking {
        server.enqueue(MockResponse().setResponseCode(404))
        delay(500)

        onView(
            withId(R.id.text_initial_load_error)// ve se o recycle ta visivel
        ).check(matches(isDisplayed()))
    }


}
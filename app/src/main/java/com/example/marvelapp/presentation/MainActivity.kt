package com.example.marvelapp.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.marvelapp.R
import com.example.marvelapp.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        //todo configurar toolbar
        setSupportActionBar(binding.toolbarApp)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_container) as NavHostFragment

        navController = navHostFragment.navController

        binding.apply {
            bottomNavMain.setupWithNavController(navController)

            //todo define as rotas principais
            appBarConfiguration = AppBarConfiguration(
                setOf(
                    R.id.charactersFragment,
                    R.id.heroesFragment,
                    R.id.favoritesFragment,
                    R.id.sortFragment
                )
            )

            //todo configurar toolbar
            setupActionBarWithNavController(navController, appBarConfiguration)
            toolbarApp.setupWithNavController(navController, appBarConfiguration)

            navController.addOnDestinationChangedListener { _, destination, _ ->
                val isTopLevelDestination =
                    appBarConfiguration.topLevelDestinations.contains(destination.id)

                if (!isTopLevelDestination) {
                    toolbarApp.setNavigationIcon(R.drawable.ic_back)
                }
            }
        }


    }
}
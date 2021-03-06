package com.example.readdit

import android.os.Bundle
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.example.readdit.databinding.ActivityNavigationBarBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNavigationBarBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNavigationBarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        navController = findNavController(R.id.nav_host_fragment_activity_navigation_bar)

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_explore, R.id.navigation_library, R.id.navigation_notes, R.id.navigation_settings
            )
        )
        navView.setupWithNavController(navController)

        // Hide the nav bar when the user is in splash screen/onboarding screen/sign in screen/sign up screen
        navController.addOnDestinationChangedListener { _, nd: NavDestination, _ ->
            if (nd.id == R.id.navigation_splash_screen ||
                nd.id == R.id.navigation_onboarding_view_pager ||
                nd.id == R.id.navigation_sign_in ||
                nd.id == R.id.navigation_forgot_password ||
                nd.id == R.id.navigation_sign_up ||
                nd.id == R.id.navigation_edit_account_data ||
                nd.id == R.id.navigation_add_notes||
                nd.id == R.id.navigation_save_notes
                    )
            {
                navView.visibility = View.GONE
            }
            else{
                navView.visibility = View.VISIBLE
            }
        }
        navView.setOnItemSelectedListener { item ->
            // In order to get the expected behavior, you have to call default Navigation method manually
            NavigationUI.onNavDestinationSelected(item, navController)

            return@setOnItemSelectedListener true
        }

    }
}
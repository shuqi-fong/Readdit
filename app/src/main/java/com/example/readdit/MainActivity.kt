package com.example.readdit

import android.os.Bundle
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.readdit.databinding.ActivityNavigationBarBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNavigationBarBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNavigationBarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_navigation_bar)

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_explore, R.id.navigation_library, R.id.navigation_thoughts, R.id.navigation_profile
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
                nd.id == R.id.navigation_edit_account_data)
            {
                navView.visibility = View.GONE
            }
            else{
                navView.visibility = View.VISIBLE
            }
        }
    }
}
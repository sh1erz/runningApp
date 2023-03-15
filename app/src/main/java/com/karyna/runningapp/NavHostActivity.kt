package com.karyna.runningapp

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.auth.FirebaseAuth
import com.karyna.runningapp.auth.AuthFragment
import com.karyna.runningapp.auth.AuthFragmentDirections
import com.karyna.runningapp.databinding.ActivityNavHostBinding
import com.karyna.feature.main.R as RMain

class NavHostActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNavHostBinding
    private lateinit var navController: NavController
    private var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    private val authListener: FirebaseAuth.AuthStateListener = FirebaseAuth.AuthStateListener {
        val user = FirebaseAuth.getInstance().currentUser
        if (user == null) navigateToSignIn()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNavHostBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupNavigation(showLogin = firebaseAuth.currentUser == null)
    }

    override fun onStart() {
        super.onStart()
        firebaseAuth.addAuthStateListener(authListener)
    }

    override fun onStop() {
        super.onStop()
        firebaseAuth.removeAuthStateListener(authListener)
    }


    private fun setupNavigation(showLogin: Boolean) {
        navController = findNavController(R.id.nav_host_fragment)
        val navGraph = navController.navInflater.inflate(R.navigation.nav_graph)
        val startDestinationId = if (showLogin) R.id.loginFragment else RMain.id.map_nav_graph
        navGraph.setStartDestination(startDestinationId)
        navController.graph = navGraph
        binding.bottomNavView.setupWithNavController(navController)
        setBottomNavBarVisibility()
    }

    private fun setBottomNavBarVisibility() {
        supportFragmentManager.registerFragmentLifecycleCallbacks(object :
            FragmentManager.FragmentLifecycleCallbacks() {
            override fun onFragmentViewCreated(fm: FragmentManager, f: Fragment, v: View, savedInstanceState: Bundle?) {
                binding.bottomNavView.isVisible = when (f) {
                    is AuthFragment -> false
                    else -> true
                }
            }
        }, true)
    }

    private fun navigateToSignIn() {
        navController.navigate(AuthFragmentDirections.actionGlobalLoginFragment())
    }
}
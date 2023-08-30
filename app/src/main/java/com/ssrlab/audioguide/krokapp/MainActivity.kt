package com.ssrlab.audioguide.krokapp

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.ssrlab.audioguide.krokapp.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var navController: NavController

    private val job = Job()
    private val scope = CoroutineScope(Dispatchers.IO + job)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpUtils()
    }

    private fun setUpUtils() {

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.main_nav_host) as NavHostFragment
        navController = navHostFragment.navController

        bottomNavigationView = binding.mainBottomNav
        bottomNavigationView.inflateMenu(R.menu.main_graph_menu)
        bottomNavigationView.setupWithNavController(navController)
    }

    fun setToolbar(title: String, isBackButtonShown: Boolean = false, action: () -> Unit) {
        binding.apply {
            mainToolbarTitle.text = title

            if (isBackButtonShown) mainToolbarButton.visibility = View.VISIBLE
            else mainToolbarButton.visibility = View.GONE
            mainToolbarButton.setOnClickListener { action() }
        }
    }

    fun getScope() = scope
}
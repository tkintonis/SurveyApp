package com.example.surveyApp.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.surveyApp.App
import com.example.surveyApp.R
import com.example.surveyApp.databinding.ActivityMainBinding
import com.example.surveyApp.di.components.ActivitySubcomponent

class MainActivity : AppCompatActivity() {

    lateinit var activitySubcomponent: ActivitySubcomponent
        private set
    private var binding: ActivityMainBinding? = null
    private var navController: NavController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        activitySubcomponent = (application as App).applicationComponent.activityComponent().create(this)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding?.root)
        initNavigationFlow()
    }

    private fun initNavigationFlow() {
        val navHost = supportFragmentManager.findFragmentById(R.id.main_nav_host_fragment) as NavHostFragment
        navController = navHost.navController.apply { setGraph(R.navigation.main_nav_graph, intent.extras) }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}
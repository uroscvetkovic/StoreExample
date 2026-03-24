package com.example.storeexample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import dagger.hilt.android.AndroidEntryPoint
import androidx.navigation.fragment.NavHostFragment
import com.example.storeexample.databinding.ActivityMainBinding

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        binding.bottomNavView.setup(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.productDetailFragment) {
                binding.bottomNavView.animate()
                    .translationY(binding.bottomNavView.height.toFloat() + 20f)
                    .alpha(0f)
                    .setDuration(300)
                    .withEndAction { binding.bottomNavView.isVisible = false }
                    .start()
            } else {
                binding.bottomNavView.isVisible = true
                binding.bottomNavView.animate()
                    .translationY(0f)
                    .alpha(1f)
                    .setDuration(300)
                    .start()
            }
        }
    }
}

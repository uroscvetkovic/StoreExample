package com.example.storeexample.presentation.common

import android.animation.ValueAnimator
import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.view.doOnLayout
import androidx.core.widget.ImageViewCompat
import androidx.navigation.NavController
import com.example.storeexample.R
import com.example.storeexample.databinding.ViewBottomNavBinding

class BottomNavView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    private val binding = ViewBottomNavBinding.inflate(LayoutInflater.from(context), this)

    fun setup(navController: NavController) {
        binding.navItemProducts.setOnClickListener {
            if (navController.currentDestination?.id != R.id.productListFragment) {
                navController.navigate(R.id.productListFragment)
            }
        }
        binding.navItemFavorites.setOnClickListener {
            if (navController.currentDestination?.id != R.id.favoritesFragment) {
                navController.navigate(R.id.favoritesFragment)
            }
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            doOnLayout { selectDestination(destination.id) }
        }
    }

    private fun selectDestination(destinationId: Int) {
        initIndicator()
        val productsActive = destinationId == R.id.productListFragment
        slideIndicatorTo(if (productsActive) binding.navItemProducts else binding.navItemFavorites)
        animateIconTint(binding.imageViewProducts, productsActive)
        animateIconTint(binding.imageViewFavorites, !productsActive)
    }

    private fun initIndicator() {
        if (binding.navIndicator.width != 0) return
        val item = binding.navItemProducts
        binding.navIndicator.layoutParams = LayoutParams(item.width, item.height)
        binding.navIndicator.translationX = item.left.toFloat()
    }

    private fun slideIndicatorTo(item: View) {
        binding.navIndicator.animate()
            .translationX(item.left.toFloat())
            .setDuration(300)
            .setInterpolator(DecelerateInterpolator())
            .start()
    }

    private fun animateIconTint(icon: ImageView, isSelected: Boolean) {
        val from = ImageViewCompat.getImageTintList(icon)?.defaultColor
            ?: ContextCompat.getColor(context, R.color.nav_unselected)
        val to = ContextCompat.getColor(context, if (isSelected) R.color.nav_selected else R.color.nav_unselected)
        ValueAnimator.ofArgb(from, to).apply {
            duration = 250
            addUpdateListener {
                ImageViewCompat.setImageTintList(icon, ColorStateList.valueOf(it.animatedValue as Int))
            }
            start()
        }
    }
}

package com.example.storeexample.presentation.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storeexample.R
import com.example.storeexample.data.local.entity.FavoriteProduct
import com.example.storeexample.data.remote.model.Product
import com.example.storeexample.databinding.FragmentFavoritesBinding
import com.example.storeexample.presentation.products.ProductAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FavoritesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = ProductAdapter(
            onProductClick = { productId ->
                findNavController().navigate(
                    R.id.action_favoritesFragment_to_productDetailFragment,
                    bundleOf("productId" to productId)
                )
            },
            onFavoriteClick = { product ->
                viewModel.removeFavorite(product.id)
            }
        )

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.favorites.collect { favorites ->
                    adapter.submitList(favorites.map { it.toProduct() })
                    adapter.setFavorites(favorites.map { it.id }.toSet())
                    binding.textViewEmpty.isVisible = favorites.isEmpty()
                    binding.recyclerView.isVisible = favorites.isNotEmpty()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun FavoriteProduct.toProduct() = Product(
        id = id,
        title = title,
        category = category,
        price = price,
        rating = rating,
        thumbnail = thumbnail,
        description = "",
        discountPercentage = 0.0,
        stock = 0,
        brand = null,
        images = emptyList()
    )
}

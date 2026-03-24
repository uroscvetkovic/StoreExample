package com.example.storeexample.presentation.products

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.storeexample.R
import com.bumptech.glide.Glide
import com.example.storeexample.databinding.FragmentProductDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProductDetailFragment : Fragment() {

    private var _binding: FragmentProductDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProductDetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonBack.setOnClickListener { findNavController().navigateUp() }
        binding.buttonFavorite.setOnClickListener { viewModel.toggleFavorite() }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isFavorite.collect { isFav ->
                    binding.buttonFavorite.setImageResource(
                        if (isFav) R.drawable.ic_favorite else R.drawable.ic_favorite_border
                    )
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    binding.progressBar.isVisible = state is ProductDetailUiState.Loading
                    binding.scrollView.isVisible = state is ProductDetailUiState.Success
                    binding.textViewError.isVisible = state is ProductDetailUiState.Error

                    if (state is ProductDetailUiState.Success) {
                        val p = state.product
                        binding.textViewTitle.text = p.title
                        binding.textViewCategory.text = p.category
                        binding.textViewPrice.text = "$" + "%.2f".format(p.price)
                        binding.textViewRating.text = "★ %.1f".format(p.rating)
                        binding.textViewStock.text = "${p.stock} in stock"
                        binding.textViewDescription.text = p.description
                        binding.textViewBrand.text = p.brand ?: ""
                        binding.textViewBrand.isVisible = p.brand != null
                        Glide.with(binding.imageViewProduct)
                            .load(p.thumbnail)
                            .into(binding.imageViewProduct)
                    }

                    if (state is ProductDetailUiState.Error) {
                        binding.textViewError.text = state.message
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

package com.batyrzhan.autoparts.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.batyrzhan.autoparts.MainActivity
import com.batyrzhan.autoparts.R
import com.batyrzhan.autoparts.adapters.ProductsAdapter
import com.batyrzhan.autoparts.contracts.ProductsContract
import com.batyrzhan.autoparts.databinding.ProductsFragmentBinding
import com.batyrzhan.autoparts.model.Product
import com.batyrzhan.autoparts.presenters.ProductsPresenter
import com.batyrzhan.autoparts.utils.Utils
import com.batyrzhan.common.base.BaseFragment
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class ProductsFragment : BaseFragment<ProductsContract.View, ProductsContract.Presenter>(),
    ProductsContract.View {
    private var _binding: ProductsFragmentBinding? = null
    private val binding get() = _binding
    override val presenter: ProductsPresenter by viewModel()
    private var productLayoutManager: LinearLayoutManager? = null
    private var productAdapter: ProductsAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ProductsFragmentBinding.inflate(inflater, container, false)
        return binding?.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.let {
            setupToolbar()
            setAdapter()

            binding?.filterBtn?.setOnClickListener {
                activity?.findNavController(R.id.nav_host_fragment)
                    ?.navigate(R.id.action_productsFragment_to_filterFragment)
            }
        }
    }

    //sort
    private fun setupToolbar() {
        binding?.toolbarProductFr?.inflateMenu(R.menu.toolbar_product_menu_sort)
        binding?.toolbarProductFr?.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.sortAscPrice -> {
                    presenter.checkComparatorSort(Product.ASC_TOURS_PRICE)
                    return@setOnMenuItemClickListener true
                }
                R.id.sortDescPrice -> {
                    presenter.checkComparatorSort(Product.DESC_TOURS_PRICE)
                    return@setOnMenuItemClickListener true
                }
            }
            false
        }
    }

    private fun setAdapter() {
        productAdapter = ProductsAdapter(requireContext())
        productLayoutManager = GridLayoutManager(
            activity,
            3,
            GridLayoutManager.VERTICAL,
            false
        )
        binding?.productsRecView?.apply {
            layoutManager = productLayoutManager
            adapter = productAdapter
            setHasFixedSize(true)
        }
        getData()
    }

    private fun getData() {
        // title toolbar activity
        (activity as MainActivity).supportActionBar?.title =
            arguments?.getString("catName", "").orEmpty()
        if (Utils.isNetworkAvailable(requireContext())) {
            showProgress()
            presenter.getProducts(arguments?.getString("catId", "").orEmpty())
        } else
            view?.let { Snackbar.make(it, "Нет подключения к интернету!", 1500).show() }

    }

    override fun showProgress() {
        binding?.progressProductsFragment?.visibility = View.VISIBLE
    }

    override fun visibleEmptyLayout() {
        binding?.emptyProductLayout?.visibility = View.VISIBLE
    }


    override fun setProductsToAdapter(list: List<Product>) {
        productAdapter = ProductsAdapter(requireContext())
        binding?.productsRecView?.apply {
            layoutManager = productLayoutManager
            adapter = productAdapter
        }
        productAdapter?.setList(list)
    }

    override fun hideProgress() {
        binding?.progressProductsFragment?.visibility = View.GONE
    }

    override fun goneEmptyLayout() {
        binding?.emptyProductLayout?.visibility = View.GONE
    }
}



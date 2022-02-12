package com.batyrzhan.autoparts.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.batyrzhan.autoparts.adapters.ProductsAdapter
import com.batyrzhan.autoparts.contracts.SearchContract
import com.batyrzhan.autoparts.databinding.SearchFragmentBinding
import com.batyrzhan.autoparts.model.Product
import com.batyrzhan.autoparts.presenters.SearchPresenter
import com.batyrzhan.autoparts.utils.Utils
import com.batyrzhan.common.base.BaseFragment
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel


class SearchFragment : BaseFragment<SearchContract.View, SearchContract.Presenter>(),
    SearchContract.View {
    private var _binding: SearchFragmentBinding? = null
    private val binding get() = _binding
    override val presenter: SearchPresenter by viewModel()
    private var productLayoutManager: LinearLayoutManager? = null
    private var productAdapter: ProductsAdapter? = null
    private var inputMethodManager: InputMethodManager? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = SearchFragmentBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setProductAdapter()
        searchText()
    }

    private fun searchText() {
    //    binding?.searchTxt?.requestFocus()
//        inputMethodManager = context
//            ?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//        inputMethodManager?.toggleSoftInput(
//            InputMethodManager.SHOW_FORCED,
//            InputMethodManager.HIDE_IMPLICIT_ONLY
//        )
        binding?.searchTxt?.addTextChangedListener {
            presenter.filter(it.toString())
        }
    }

    private fun setProductAdapter() {
        productAdapter = ProductsAdapter(requireContext())
        productLayoutManager = GridLayoutManager(
            activity,
            3,
            GridLayoutManager.VERTICAL,
            false
        )
        binding?.recMain?.apply {
            layoutManager = productLayoutManager
            adapter = productAdapter
            setHasFixedSize(true)
        }
        getDataProduct()
    }


    override fun setFilteredToAdapter(list: List<Product>) {
        if (list.isNotEmpty()) {
            binding?.recMain?.visibility = View.VISIBLE
            binding?.txtSearchEmpty?.visibility = View.GONE
            productAdapter?.filtered(list as MutableList<Product>)
        } else binding?.apply {
            recMain.visibility = View.GONE
            txtSearchEmpty.visibility = View.VISIBLE
        }
    }

    override fun setProductsToAdapter(list: List<Product>) {
        productAdapter?.setList(list)
    }

    override fun hideProgress() {
        binding?.progressRecProduct?.visibility = View.GONE
    }

    private fun getDataProduct() {
        if (Utils.isNetworkAvailable(requireContext())) presenter.getProductsRecently()
        else view?.let { Snackbar.make(it, "Нет подключения к интернету!", 1500).show() }
    }

    override fun showProgress() {
        binding?.progressRecProduct?.visibility = View.VISIBLE
    }
}
package com.batyrzhan.autoparts.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.batyrzhan.autoparts.adapters.CatalogAdapter
import com.batyrzhan.autoparts.contracts.CatalogContract
import com.batyrzhan.autoparts.databinding.CatalogFragmentBinding
import com.batyrzhan.autoparts.model.PartCategory
import com.batyrzhan.autoparts.presenters.CatalogPresenter
import com.batyrzhan.autoparts.utils.LoadingDialog
import com.batyrzhan.autoparts.utils.Utils
import com.batyrzhan.common.base.BaseFragment
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel

class CatalogFragment : BaseFragment<CatalogContract.View, CatalogContract.Presenter>(),
    CatalogContract.View {

    private var _binding: CatalogFragmentBinding? = null
    private val binding get() = _binding
    override val presenter: CatalogPresenter by viewModel()
    private var catalogAdapter: CatalogAdapter? = null
    private var dialog: LoadingDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = CatalogFragmentBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        activity?.let {
            setAdapter()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun setAdapter() {
        catalogAdapter = CatalogAdapter(requireContext())
        val catalogLayoutManager = LinearLayoutManager(
            context, LinearLayoutManager.VERTICAL,
            false
        )
        binding?.catalogRecView?.apply {
            layoutManager = catalogLayoutManager
            adapter = catalogAdapter
        }
        getData()
    }

    private fun getData() {
        binding?.progressCatalog?.visibility = View.VISIBLE
        if (Utils.isNetworkAvailable(requireContext())) {
            dialog?.startDialog("", "")
            presenter.getDataCatalogs()
        } else view?.let { Snackbar.make(it, "Нет подключения к интернету!", 1500)
            .show() }

    }

    override fun showAllDataFromDB(list: List<PartCategory>) {
        catalogAdapter?.setList(list)
        binding?.progressCatalog?.visibility = View.GONE
    }


}
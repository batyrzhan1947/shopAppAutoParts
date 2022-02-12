package com.batyrzhan.autoparts.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.batyrzhan.autoparts.adapters.HelpAdapter
import com.batyrzhan.autoparts.contracts.InfoContract
import com.batyrzhan.autoparts.databinding.HelpFragmentBinding
import com.batyrzhan.autoparts.model.Help
import com.batyrzhan.autoparts.presenters.InfoPresenter
import com.batyrzhan.autoparts.utils.Utils
import com.batyrzhan.common.base.BaseFragment
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel

class InfoFragment : BaseFragment<InfoContract.View, InfoContract.Presenter>(), InfoContract.View {
    override val presenter: InfoPresenter by viewModel()
    private var _binding: HelpFragmentBinding? = null
    private val binding get() = _binding
    private var helpLayoutManager: LinearLayoutManager? = null
    private var helpAdapter: HelpAdapter? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = HelpFragmentBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.let {
            setAdapter()
        }
    }


    private fun setAdapter() {
        helpAdapter = HelpAdapter(requireContext())
        helpLayoutManager = LinearLayoutManager(
            context, LinearLayoutManager.VERTICAL,
            false
        )
        binding?.recyclerViewHelp?.apply {
            layoutManager = helpLayoutManager
            adapter = helpAdapter
        }
        binding?.progressHelpFragment?.visibility = View.VISIBLE
        if (Utils.isNetworkAvailable(requireContext())) {
            presenter.getDataHelps()
        } else view?.let {
            Snackbar.make(it, "Нет подключения к интернету!", 1500)
                .show() }

    }

    override fun showAllDataFromDB(list: List<Help>) {
        if (list.isNotEmpty()) {
            helpAdapter?.setList(list)
            binding?.progressHelpFragment?.visibility = View.GONE
        } else {
            view?.let { Snackbar.make(it, "Пустой список!", 1500) }
        }
    }
}
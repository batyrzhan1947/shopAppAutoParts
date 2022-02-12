package com.batyrzhan.autoparts.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.batyrzhan.autoparts.adapters.HistoryOrderAdapter
import com.batyrzhan.autoparts.contracts.HistoryOrdersContract
import com.batyrzhan.autoparts.database.model.Order
import com.batyrzhan.autoparts.databinding.HistoryOrderFragmentBinding
import com.batyrzhan.autoparts.presenters.HistoryOrdersPresenter
import com.batyrzhan.common.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class HistoryOrdersFragment :
    BaseFragment<HistoryOrdersContract.View, HistoryOrdersContract.Presenter>(),
    HistoryOrdersContract.View {
    private var _binding: HistoryOrderFragmentBinding? = null
    private val binding get() = _binding
    override val presenter: HistoryOrdersPresenter by viewModel()
    private var historyLayoutManager: LinearLayoutManager? = null
    private var historyAdapter: HistoryOrderAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = HistoryOrderFragmentBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapter()
    }

    private fun setAdapter() {
        context?.let { context ->
            historyAdapter = HistoryOrderAdapter(context,
                clickListener = {
                    val bundle = Bundle()
                    bundle.putString("orderCode", it.codeOrder)
                    activity?.supportFragmentManager?.let { it1 ->
                        OrderBottomSheetDialog(bundle).show(it1, null)
                    }
                })
        }

        historyLayoutManager = LinearLayoutManager(
            context, LinearLayoutManager.VERTICAL,
            false
        )
        binding?.recyclerHistory?.apply {
            layoutManager = historyLayoutManager
            adapter = historyAdapter
        }
        presenter.getDataHistoryOrders()
    }

    override fun showList(list: List<Order>) {
        historyAdapter?.setList(list)
    }
}
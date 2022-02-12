package com.batyrzhan.autoparts.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.preference.PreferenceManager
import com.batyrzhan.autoparts.contracts.OrderSheetDialogContract
import com.batyrzhan.autoparts.databinding.DialogBottomSheetBinding
import com.batyrzhan.autoparts.presenters.OrderSheetPresenter
import com.batyrzhan.common.base.BaseBottomSheetDialogFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class OrderBottomSheetDialog(private val bundle: Bundle) :
    BaseBottomSheetDialogFragment<OrderSheetDialogContract.View, OrderSheetDialogContract.Presenter>(),
    OrderSheetDialogContract.View {
    private var _binding: DialogBottomSheetBinding? = null
    private val binding get() = _binding
    override val presenter: OrderSheetPresenter by viewModel()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DialogBottomSheetBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOrder()
    }

    private fun setOrder() {
        val code = bundle.getString("orderCode", "").orEmpty()
        presenter.setOrder(code)
    }

    override fun showOrderInfo(
        codeOrder: String,
        orderTotal: String,
        dateTime: String,
        orderList: String,
        statusOrder:String
    ) {
        binding?.statusOrder?.text = statusOrder
        binding?.codeOrderTxtView?.text = codeOrder
        binding?.totalOrderTxtView?.text = orderTotal
        binding?.dateTimeOrderTxtView?.text = dateTime
        binding?.listOrderTxtView?.text = orderList
    }
}
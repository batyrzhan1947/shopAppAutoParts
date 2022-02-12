package com.batyrzhan.autoparts.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.batyrzhan.autoparts.MainActivity
import com.batyrzhan.autoparts.R
import com.batyrzhan.autoparts.adapters.ProductsAdapter
import com.batyrzhan.autoparts.adapters.SliderNotifyAdapter
import com.batyrzhan.autoparts.contracts.MainPageContract
import com.batyrzhan.autoparts.contracts.ProductsContract
import com.batyrzhan.autoparts.databinding.MainFragmentBinding
import com.batyrzhan.autoparts.model.Product
import com.batyrzhan.autoparts.model.ShopNotification
import com.batyrzhan.autoparts.presenters.MainPagePresenter
import com.batyrzhan.autoparts.presenters.ProductsPresenter
import com.batyrzhan.autoparts.utils.Utils
import com.batyrzhan.common.base.BaseFragment
import com.google.android.material.snackbar.Snackbar
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType
import com.smarteist.autoimageslider.SliderAnimations
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainFragment : BaseFragment<MainPageContract.View, MainPageContract.Presenter>(),
  MainPageContract.View {
    override val presenter: MainPagePresenter by viewModel()
    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding
    private var notifyAdapter: SliderNotifyAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = MainFragmentBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.disposeCompositeDisposable()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapter()
        refreshData()
        (activity as MainActivity).setCountCart()

        binding?.searchTxtView?.setOnClickListener {
            view.findNavController().navigate(R.id.action_mainFragment_to_searchFragment)
        }

    }



    private fun setAdapter() {
        notifyAdapter = SliderNotifyAdapter()
        notifyAdapter?.let { binding?.sliderView?.setSliderAdapter(it) }
        binding?.sliderView?.setIndicatorEnabled(true)
        binding?.sliderView?.setIndicatorVisibility(true)
        binding?.sliderView?.setIndicatorAnimation(IndicatorAnimationType.WORM)
        binding?.sliderView?.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION)
        binding?.sliderView?.startAutoCycle()
        getData()
    }

    private fun getData() {
        binding?.progressMainFragment?.visibility = View.VISIBLE
        if(Utils.isNetworkAvailable(requireContext())) presenter.getDataNotifications()
        else showSnackBar("Нет подключения к интернету!")
    }

    private fun refreshData() {
        binding?.swipeMainFragment?.setOnRefreshListener {
            if (Utils.isNetworkAvailable(requireContext())) getData()
             else showSnackBar("Нет подключения к интернету!")
            binding?.swipeMainFragment?.isRefreshing = false
        }
    }

    override fun showSnackBar(txt: String) {
        view?.let { Snackbar.make(it, txt, 1500).show() }
    }



    override fun showAllNotificationsFromDB(list: List<ShopNotification>) {
        if (list.isNotEmpty()) {
            notifyAdapter?.setList(list)
            binding?.progressMainFragment?.visibility = View.GONE
        } else showSnackBar("Пустой список!")

    }

}
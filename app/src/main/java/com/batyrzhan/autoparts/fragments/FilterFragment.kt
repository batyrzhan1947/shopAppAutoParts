package com.batyrzhan.autoparts.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import com.batyrzhan.autoparts.R
import com.batyrzhan.autoparts.databinding.FilterFragmentBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import java.text.NumberFormat
import java.util.*
import kotlin.math.roundToInt


class FilterFragment:Fragment() {
    private var _binding:FilterFragmentBinding? = null
    private val binding get() = _binding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
      _binding = FilterFragmentBinding.inflate(inflater, container, false)
        return binding?.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setRangeSlider()

        binding?.brandTitle?.setOnClickListener {
            activity?.supportFragmentManager?.let { it1 ->
                FilterBottomSheetDialog().show(it1, null)
            }
        }
    }

    private fun setRangeSlider(){
        binding?.rangeSlider?.setLabelFormatter { value: Float ->
            val format = NumberFormat.getCurrencyInstance()
            format.maximumFractionDigits = 0
            format.currency = Currency.getInstance("KZT")
            format.format(value.toDouble())
        }

        binding?.rangeSlider?.addOnChangeListener { rangeSlider, _, _ ->
            binding?.startValue?.text = rangeSlider.values[0].roundToInt().toString()
            binding?.endValue?.text = rangeSlider.values[1].roundToInt().toString()
        }
    }

}
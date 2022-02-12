 package com.batyrzhan.autoparts.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.batyrzhan.autoparts.adapters.MarksAdapter
import com.batyrzhan.autoparts.api.AdminPanelApiService
import com.batyrzhan.autoparts.database.AppDataBase
import com.batyrzhan.autoparts.databinding.FilterBottomSheetBinding
import com.batyrzhan.autoparts.model.MarkaAuto
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class FilterBottomSheetDialog : BottomSheetDialogFragment() {
    private var _binding: FilterBottomSheetBinding? = null
    private val binding get() = _binding
    private var db: AppDataBase? = null
    private var compositeDisposable: CompositeDisposable? = null
    private var apiService: AdminPanelApiService? = null
    private var marksLayoutManager: LinearLayoutManager? = null
    private var marksAdapter: MarksAdapter? = null
    private var markaList = mutableListOf<MarkaAuto>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FilterBottomSheetBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        compositeDisposable?.dispose()
        super.onDestroy()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        apiService = AdminPanelApiService.create()
        setAdapterMarks()
    }


    private fun setAdapterMarks(){
        marksLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        marksAdapter = context?.let { MarksAdapter(it) }
        binding?.recyclerMarkaAuto?.apply {
            adapter = marksAdapter
            layoutManager = marksLayoutManager
        }
        getMarksData()
    }

    private fun getMarksData(){
        binding?.progressFilter?.visibility = View.VISIBLE
        apiService?.getMarksAuto()
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe(
                {
                    markaList.clear()
                    markaList.addAll(it)
                    marksAdapter?.setList(markaList)
                    binding?.progressFilter?.visibility = View.GONE
                }, { th->
                    Log.e("App", "getMarksData: ",th )
                    binding?.progressFilter?.visibility = View.GONE
                }
            ).also { compositeDisposable  }
    }


}
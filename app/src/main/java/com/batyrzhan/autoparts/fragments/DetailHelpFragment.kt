package com.batyrzhan.autoparts.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import androidx.preference.PreferenceManager
import com.batyrzhan.autoparts.MainActivity
import com.batyrzhan.autoparts.contracts.DetailHelpContract
import com.batyrzhan.autoparts.databinding.HelpDetailFragmentBinding
import com.batyrzhan.autoparts.model.Help
import com.batyrzhan.autoparts.presenters.DetailHelpPresenter
import com.batyrzhan.autoparts.utils.Constant
import com.batyrzhan.autoparts.utils.Utils
import com.batyrzhan.common.base.BaseFragment
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailHelpFragment : BaseFragment<DetailHelpContract.View, DetailHelpContract.Presenter>(),
    DetailHelpContract.View {
    private var _binding: HelpDetailFragmentBinding? = null
    private val binding get() = _binding
    override val presenter: DetailHelpPresenter by viewModel()
    private var helpID: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = HelpDetailFragmentBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getDetailHelp()
    }

    override fun showProgress() {
        binding?.progressDetailHelp?.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        binding?.progressDetailHelp?.visibility = View.GONE
    }

    private fun getDetailHelp() {
        helpID = arguments?.getInt("helpId", 0)
        // title toolbar activity
        (activity as MainActivity).supportActionBar?.title =
            arguments?.getString("helpTitle", "").orEmpty()
        if (Utils.isNetworkAvailable(requireContext())) {
            helpID?.let { presenter.getDetailHelp(it) }
        } else view?.let { Snackbar.make(it, "Нет подключения к интернету!", 1500).show() }
    }

    override fun showText(item: List<Help>) {
        binding?.contentWebView?.settings?.defaultTextEncodingName = "UTF-8"
        binding?.contentWebView?.isFocusableInTouchMode = false
        binding?.contentWebView?.isFocusable = false
        val webSettings: WebSettings? = binding?.contentWebView?.settings
        webSettings?.defaultFontSize = 16
        true.also { it1 -> webSettings?.javaScriptEnabled = it1 }
        val mimeType = "text/html; charset=UTF-8"
        val encoding = "utf-8"
        val text = ("<html><head>"
                + "<style type=\"text/css\">body{color: #525252;}"
                + "</style></head>"
                + "<body>"
                + item[0].content
                + "</body></html>")

        val textRtl = ("<html dir='rtl'><head>"
                + "<style type=\"text/css\">body{color: #525252;}"
                + "</style></head>"
                + "<body>"
                + item[0].content
                + "</body></html>")

        if (Constant.ENABLE_RTL_MODE) {
            binding?.contentWebView?.loadDataWithBaseURL(
                null, textRtl, mimeType, encoding, null
            )
        } else {
            binding?.contentWebView?.loadDataWithBaseURL(
                null, text, mimeType, encoding, null
            )
        }
    }
}
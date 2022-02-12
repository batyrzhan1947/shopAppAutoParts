package com.batyrzhan.autoparts.fragments

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import androidx.preference.PreferenceManager
import com.batyrzhan.autoparts.MainActivity
import com.batyrzhan.autoparts.contracts.DetailProductContract
import com.batyrzhan.autoparts.databinding.DetailProductFragmentBinding
import com.batyrzhan.autoparts.model.MarksProduct
import com.batyrzhan.autoparts.model.Product
import com.batyrzhan.autoparts.presenters.DetailProductPresenter
import com.batyrzhan.autoparts.utils.Constant
import com.batyrzhan.autoparts.utils.Utils
import com.batyrzhan.common.base.BaseFragment
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailProductFragment :
    BaseFragment<DetailProductContract.View, DetailProductContract.Presenter>(),
    DetailProductContract.View {
    private var _binding: DetailProductFragmentBinding? = null
    private val binding get() = _binding
    override val presenter: DetailProductPresenter by viewModel()
    private var productID: Int? = null
    private var tax: Double? = null
    private var currencyCode: String? = null
    private var productQuantity: Int? = null
    private var productPrice: Double = 0.0
    private var imgProduct: String? = null
    private var prefs: SharedPreferences? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DetailProductFragmentBinding.inflate(inflater, container, false)
        return binding?.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        presenter.clearDispose()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prefs = context?.let { PreferenceManager.getDefaultSharedPreferences(it) }
        getDetailProduct()
        binding?.cartDetailBtn?.setOnClickListener {
            inputData()
        }

    }

    override fun showProgress() {
        binding?.progressDetProdfr?.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        binding?.progressDetProdfr?.visibility = View.GONE
    }

    override fun showMarksAuto(item: MarksProduct) {
        binding?.markaTitleTxtView?.text = "Марка автомибиля: ${item.marks}"
    }


    override fun showText(item: Product) {
        Picasso.get().load(Constant.BASE_URL + "/upload/product/" + item.p_img)
            .fit().centerCrop().into(binding?.productImgDetail)
        imgProduct = item.p_img
        binding?.productNameDetail?.text = item.p_name
        binding?.priceProductDetail?.text =
            item.p_price.toInt().toString() + " ${item.currency_code}"
        productPrice = item.p_price
        binding?.brandTitleTxtView?.text = "Бренд: ${item.p_brand}"
        binding?.oemTitleTxtView?.text = "Номер oem: ${item.p_oem}"
        binding?.articulTitleTxtView?.text =
            "Артикул производителя: ${item.p_articul}"
        binding?.typePrTxtView?.text =
            "Дополнительно: ${item.p_type} ${item.p_target}"

        binding?.descInpTxtView?.settings?.defaultTextEncodingName = "UTF-8"
        binding?.descInpTxtView?.isFocusableInTouchMode = false
        binding?.descInpTxtView?.isFocusable = false

        val webSettings: WebSettings? = binding?.descInpTxtView?.settings
        webSettings?.defaultFontSize = 16
        true.also { it1 -> webSettings?.javaScriptEnabled = it1 }
        val mimeType = "text/html; charset=UTF-8"
        val encoding = "utf-8"
        val text = ("<html><head>"
                + "<style type=\"text/css\">body{color: #525252;}"
                + "</style></head>"
                + "<body>"
                + item.p_description
                + "</body></html>")

        val textRtl = ("<html dir='rtl'><head>"
                + "<style type=\"text/css\">body{color: #525252;}"
                + "</style></head>"
                + "<body>"
                + item.p_description
                + "</body></html>")


        if (Constant.ENABLE_RTL_MODE) {
            binding?.descInpTxtView?.loadDataWithBaseURL(
                null, textRtl, mimeType, encoding, null
            )
        } else {
            binding?.descInpTxtView?.loadDataWithBaseURL(
                null, text, mimeType, encoding, null
            )
        }
        tax = item.tax.toDouble()
        currencyCode = item.currency_code
        productQuantity = item.p_quantity
    }

    private fun getDetailProduct() {
        showProgress()
        if (Utils.isNetworkAvailable(requireContext())) {
            productID = arguments?.getString("productId")?.toInt()
            productID?.let { presenter.getDetailProduct(it) }
        } else view?.let { Snackbar.make(it, "Нет подключения к интернету!", 1500).show() }
    }

    private fun inputData() {
        val countProduct = binding?.elegantBtn?.number.toString()
        presenter.inputData(
            countProduct, productQuantity ?: 0,
            productID ?: 0, productPrice,
            productNameDetail = binding?.productNameDetail?.text?.toString().orEmpty(),
            imgProduct ?: "", currencyCode ?: ""
        )

        (activity as MainActivity).setCountCart()
    }

    override fun showSnackBar(msg: Int) {
        view?.let { Snackbar.make(it, msg, 2000).show() }
    }
}

package com.batyrzhan.autoparts.fragments

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.navigation.findNavController
import com.android.volley.toolbox.Volley
import com.batyrzhan.autoparts.R
import com.batyrzhan.autoparts.contracts.CheckOutContract
import com.batyrzhan.autoparts.databinding.CheckoutFragmentBinding
import com.batyrzhan.autoparts.presenters.CheckOutPresenter
import com.batyrzhan.autoparts.utils.Constant
import com.batyrzhan.autoparts.utils.LoadingDialog
import com.batyrzhan.common.base.BaseFragment
import com.google.android.material.snackbar.Snackbar
import com.onesignal.OneSignal
import org.koin.androidx.viewmodel.ext.android.viewModel


class CheckOutFragment : BaseFragment<CheckOutContract.View, CheckOutContract.Presenter>(),
    CheckOutContract.View {
    override val presenter: CheckOutPresenter by viewModel()
    private var _binding: CheckoutFragmentBinding? = null
    private val binding get() = _binding
    private var strOrderTotal: String = ""
    private var strOrderList: String = ""
    private var dataOrderList: String? = null

    private var dialog: LoadingDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = CheckoutFragmentBinding.inflate(inflater, container, false)
        return binding?.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog = LoadingDialog(this)

        presenter.setUserInfo()
        presenter.getSpinnerData()
        presenter.getDataFromDB()
        submitOrder()
        getTextCheckedRadioDtn()

    }


    private fun submitOrder() {
        binding?.btnSubmitOrder?.setOnClickListener {
            getValueFromEditText()
        }
    }

    private fun getValueFromEditText() {
        val strName = binding?.edtName?.text.toString()
        val strEmail = binding?.edtEmail?.text.toString()
        val strPhone = binding?.edtPhone?.text.toString()
        val strAddress = binding?.edtAddress?.text.toString()
        val strShipping = binding?.edtShipping?.text.toString()
        strOrderList = binding?.edtOrderList?.text.toString()
        strOrderTotal = binding?.edtOrderTotal?.text.toString()
        val strComment = binding?.edtComment?.text?.toString().orEmpty()

        if (strName == "") {
            binding?.edtName?.error = "Введите имя!"
            binding?.edtName?.requestFocus()
            return
        }
        if (TextUtils.isEmpty(strEmail)) {
            binding?.edtEmail?.error = "Введите email почту!"
            binding?.edtEmail?.requestFocus()
            return
        }
        if (TextUtils.isEmpty(strPhone)) {
            binding?.edtPhone?.error = "Введите номер телефона!"
            binding?.edtPhone?.requestFocus()
            return
        }
        if (TextUtils.isEmpty(strAddress)) {
            binding?.edtAddress?.error = "Введите Ваш адрес!"
            binding?.edtAddress?.requestFocus()
            return
        }
        if (strShipping.equals("", ignoreCase = true)) showSnackBar(R.string.checkout_fill_form)
        else if(binding?.rBtnCash?.isChecked != true && binding?.rBtnCard?.isChecked != true
            && binding?.rBtnCurier?.isChecked != true){
            showSnackBar(R.string.select_payment_type)
        } else {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle(R.string.checkout_dialog_title)
            builder.setMessage(R.string.checkout_dialog_msg)
            builder.setCancelable(false)
            builder.setPositiveButton(
                resources.getString(R.string.dialog_option_yes)
            ) { _, _ ->
                dialog?.startDialog(
                    getString(R.string.checkout_submit_title),
                    getString(R.string.checkout_submit_msg)
                )

                val request = presenter.setParamsPOST(
                    strName,
                    strEmail,
                    strPhone,
                    binding?.txtRb?.text.toString(),
                    strAddress,
                    strShipping,
                    strOrderList,
                    strOrderTotal,
                    strComment,
                    OneSignal.getPermissionSubscriptionState().subscriptionStatus.userId,
                    Constant.BASE_URL
                )
                val requestQueue = Volley.newRequestQueue(requireContext())
                requestQueue.add(request)
            }
            builder.setNegativeButton(resources.getString(R.string.dialog_option_no), null)
            builder.setCancelable(false)
            builder.show()
        }
    }

    override fun showSnackBar(msg: Int) {
        view?.let { Snackbar.make(it, msg, Snackbar.LENGTH_SHORT).show() }
    }

    private fun getTextCheckedRadioDtn(){
        binding?.radioGroup?.setOnCheckedChangeListener { group, _ ->
            val checkedRb = group.checkedRadioButtonId.let {
                activity?.findViewById<RadioButton>(it) }
            binding?.txtRb?.text =checkedRb?.text?.toString().orEmpty()
        }
    }

    override fun hideDialog() {
        dialog?.dismiss()
    }

    override fun showError(str: String) {
        Toast.makeText(requireContext(), str, Toast.LENGTH_LONG)
            .show()
    }


    override fun dialogSuccess() {
        dialogSuccessOrder()
    }

    private fun dialogSuccessOrder() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(R.string.checkout_success_title)
        builder.setMessage(R.string.checkout_success_msg)
        builder.setCancelable(false)
        builder.setPositiveButton(
            R.string.checkout_option_ok
        ) { _, _ ->
            presenter.addOrdersAndDeleteAllCarts(strOrderList, strOrderTotal)
            view?.findNavController()?.navigate(R.id.mainFragment)
        }
        val alert = builder.create()
        alert.show()
    }

    override fun setSpinnerAdapter(list: MutableList<String>) {
        val myAdapter =
            context?.let { it1 -> ArrayAdapter(it1, R.layout.spinner_item, list) }
        myAdapter?.setDropDownViewResource(R.layout.spinner_dropdown_item)
        binding?.spinner?.adapter = myAdapter

        binding?.spinner?.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    adapterView: AdapterView<*>?, view: View, pos: Int, l: Long
                ) {
                    binding?.edtShipping?.setText(list[pos])
                }

                override fun onNothingSelected(adapterView: AdapterView<*>?) {}
            }
    }

    override fun hideProgress() {
        binding?.progressCheckout?.visibility = View.GONE
    }

    override fun setDataToEditTexts(
       orderList:String,
        orderPriceStr: String,
        currencyCode: String,
        priceTax: String,
        taxStr: String,
        totalPriceStr: String,
        strTax: String
    ) {
        dataOrderList = ""
        dataOrderList += orderList

        if (Constant.ENABLE_DECIMAL_ROUNDING) {
            dataOrderList += "\n" + resources.getString(R.string.txt_order) + " " + orderPriceStr + " " + currencyCode + "\n" + resources.getString(
                R.string.txt_tax
            ) + " " + priceTax + " % : " + taxStr + " " + currencyCode + "\n" + resources.getString(
                R.string.txt_total
            ) + " " + totalPriceStr + " " + currencyCode

            binding?.edtOrderTotal?.setText("$totalPriceStr $currencyCode")
        } else {

            dataOrderList += "\n" + resources.getString(R.string.txt_order) + " " + orderPriceStr + " " + currencyCode + "\n" + resources.getString(
                R.string.txt_tax
            ) + " " + strTax + " % : " + taxStr + " " + currencyCode + "\n" + resources.getString(
                R.string.txt_total
            ) + " " + totalPriceStr + " " + currencyCode

            binding?.edtOrderTotal?.setText("$totalPriceStr $currencyCode")
        }

        binding?.edtOrderList?.setText(dataOrderList)
    }

    override fun setOrderList(orderList: String) {
        dataOrderList += if (orderList.equals("", ignoreCase = true)) {
            getString(R.string.no_order)
        }else{
            orderList
        }

    }

    override fun showProgress() {
        binding?.progressCheckout?.visibility = View.VISIBLE
    }

    override fun showUserInfo(name: String, email: String, phone: String, address: String) {
        binding?.edtName?.setText(name)
        binding?.edtEmail?.setText(email)
        binding?.edtPhone?.setText(phone)
        binding?.edtAddress?.setText(address)
    }


}
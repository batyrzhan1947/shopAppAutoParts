package com.batyrzhan.autoparts.utils

import android.app.AlertDialog
import androidx.fragment.app.Fragment
import com.batyrzhan.autoparts.R

class LoadingDialog(fragment: Fragment) {
    private var fragment: Fragment? = fragment
    private var dialog: AlertDialog? = null

    fun startDialog(title:String, msg:String) {
        val builder = AlertDialog.Builder(fragment?.requireContext())
        val inflater = fragment?.layoutInflater
        builder.setView(inflater?.inflate(R.layout.progress, null))
        builder.setCancelable(false)
        builder.setTitle(title)
        builder.setMessage(msg)
        dialog = builder.create()
        dialog?.show()
    }

    fun dismiss() {
        dialog?.dismiss()
    }
}

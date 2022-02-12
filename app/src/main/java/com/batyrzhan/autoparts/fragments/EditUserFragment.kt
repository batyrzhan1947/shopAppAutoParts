package com.batyrzhan.autoparts.fragments

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.batyrzhan.autoparts.R
import com.batyrzhan.autoparts.api.Executors
import com.batyrzhan.autoparts.database.AppDataBase
import com.batyrzhan.autoparts.databinding.EditUserFragmentBinding
import com.batyrzhan.autoparts.database.model.User
import com.batyrzhan.autoparts.MyApplication
import com.batyrzhan.autoparts.contracts.EditUserContract
import com.batyrzhan.autoparts.presenters.EditUserPresenter
import com.batyrzhan.autoparts.utils.Utils
import com.batyrzhan.common.base.BaseFragment
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel

class EditUserFragment : BaseFragment<EditUserContract.View, EditUserContract.Presenter>(),
EditUserContract.View{
    private var _binding: EditUserFragmentBinding? = null
    private val binding get() = _binding
    override val presenter : EditUserPresenter by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = EditUserFragmentBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.shutdownExecutors()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
       presenter.getUserIfExist()
        setUser()
    }

    private fun setUser() {
        binding?.saveUserBtn?.setOnClickListener {
            val name = binding?.edtName?.text.toString()
            val email = binding?.edtEmail?.text.toString()
            val phone = binding?.edtPhone?.text.toString()
            val address = binding?.edtAddress?.text.toString()
            presenter.setUser(name, email, phone, address)
        }
    }

    override fun transitionToFragment() {
        view?.findNavController()
            ?.navigate(R.id.action_editUserFragment_to_profileFragment)
    }

    override fun setSnackBar(msg: String) {
        view?.let { it1 ->
            Snackbar.make(it1, msg, 1500).show()
        }
    }

    override fun showUserInfo(name: Editable, email: Editable, phone: Editable, address: Editable) {
        binding?.edtName?.text = name
        binding?.edtEmail?.text = email
        binding?.edtPhone?.text = phone
        binding?.edtAddress?.text = address
    }
}
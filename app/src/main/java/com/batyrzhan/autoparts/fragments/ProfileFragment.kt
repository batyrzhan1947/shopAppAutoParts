package com.batyrzhan.autoparts.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.batyrzhan.autoparts.R
import com.batyrzhan.autoparts.api.Executors
import com.batyrzhan.autoparts.database.AppDataBase
import com.batyrzhan.autoparts.databinding.ProfileFragmentBinding
import com.batyrzhan.autoparts.MyApplication
import com.batyrzhan.autoparts.contracts.ProfileContract
import com.batyrzhan.autoparts.presenters.ProfilePresenter
import com.batyrzhan.common.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel


class ProfileFragment : BaseFragment<ProfileContract.View,ProfileContract.Presenter>(),
ProfileContract.View{
    private var _binding: ProfileFragmentBinding? = null
    private val binding get() = _binding
    override val presenter:ProfilePresenter by viewModel()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ProfileFragmentBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.shutdownExecutors()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.setUserIfExist()
        setButtonsAction()
    }

    private fun setButtonsAction() {
        binding?.btnEditUser?.setOnClickListener {
            view?.findNavController()
                ?.navigate(R.id.action_profileFragment_to_editUserFragment)
        }

        binding?.btnOrderHistory?.setOnClickListener {
            view?.findNavController()
                ?.navigate(R.id.action_profileFragment_to_historyOrdersFragment)
        }

        binding?.btnShare?.setOnClickListener {
            // val share_text =  Html.fromHtml(resources.getString(R.string.share_app)).toString()
            val shareText = HtmlCompat.fromHtml(
                resources.getString(R.string.share_app),
                HtmlCompat.FROM_HTML_MODE_LEGACY
            ).toString()
            val intent = Intent()
            intent.action = Intent.ACTION_SEND
            intent.putExtra(
                Intent.EXTRA_TEXT, """ $shareText
     
     https://play.google.com/store/apps/details?id=${activity?.packageName}
     """.trimIndent()
            )
            intent.type = "text/plain"
            startActivity(intent)
        }

        binding?.btnPrivacy?.setOnClickListener {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(getString(R.string.privacy_policy_url))
                )
            )
        }
    }

    override fun showUserInfo(name: String, email: String, phone: String, address: String) {
        binding?.txtUserName?.text = name
        binding?.txtUserEmail?.text = email
        binding?.txtUserPhone?.text = phone
        binding?.txtUserAddress?.text = address
    }


}
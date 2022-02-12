package com.batyrzhan.autoparts.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.batyrzhan.autoparts.MainActivity
import com.batyrzhan.autoparts.R
import com.batyrzhan.autoparts.adapters.CartAdapter
import com.batyrzhan.autoparts.api.Executors
import com.batyrzhan.autoparts.database.AppDataBase
import com.batyrzhan.autoparts.database.model.Cart
import com.batyrzhan.autoparts.databinding.CartFragmentBinding
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers


class CartFragment : Fragment() {
    private var _binding: CartFragmentBinding? = null
    private val binding get() = _binding
    private var db: AppDataBase? = null
    private var executors: Executors? = null
    private var compositeDisposable: CompositeDisposable? = null
    private var cartLayoutManager: LinearLayoutManager? = null
    private var cartAdapter: CartAdapter? = null
    private var cartList = mutableListOf<Cart>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = CartFragmentBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.let {
            db = context?.let { AppDataBase.getInstance(it) }
            executors = Executors()
            compositeDisposable = CompositeDisposable()
            isCheckCart()
            setAdapter()
            setAllTotalPrice()

            binding?.continueBtn?.setOnClickListener {
                view.findNavController().navigate(R.id.action_cartFragment_to_checkOutFragment)
            }
            binding?.clearCartTxtView?.setOnClickListener {
             onClearCart()
            }
        }
    }

    private fun onClearCart() {
        executors?.diskIO()?.execute {
            db?.cartDao()?.deleteAllCarts()
        }
        isCheckCart()
        setAllTotalPrice()
    }

    private fun setAllTotalPrice() {

        db?.cartDao()?.getAllTotalPrice()
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe({ total ->
                if (total != null) {
                    if (total > 0)
                        binding?.allPriceCart?.text = "${total.toInt()} KZT"
                }
            }, {
                Log.e("App", "setAllTotalPrice: ", it)
            }).also { compositeDisposable }

    }


    private fun isCheckCart() {

        (activity as MainActivity).setCountCart()
        db?.cartDao()?.getCountCarts()
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe({ countCarts ->
                if (countCarts != null) {
                    if (countCarts > 0) {
                        binding?.layoutRecCart?.visibility = View.VISIBLE
                        binding?.layoutEmptyHistory?.root?.visibility = View.GONE
                    } else {
                        binding?.layoutEmptyHistory?.root?.visibility = View.VISIBLE
                        //   binding?.layoutRecCart?.visibility = View.GONE
                        binding?.layoutEmptyHistory?.btnContinue?.setOnClickListener {
                            it.findNavController()
                                .navigate(R.id.action_cartFragment_to_catalogFragment)
                        }
                    }
                }
            }, {
                Log.e("App", "isCheckCart: ",it )
            }).also { compositeDisposable }


    }

    private fun setAdapter() {
        cartAdapter = CartAdapter(requireContext(),
            clickListener = { cart, _->
                removeItem(cart)
                isCheckCart()
                setAllTotalPrice()
            })
        cartLayoutManager = LinearLayoutManager(
            context, LinearLayoutManager.VERTICAL,
            false
        )
        binding?.cartRecView?.apply {
            layoutManager = cartLayoutManager
            adapter = cartAdapter
            setHasFixedSize(true)
            itemAnimator = DefaultItemAnimator()

        }
        getData()
    }

    private fun removeItem(cart: Cart) {

        executors?.diskIO()?.execute {
            db?.cartDao()?.deleteCart(cart)
        }

        val disposable = db?.cartDao()?.getAllCarts()
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe(
                {
                    binding?.cartRecView?.apply {
                        layoutManager = cartLayoutManager
                        adapter = cartAdapter
                        setHasFixedSize(true)
                        itemAnimator = DefaultItemAnimator()

                    }

                    cartAdapter?.setList(it)

                }, { th -> Log.e("App", "removeItem: ", th) })
        disposable?.let { compositeDisposable?.add(it) }

    }

    private fun getData() {
        db?.cartDao()?.getAllCarts()
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe(
                {
                    cartList.clear()
                    cartList.addAll(it)
                    cartAdapter?.setList(cartList)
                }, {
                    Log.e("App", "onViewCreated: ", it)
                }
            ).also { compositeDisposable }
    }

    override fun onStop() {
        super.onStop()
        compositeDisposable?.dispose()
        executors?.shutdown()
    }
}
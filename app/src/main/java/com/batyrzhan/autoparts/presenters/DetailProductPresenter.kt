package com.batyrzhan.autoparts.presenters

import android.util.Log
import com.batyrzhan.autoparts.R
import com.batyrzhan.autoparts.api.AdminPanelApiService
import com.batyrzhan.autoparts.api.Executors
import com.batyrzhan.autoparts.contracts.DetailProductContract
import com.batyrzhan.autoparts.database.CartDAO
import com.batyrzhan.autoparts.database.model.Cart
import com.batyrzhan.common.base.BasePresenter
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class DetailProductPresenter(
    private val apiService: AdminPanelApiService,
    private val cartDao: CartDAO
) : BasePresenter<DetailProductContract.View>(),
    DetailProductContract.Presenter {
    private var compositeDisposable: CompositeDisposable? = null

    override fun clearDispose() {
        compositeDisposable?.disposeOnCleared()
    }

    override fun getDetailProduct(productID: Int) {
        if (productID > 0) {
            apiService.getProduct(productID).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { item ->
                        view?.showText(item)
                    }, { th ->
                        Log.e("App", "onViewCreated: ", th)
                        view?.hideProgress()
                    }
                ).disposeOnCleared()
            apiService.getProductMarks(productID).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { item ->
                        view?.showMarksAuto(item)
                        view?.hideProgress()
                    }, { th ->
                        Log.e("App", "onViewCreated: ", th)
                        view?.hideProgress()
                    }
                ).disposeOnCleared()
        }
    }

    init {
        compositeDisposable = CompositeDisposable()
    }

    override fun inputData(
        countProduct: String, productQuantity: Int, productID: Int, productPrice: Double,
        productNameDetail: String, imgProduct: String, currencyCode: String
    ) {
        val quantity: Int
        if (!countProduct.equals("", ignoreCase = true)) {
            quantity = countProduct.toInt()

            productQuantity.let { prodCount ->
                when {
                    quantity <= 0 -> {
                        view?.showSnackBar(R.string.msg_stock_below_0)
                    }
                    quantity > prodCount -> {
                        view?.showSnackBar(R.string.msg_stock_not_enough)
                    }
                    else -> {

                        Completable.fromAction {
                            if (cartDao.ifExistsById(productID) >= 1) {
                                cartDao.updateData(
                                    productID, quantity,
                                    productPrice * quantity
                                )
                            } else {
                                val cart = Cart(
                                    productID, productNameDetail,
                                    quantity, productPrice * quantity,
                                    imgProduct, currencyCode
                                )
                                cartDao.insertProductToCart(cart)
                            }
                        }.subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                {
                                  //  getCountCarts()

                                    view?.showSnackBar(R.string.msg_success_add_cart)
                                }, {
                                    Log.e("App", "inputData: ",it )
                                }
                            ).also { compositeDisposable }
                    }
                }
            }

        } else {
            view?.showSnackBar(R.string.countNotSendTxt)
        }
    }
}
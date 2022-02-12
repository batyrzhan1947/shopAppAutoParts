package com.batyrzhan.autoparts.presenters

import android.util.Log
import com.batyrzhan.autoparts.api.AdminPanelApiService
import com.batyrzhan.autoparts.api.Executors
import com.batyrzhan.autoparts.contracts.ProductsContract
import com.batyrzhan.autoparts.model.Product
import com.batyrzhan.common.base.BasePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*

class ProductsPresenter(
    private val apiService: AdminPanelApiService
) : BasePresenter<ProductsContract.View>(),
    ProductsContract.Presenter {
    private var productList = mutableListOf<Product>()

    override fun getProducts(categoryId: String) {

        if (categoryId.isNotEmpty()) {
            apiService.getProducts(categoryId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { list ->
                        if (list.isNotEmpty()) {
                            view?.goneEmptyLayout()
                            productList.clear()
                            productList.addAll(list)
                            view?.setProductsToAdapter(productList)

                            view?.hideProgress()
                        } else {
                            view?.visibleEmptyLayout()
                            view?.hideProgress()
                        }
                    }, { th ->
                        Log.e("App", "onViewCreated: ", th)
                        view?.hideProgress()
                    }
                ).disposeOnCleared()

        }
    }

    override fun checkComparatorSort(c: Comparator<Product>) {
        val executors = Executors()
        executors.diskIO().execute {
            Collections.sort(this.productList, c)
            executors.mainThread().execute {
                view?.setProductsToAdapter(productList)
            }
        }
        executors.shutdown()
    }
}


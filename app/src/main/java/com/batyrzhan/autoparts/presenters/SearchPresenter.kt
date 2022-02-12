package com.batyrzhan.autoparts.presenters

import android.util.Log
import com.batyrzhan.autoparts.api.AdminPanelApiService
import com.batyrzhan.autoparts.contracts.MainPageContract
import com.batyrzhan.autoparts.contracts.SearchContract
import com.batyrzhan.autoparts.database.CartDAO
import com.batyrzhan.autoparts.model.Product
import com.batyrzhan.autoparts.model.ShopNotification
import com.batyrzhan.common.base.BasePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.*

class SearchPresenter(
    private val apiService: AdminPanelApiService
) : BasePresenter<SearchContract.View>(),
    SearchContract.Presenter {

    private var productList = mutableListOf<Product>()

    override fun filter(text: String) {
        val filterList = mutableListOf<Product>()
        for(item in productList){
            if (item.p_name.toLowerCase(Locale.getDefault())
                    .contains(text.toLowerCase(Locale.getDefault())))
                filterList.add(item)
        }
        view?.setFilteredToAdapter(filterList)
    }

    override fun getProductsRecently() {

            apiService.getRecently()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { list ->
                        if (list.isNotEmpty()) {
                            productList.clear()
                            productList.addAll(list)
                            view?.setFilteredToAdapter(productList)
                            view?.hideProgress()
                        } else {
                            view?.hideProgress()
                        }
                    }, { th ->
                        Log.e("App", "onViewCreated: ", th)
                        view?.hideProgress()
                    }
                ).disposeOnCleared()


    }



}

package com.batyrzhan.common.base

interface MvpPresenter<V : MvpView> {

    fun attach(view: V)

    fun detach()
}
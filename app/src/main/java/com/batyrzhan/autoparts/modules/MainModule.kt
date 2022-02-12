package com.batyrzhan.autoparts.modules

import com.batyrzhan.autoparts.api.AdminPanelApiService
import com.batyrzhan.autoparts.database.AppDataBase
import com.batyrzhan.autoparts.database.CartDAO
import com.batyrzhan.autoparts.presenters.*
import com.batyrzhan.common.base.InjectionModule
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

object MainModule : InjectionModule {
    override fun create(): Module = module {
        single { AppDataBase.getInstance(androidContext()) }
        single { AdminPanelApiService.create() }
        fun provideCartDao(db: AppDataBase): CartDAO {
            return db.cartDao()
        }
        single { provideCartDao(get()) }
        viewModel { InfoPresenter(get()) }
        viewModel { MainPagePresenter(get()) }
        viewModel { CatalogPresenter(get()) }
        viewModel { EditUserPresenter(get()) }
        viewModel { ProfilePresenter(get()) }
        viewModel { ProductsPresenter(get()) }
        viewModel { CheckOutPresenter(get(), get()) }
        viewModel { DetailProductPresenter(get(), get()) }
        viewModel { OrderSheetPresenter(get()) }
        viewModel { DetailHelpPresenter(get()) }
        viewModel { HistoryOrdersPresenter(get()) }
        viewModel { SearchPresenter(get()) }
    }
}
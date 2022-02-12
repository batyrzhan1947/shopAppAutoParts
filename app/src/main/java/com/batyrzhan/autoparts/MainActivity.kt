package com.batyrzhan.autoparts

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.*
import com.batyrzhan.autoparts.api.Executors
import com.batyrzhan.autoparts.database.AppDataBase
import com.batyrzhan.autoparts.database.CartDAO
import com.batyrzhan.autoparts.databinding.ActivityMainBinding
import com.batyrzhan.autoparts.utils.FragmentListener
import com.onesignal.OneSignal
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MainActivity : AppCompatActivity(), FragmentListener {
    private lateinit var binding: ActivityMainBinding
  //  private var binding: ActivityMainBinding? = null
    private var appBarConfiguration: AppBarConfiguration? = null
    private var cartDAO: CartDAO? = null
    private var disposable: CompositeDisposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setNavigation()
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE)
        OneSignal.startInit(this)
            .unsubscribeWhenNotificationsAreDisabled(true)
            .init()
    }

    override fun onStart() {
        super.onStart()
        if (intent.hasExtra("item")) {
            when {
                intent.getStringExtra("item").equals("product") -> {
                    if (intent.hasExtra("productBundle")) {
                        findNavController(R.id.nav_host_fragment).navigate(
                            R.id.detailProductFragment,
                            intent.getBundleExtra("productBundle")
                        )
                    }
                }
                intent.getStringExtra("item").equals("history") -> {
                    findNavController(R.id.nav_host_fragment).navigate(R.id.historyOrdersFragment)
                }
            }
        }
    }

    override fun onDestroy() {
        disposable?.dispose()
        disposable?.clear()
        super.onDestroy()
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return appBarConfiguration?.let { navController.navigateUp(it) } == true || super.onSupportNavigateUp()
    }

    private fun setNavigation() {
        setSupportActionBar(binding.toolbarMain)
        val navController = findNavController(R.id.nav_host_fragment)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.mainFragment, R.id.catalogFragment,
                R.id.cartFragment, R.id.profileFragment, R.id.infoFragment
            ), binding.drawerLayout
        )
        appBarConfiguration?.let { setupActionBarWithNavController(navController, it) }
      //  binding.navigationView.setupWithNavController(navController)
        binding.bottomNavigationMain.setupWithNavController(navController)
        binding.imgCart.setOnClickListener {
            this.findNavController(R.id.nav_host_fragment).navigate(R.id.cartFragment) }
        binding.searchImg.setOnClickListener {
            this.findNavController(R.id.nav_host_fragment).navigate(R.id.searchFragment) }
    }

    override fun setCountCart() {
        val executors = Executors()
        executors.diskIO().execute {
            cartDAO = AppDataBase.getInstance(applicationContext).cartDao()
            executors.mainThread().execute {
                cartDAO?.getCountCarts()
                    ?.subscribeOn(Schedulers.computation())
                    ?.observeOn(AndroidSchedulers.mainThread())
                    ?.subscribe({ count ->
                        binding.countProductCartTxtView.text =
                            if (count > 0) count.toString() else ""
                    }, {
                        Log.e("App", "setCountCart: ", it)
                    }).also { disposable }
            }
        }
        executors.shutdown()
    }
}

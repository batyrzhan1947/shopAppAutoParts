package com.batyrzhan.autoparts.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.batyrzhan.autoparts.MainActivity
import com.batyrzhan.autoparts.R
import com.batyrzhan.autoparts.database.AppDataBase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SplashActivity : AppCompatActivity() {
    private var isCancelled: Boolean? = null
    private var progressBar: ProgressBar? = null
    private var itemId: Long? = null
    private var url: String? = null
    private var buyerCode: String? = null
    private var statusOrder: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        progressBar = findViewById(R.id.progressBar)
        isCancelled = false

        if (intent.hasExtra("nid")) {
            itemId = intent.getLongExtra("nid", 0)
            url = intent?.getStringExtra("external_link").toString()
            buyerCode = intent?.getStringExtra("buyerCode").toString()
            statusOrder = intent?.getStringExtra("status").toString()
        } else {
            itemId = 0
            url = ""
        }
        Looper.myLooper()?.let {
            Handler(it).postDelayed({
                progressBar?.visibility = View.GONE
                isCancelled?.let { isCanceled ->
                    if (!isCanceled) {
                        if (itemId == 0L) {
                            if (url == "" || url == "no_url") {
                                val intent =
                                    Intent(applicationContext, MainActivity::class.java)
                                startActivity(intent)
                                finish()
                            } else {
                                val a = Intent(applicationContext, MainActivity::class.java)
                                startActivity(a)
                                val b = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                                startActivity(b)
                                finish()
                            }
                        } else if (itemId == 1010101010L) {
                            if (buyerCode !=null && statusOrder != null) {
                                CoroutineScope(Dispatchers.IO).launch {
                                    val orderDao =
                                        AppDataBase.getInstance(applicationContext).orderDao()
                                    buyerCode?.let { code ->
                                        statusOrder?.let { status ->
                                            orderDao.updateStatusOrder(code, status)
                                        }
                                    }
                                    withContext(Dispatchers.Main) {
                                        val a = Intent(applicationContext, MainActivity::class.java)
                                        a.putExtra("item", "history")
                                        startActivity(a)
                                        finish()
                                    }
                                }
                            }
                        } else {
                            val bundle = Bundle()
                            itemId?.let { it1 -> bundle.putString("productId", it1.toString()) }
                            val a = Intent(applicationContext, MainActivity::class.java)
                            a.putExtra("productBundle", bundle)
                            a.putExtra("item", "product")
                            startActivity(a)
                            finish()
                        }
                    }
                }
            }, 1500)
        }
    }
}
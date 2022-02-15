package com.batyrzhan.autoparts.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.text.HtmlCompat
import com.batyrzhan.autoparts.R
import com.batyrzhan.autoparts.activity.SplashActivity
import com.onesignal.NotificationExtenderService
import com.onesignal.OSNotificationReceivedResult
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

class MyNotificationExtenderExample : NotificationExtenderService() {

    private val notificationId = 1
    private var mNotificationManager: NotificationManager? = null
    private var message: String? = null
    private var bigPicture: String? = null
    var title: String? = null
    private var cname: String? = null
    private var url: String? = null
    private var nid: Long? = null
    private var buyerCode: String? = null
    private var statusOrder: String? = null

    private val notificationChannelId = "ecommerce_channel_01"

    override fun onNotificationProcessing(receivedResult: OSNotificationReceivedResult?): Boolean {
        title = receivedResult?.payload?.title
        message = receivedResult?.payload?.body
        bigPicture = receivedResult?.payload?.bigPicture

        try {
            nid = receivedResult?.payload?.additionalData?.getLong("cat_id")
            cname = receivedResult?.payload?.additionalData?.getString("cat_name")
            buyerCode = receivedResult?.payload?.additionalData?.getString("buyerCode").orEmpty()
            statusOrder = receivedResult?.payload?.additionalData?.getString("status").orEmpty()
            url = receivedResult?.payload?.additionalData?.getString("external_link")
        } catch (e: Exception) {
            e.printStackTrace()
        }
        sendNotification()
        return true
    }

    private fun sendNotification() {
        mNotificationManager = this.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        val intent: Intent
        if (nid == 0L && url != "false" && !url?.trim { it <= ' ' }?.isEmpty()!!) {
            intent = Intent(this, SplashActivity::class.java)
            intent.putExtra("nid", nid)
            intent.putExtra("external_link", url)
            intent.putExtra("cname", cname)
        } else {
            intent = Intent(this, SplashActivity::class.java)
            intent.putExtra("nid", nid)
            intent.putExtra("external_link", url)
            intent.putExtra("cname", cname)
            intent.putExtra("buyerCode", buyerCode)
            intent.putExtra("status", statusOrder)
        }
        val mChannel: NotificationChannel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence = "Ecommerce Android App Channel"
            val importance = NotificationManager.IMPORTANCE_HIGH
            mChannel = NotificationChannel(notificationChannelId, name, importance)
            mNotificationManager?.createNotificationChannel(mChannel)
        }

        val contentIntent =
            PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)



        val uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val mBuilder = NotificationCompat.Builder(this, notificationChannelId)
            .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.ic_app_logo))
            .setAutoCancel(true)
            .setSound(uri)
            .setAutoCancel(true)
            .setChannelId(notificationChannelId)
            .setLights(Color.RED, 800, 800)

        mBuilder.setSmallIcon(getNotificationIcon(mBuilder))

        mBuilder.setContentTitle(title)
        mBuilder.setTicker(message)

        if (bigPicture != null) {
            mBuilder.setStyle(
                NotificationCompat.BigPictureStyle()
                    .bigPicture(getBitmapFromURL(bigPicture))
                    .setSummaryText(message?.let {
                        HtmlCompat.fromHtml(
                            it,
                            HtmlCompat.FROM_HTML_MODE_LEGACY
                        )
                    })
            )
            mBuilder.setContentText(message?.let {
                HtmlCompat.fromHtml(
                    it,
                    HtmlCompat.FROM_HTML_MODE_LEGACY
                )
            })
        } else {
            mBuilder.setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(message?.let {
                        HtmlCompat.fromHtml(
                            it,
                            HtmlCompat.FROM_HTML_MODE_LEGACY
                        )
                    })
            )
            mBuilder.setContentText(message?.let {
                HtmlCompat.fromHtml(
                    it,
                    HtmlCompat.FROM_HTML_MODE_LEGACY
                )
            })
        }

        mBuilder.setContentIntent(contentIntent)
        mNotificationManager?.notify(notificationId, mBuilder.build())

    }

    private fun getNotificationIcon(notificationBuilder: NotificationCompat.Builder): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notificationBuilder.color = getColour()
            R.drawable.ic_shopping_cart_primary
        } else {
            R.drawable.ic_shopping_cart_primary
        }
    }

    private fun getColour(): Int {
        return R.color.colorPrimary
    }

    private fun getBitmapFromURL(src: String?): Bitmap? {
        return try {
            val url = URL(src)
            val connection = url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val input = connection.inputStream
            BitmapFactory.decodeStream(input)
        } catch (e: IOException) {
            Log.e("App", "getBitmapFromURL: ",e )
            null
        }
    }

}
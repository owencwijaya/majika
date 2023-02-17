package com.example.majika.ui.payment

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.lifecycleScope
import com.example.majika.MainActivity
import com.example.majika.MajikaApplication
import com.example.majika.R
import com.example.majika.databinding.ActivityPaymentBinding
import com.example.majika.ui.dialog.SuccessDialogFragment
import com.example.majika.ui.menu.MenuFragment
import com.example.majika.utils.observeOnce
import com.google.zxing.Result
import kotlinx.coroutines.android.HandlerDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.dm7.barcodescanner.zxing.ZXingScannerView
import java.util.Timer

class PaymentActivity : AppCompatActivity(), ZXingScannerView.ResultHandler {
    private lateinit var binding: ActivityPaymentBinding
    private lateinit var qrScanner: ZXingScannerView
    private var totalBayar: Int = 0
    private val CHANNEL_ID = "Majika Pembayaran"

    private val paymentViewModel: PaymentViewModel by viewModels {
        PaymentViewModelFactory((application as MajikaApplication).repository)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionBar = supportActionBar
        actionBar?.apply {
            setDisplayShowHomeEnabled(true)
            setDisplayHomeAsUpEnabled(true)
        }

        paymentViewModel.totalPrice.observe(this) {total ->
            binding.totalPembayaran.text = getString(R.string.total_pembayaran, total)
            totalBayar = total
        }

        qrScanner = ZXingScannerView(this)
        qrScanner.setAutoFocus(true)
        qrScanner.setResultHandler(this)
        binding.scannerLayout.addView(qrScanner)
    }

    override fun onStart(){
        super.onStart()
        qrScanner.startCamera()
    }

    override fun onResume(){
        super.onResume()
        qrScanner.resumeCameraPreview(this)
    }

    override fun onPause(){
        super.onPause()
        qrScanner.stopCamera()
    }

    override fun handleResult(res: Result?) {
        paymentViewModel.getPaymentStatus(res?.text.toString())
        paymentViewModel.paymentStatus.observe(this) {it ->
            // lanjutin di sini mas adit
            if (it.status.equals(FAILED_PAYMENT)) {
                Toast.makeText(this, FAILED_MSG, Toast.LENGTH_SHORT).show()
                qrScanner.resumeCameraPreview(this)
            } else if (it.status.equals(SUCCESSFUL_PAYMENT)) {
                handleSuccessfulPayment()
            } else {
                Toast.makeText(this, OTHER_FAILED_MSG, Toast.LENGTH_SHORT).show()
                qrScanner.resumeCameraPreview(this)
            }
        }
    }

    fun handleSuccessfulPayment() {
        paymentViewModel.deleteCart()
        createNotificationChannel()
        createNotification(totalBayar)
        this.lifecycleScope.launch {
            val newDialog = SuccessDialogFragment()
            newDialog.show(supportFragmentManager, CHANNEL_ID)
            delay(5000)
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
            this@PaymentActivity.finish()
        }
    }

    private fun createNotification(totalBayar: Int) {
        var builder = NotificationCompat.Builder(this, "MAJIKA")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle("Pembayaran Berhasil")
            .setContentText("Berhasil melakukan pembayaran sebesar ${totalBayar}")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        with(NotificationManagerCompat.from(applicationContext)) {
            if (ActivityCompat.checkSelfPermission(
                    applicationContext,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            notify(1, builder.build())
        }
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    companion object {
        const val TOTAL = "total"
        const val FAILED_PAYMENT = "FAILED"
        const val SUCCESSFUL_PAYMENT = "SUCCESS"
        const val FAILED_MSG = "Pembayaran gagal, harap coba lagi"
        const val OTHER_FAILED_MSG = "QR Code tidak dikenali"
    }
}
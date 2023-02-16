package com.example.majika.ui.payment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.example.majika.MajikaApplication
import com.example.majika.R
import com.example.majika.databinding.ActivityPaymentBinding
import com.google.zxing.Result
import me.dm7.barcodescanner.zxing.ZXingScannerView

class PaymentActivity : AppCompatActivity(), ZXingScannerView.ResultHandler {
    private lateinit var binding: ActivityPaymentBinding
    private lateinit var qrScanner: ZXingScannerView

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
        qrScanner.setResultHandler(this)
        qrScanner.startCamera()
    }

    override fun onPause(){
        super.onPause()
        qrScanner.stopCamera()
    }

    override fun handleResult(res: Result?) {
        paymentViewModel.getPaymentStatus(res?.text.toString())
        paymentViewModel.paymentStatus.observe(this) {it ->
            Toast.makeText(this, it.status, Toast.LENGTH_SHORT).show()
            // lanjutin di sini mas adit
        }
    }

    companion object {
        const val TOTAL = "total"
    }
}
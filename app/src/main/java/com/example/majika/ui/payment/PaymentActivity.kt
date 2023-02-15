package com.example.majika.ui.payment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.majika.MajikaApplication
import com.example.majika.R
import com.example.majika.databinding.ActivityPaymentBinding

class PaymentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPaymentBinding
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

    }

    companion object {
        const val TOTAL = "total"
    }
}
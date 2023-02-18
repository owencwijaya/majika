package com.example.majika.ui.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.majika.MainActivity
import com.example.majika.R
class SuccessDialogFragment : DialogFragment() {
    private var seconds: Int = 5;
    private var eta: Long = 5000;
    private var timer: CountDownTimer? = null
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setMessage(getString(R.string.success_dialog_msg, seconds))
                .setTitle("Pembayaran Berhasil")
                .setNeutralButton(R.string.timer) { _, _ ->
                    val intent = Intent(context, MainActivity::class.java)
                    startActivity(intent)
                    it.finish()
                }
            builder.create()

        } ?: throw IllegalStateException("Activity can't be null")
    }

}
package com.example.majika.ui.twibbon

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.majika.databinding.FragmentTwibbonBinding

class TwibbonFragment : Fragment() {

    private var _binding: FragmentTwibbonBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


//        val twibbonViewModel =
//            ViewModelProvider(this).get(TwibbonViewModel::class.java)

//        setup view
        _binding = FragmentTwibbonBinding.inflate(inflater, container, false)
        val root: View = binding.root

//        setup text
        val textView: TextView = binding.textTwibbon

        textView.text = "testing"
//        setup button
        val captureButton: Button = binding.captureButton
        captureButton.setOnClickListener {
            takePhoto()
        }






        return root
    }

//    override fun onDestroyView(textView: TextView) {
//        super.onDestroyView()
//        _binding = null
//    }

    private val REQUEST_IMAGE_CAPTURE = 1
override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {




    if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK){
        val takenImage = data?.extras?.get("data") as Bitmap
        binding.cameraPreviewView.setImageBitmap(takenImage)

    } else {
        super.onActivityResult(requestCode, resultCode, data)
    }
}

    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        } catch (e: ActivityNotFoundException) {
            // display error state to the user
        }
    }


    @Override
    private fun takePhoto(){
        dispatchTakePictureIntent()
    }

}
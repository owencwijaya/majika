package com.example.majika.ui.twibbon

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.majika.MainActivity
import com.example.majika.R

import com.example.majika.databinding.FragmentTwibbonBinding
import com.example.majika.utils.ManagePermission
import com.google.common.util.concurrent.ListenableFuture

class TwibbonFragment : Fragment() {
    private var _binding: FragmentTwibbonBinding? = null
    private val binding get() = _binding!!

    private lateinit var managePermission: ManagePermission
    private lateinit var cameraProviderFuture : ListenableFuture<ProcessCameraProvider>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) : View? {
        // Inflate the layout for this fragment
        _binding = FragmentTwibbonBinding.inflate(inflater, container, false)
        managePermission = ManagePermission(activity as MainActivity, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startCamera()

        // Setup the listener for take photo button
        binding.captureButton.setOnClickListener { takePhoto() }
    }

    override fun onStart(){
        super.onStart()
        (activity as MainActivity).setTitle(getString(R.string.title_twibbon))
    }

    private fun startCamera() {
//        check permission
        if (managePermission.checkPermissions()) {
            binding.captureButton.text = "Take Photo"
            startCameraProvider()

        }
    }

    private fun startCameraProvider() {
//        setup needed variable
        cameraProviderFuture = ProcessCameraProvider.getInstance(this.requireContext())
        val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
        val preview : Preview = Preview.Builder().build()
        val imageCapture : ImageCapture = ImageCapture.Builder().build()
        val cameraSelector = CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_FRONT).build()

//        binding camera feed to PreviewView
        try {
            // Unbind use cases before rebinding
            cameraProvider.unbindAll()
            // Bind use cases to camera
            cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)
            preview?.setSurfaceProvider(binding.viewFinder.surfaceProvider)

        } catch (exc: Exception) {
            Log.e("CameraXApp", "Use case binding failed", exc)
        }
    }

    private fun takePhoto() {
//        unbind will stop the camera preview and freezing it
        cameraProviderFuture = ProcessCameraProvider.getInstance(this.requireContext())
        val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
        cameraProvider.unbindAll()

//        change the button text
        binding.captureButton.text = "Take Photo Again"
        binding.captureButton.setOnClickListener { takePhotoAgain() }
    }

    private fun takePhotoAgain() {
        startCamera()
        binding.captureButton.text = "Take Photo"
        binding.captureButton.setOnClickListener { takePhoto() }
    }



//    It's primarily used to define class level variables and methods called static variables. like const
    companion object {
        internal const val REQUEST_CODE_PERMISSIONS = 10
        private  val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }
}
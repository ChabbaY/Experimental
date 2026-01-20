package cloud.englert.experimental.ui.textrecognition

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.fragment.app.Fragment

import com.yalantis.ucrop.UCrop

import java.io.File

import cloud.englert.experimental.R
import cloud.englert.experimental.databinding.FragmentTextrecognitionBinding
import cloud.englert.experimental.textrecognition.TextRecognitionHelper

class TextRecognitionFragment : Fragment() {
    private var _binding: FragmentTextrecognitionBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    private var imageCapture: ImageCapture? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTextrecognitionBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Request camera permissions
        if (allPermissionsGranted()) {
            startCamera()
        } else {
            requestPermissions()
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.imageCaptureButton.setOnClickListener { _: View ->
            takePhoto()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    val cropImageLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val resultUri = UCrop.getOutput(result.data!!)
            if (resultUri != null) {
                TextRecognitionHelper.recognizeTextFromUri(
                    requireContext(), resultUri
                ) { success, text ->
                    if (success) {
                        Toast.makeText(context, "Recognition result: $text",
                            Toast.LENGTH_SHORT).show()
                        Log.d(TAG, "Recognition result: $text")
                    } else {
                        Toast.makeText(context, "Recognition failed",
                            Toast.LENGTH_SHORT).show()
                    }
                }
            } else if (result.resultCode == UCrop.RESULT_ERROR) {
                val error = UCrop.getError(result.data!!)
                Log.e(TAG, "Crop error", error)
            }
        }
    }

    fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener({
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Preview
            val preview = Preview.Builder().build().apply {
                surfaceProvider = binding.previewView.surfaceProvider
            }

            // Image Capturing
            imageCapture = ImageCapture.Builder().build()

            // Select back camera as a default
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera (some devices may not support binding imageCapture
                // and imageAnalyzer at the same time)
                cameraProvider.bindToLifecycle(this, cameraSelector,
                    preview, imageCapture)
            } catch(exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    fun takePhoto() {
        val outputFile = File(requireContext().cacheDir,
            "captured_image_${System.currentTimeMillis()}.jpg")
        val outputOptions = ImageCapture.OutputFileOptions.Builder(outputFile).build()
        imageCapture?.takePicture(outputOptions,
            ContextCompat.getMainExecutor(requireContext()),
            object: ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    val savedUri: Uri = outputFileResults.savedUri ?: outputFile.toUri()

                    val croppedFile = File(
                        requireContext().cacheDir,
                        "cropped_image_${System.currentTimeMillis()}.jpg"
                    )

                    val cropOptions = UCrop.Options().apply {
                        setFreeStyleCropEnabled(true)
                        setShowCropGrid(false)
                        setToolbarTitle(getString(R.string.crop_image))
                    }

                    val cropIntent = UCrop.of(savedUri, croppedFile.toUri())
                        .withAspectRatio(0f, 0f) // free crop
                        .withMaxResultSize(2000, 2000)
                        .withOptions(cropOptions)
                        .getIntent(requireContext())

                    cropImageLauncher.launch(cropIntent)
                }

                override fun onError(exception: ImageCaptureException) {
                    Log.e(TAG, "Image capture failed", exception)
                }
        })
    }

    private fun requestPermissions() {
        activityResultLauncher.launch(REQUIRED_PERMISSIONS)
    }

    private val activityResultLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions())
        { permissions ->
            // Handle Permission granted/rejected
            var permissionGranted = true
            permissions.entries.forEach {
                if (it.key in REQUIRED_PERMISSIONS && !it.value)
                    permissionGranted = false
            }
            if (!permissionGranted) {
                Toast.makeText(requireContext(),
                    "Permission request denied",
                    Toast.LENGTH_SHORT).show()
            } else {
                startCamera()
            }
        }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            requireContext(), it) == PackageManager.PERMISSION_GRANTED
    }

    companion object {
        private val TAG = TextRecognitionFragment::class.java.simpleName
        private val REQUIRED_PERMISSIONS =
            mutableListOf (
                Manifest.permission.CAMERA
            ).toTypedArray()
    }
}
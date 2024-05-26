package com.project.emotiondetection.facedetection

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.OptIn
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetector
import com.google.mlkit.vision.face.FaceDetectorOptions
import com.project.emotiondetection.CameraXViewModel
import com.project.emotiondetection.R
import com.project.emotiondetection.databinding.ActivityFaceDetectionBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.Executors
import kotlin.coroutines.coroutineContext

class FaceDetectionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFaceDetectionBinding
    private lateinit var processCameraProvider: ProcessCameraProvider
    private lateinit var cameraPreview: Preview
    private lateinit var cameraSelector: CameraSelector
    private lateinit var imageAnalysis: ImageAnalysis
    private var isMusicActivityStarted = false

    private val cameraXViewModel = viewModels<CameraXViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityFaceDetectionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        cameraSelector = CameraSelector
            .Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
            .build()

        cameraXViewModel.value.processCameraProvider.observe(this){
            processCameraProvider = it
            bindCameraPreview()
            bindInputAnalyzer()
        }

    }

    private fun bindCameraPreview() {
        cameraPreview = Preview
            .Builder()
            .setTargetRotation(binding.previewView.display.rotation)
            .build()
        cameraPreview.setSurfaceProvider(binding.previewView.surfaceProvider)
        try {
            processCameraProvider.bindToLifecycle(this, cameraSelector, cameraPreview)
        } catch (e: Exception) {
            Log.e(TAG, e.message ?: e.toString())
        }
    }


    private fun bindInputAnalyzer() {
        val faceDetector = FaceDetection.getClient(
            FaceDetectorOptions.Builder()
                .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
                .setContourMode(FaceDetectorOptions.CONTOUR_MODE_ALL)
                .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
                .build()
        )

        imageAnalysis = ImageAnalysis.Builder()
            .setTargetRotation(binding.previewView.display.rotation)
            .build()

        val cameraExecutors = Executors.newSingleThreadExecutor()

        imageAnalysis.setAnalyzer(cameraExecutors) { imageProxy ->
            processImageProxy(faceDetector, imageProxy)
        }
        try {
            processCameraProvider.bindToLifecycle(this, cameraSelector, imageAnalysis)
        } catch (e: Exception) {
            Log.e(TAG, e.message ?: e.toString())
        }


    }

    @OptIn(ExperimentalGetImage::class)
    private fun processImageProxy(faceDetector:FaceDetector, imageProxy: ImageProxy) {
        val inputImage = imageProxy.image?.let { image ->
            InputImage.fromMediaImage(image, imageProxy.imageInfo.rotationDegrees)
        }
        inputImage?.let {
            faceDetector.process(it)
                .addOnSuccessListener {faces->
                    binding.faceBoxOverlay.clear()
                    faces.forEach {face->
                        val faceBox = FaceBox(binding.faceBoxOverlay,face,imageProxy.cropRect)
                        binding.faceBoxOverlay.add(faceBox)
                        if (face.smilingProbability!=null){
                            val smileProb  = face.smilingProbability
                            smileProb?.let {value->
                                binding.apply {
                                    if (value>=.6f && !isMusicActivityStarted){
                                        facialExpression.text = "Happy "+"\uD83D\uDE0A"
                                        facialExpression.visibility = View.VISIBLE
                                        startMusicActivity()
                                    }
                                    else facialExpression.visibility = View.INVISIBLE
                                }
                            }
                        }
                    }
                }
                .addOnFailureListener { exception ->
                    Log.e(TAG, exception.message ?: exception.toString())
                }
                .addOnCompleteListener {
                    imageProxy.close()
                }
        }

    }
    private fun startMusicActivity() {
        isMusicActivityStarted = true
        MusicActivity.startMusicActivity(this)
        finish() // Optionally, finish this activity to remove it from the back stack
    }

    companion object {
        private val TAG = FaceDetectionActivity::class.simpleName
        fun startFaceDetection(context: Context) {
            Intent(context, FaceDetectionActivity::class.java).also {
                context.startActivity(it)
            }
        }
    }
}
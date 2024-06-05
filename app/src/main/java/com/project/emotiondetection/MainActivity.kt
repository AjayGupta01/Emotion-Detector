package com.project.emotiondetection

import android.Manifest
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.project.emotiondetection.databinding.ActivityMainBinding
import com.project.emotiondetection.facedetection.FaceDetectionActivity
import com.project.learnmlkit.utils.hasPermission
import com.project.learnmlkit.utils.onDeniedCameraPermissionRequest
import com.project.learnmlkit.utils.openAppPermissionSettings

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val permission = Manifest.permission.CAMERA
    private val permissionRequestLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            startFaceDetection()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
//        InsertDatabase.insertMainDatabase(this@MainActivity)
        binding.openFaceDetectionBtn.setOnClickListener {
            requestCameraPermissionAndStartScanner()
        }
    }

    private fun requestCameraPermissionAndStartScanner() {
        if (hasPermission(permission)) {
            startFaceDetection()
        } else {
            requestCameraPermission()
        }
    }


    private fun requestCameraPermission() {
        when {
            shouldShowRequestPermissionRationale(permission) -> {
                onDeniedCameraPermissionRequest {
                    openAppPermissionSettings()
                }
            }

            else -> {
                permissionRequestLauncher.launch(permission)
            }
        }
    }
    private fun startFaceDetection() {
        FaceDetectionActivity.startFaceDetection(this)
    }
}
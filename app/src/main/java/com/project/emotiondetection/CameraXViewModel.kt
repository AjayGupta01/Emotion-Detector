package com.project.emotiondetection

import android.app.Application
import android.util.Log
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class CameraXViewModel(application: Application) : AndroidViewModel(application) {
    private val _processCameraProvider: MutableLiveData<ProcessCameraProvider> = MutableLiveData()
    val processCameraProvider: LiveData<ProcessCameraProvider>
        get() {
            val cameraProviderFuture = ProcessCameraProvider.getInstance(getApplication())   // it is used to get camera requirements ready asynchronously and then provide instance of processCameraProvider to ProcessCameraProvider
            cameraProviderFuture.addListener(
                {
                    try {
                        _processCameraProvider.value = cameraProviderFuture.get()
                    }catch (e:Exception){
                        Log.e("ViewmodelException",e.message?:e.toString())
                    }
                },
                ContextCompat.getMainExecutor(getApplication())
            )
            return _processCameraProvider
        }
}
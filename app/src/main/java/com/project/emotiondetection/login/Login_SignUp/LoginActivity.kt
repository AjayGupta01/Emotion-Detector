package com.project.emotiondetection.login.Login_SignUp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import cn.pedant.SweetAlert.SweetAlertDialog
import com.project.emotiondetection.MainActivity
import com.project.emotiondetection.login.CollectionsOfAlertDialog.AlertDialogCollections
import com.project.emotiondetection.login.Database.InsertDatabase
import com.project.emotiondetection.login.Database.MainDatabase
import com.project.emotiondetection.databinding.ActivityLoginBinding
import com.project.emotiondetection.facedetection.FaceDetectionActivity
import kotlinx.coroutines.CoroutineScope

class LoginActivity : AppCompatActivity() {
    private val binding: ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

       InsertDatabase.insertMainDatabase(this@LoginActivity)

        enrollmentFocusListener()
        passwordFocusListener()

        binding.apply {

//            createOneText.setOnClickListener {
//                val redirect_sign_up_activity =
//                    Intent(this@LoginActivity, SignUpActivity::class.java)
//                startActivity(redirect_sign_up_activity)
//            }

            loginButton.setOnClickListener{
                val mainDb= MainDatabase(this@LoginActivity)
                var enrollment=enrollmentEditText.text.toString()
                var password=passwordEditText.text.toString()

                if(mainDb.isUserExist(enrollment,password)){
//                    AlertDialogCollections.successAlertDialog(this@LoginActivity,enrollment.toString())
//                    enrollmentEditText.text?.clear()
//                    passwordEditText.text?.clear()
                    val redirectmainActivity = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(redirectmainActivity)
                    finish()
                }
                else{
                    AlertDialogCollections.failAlertDialog(this@LoginActivity)
                    enrollmentEditText.text?.clear()
                    passwordEditText.text?.clear()
                }

            }
        }
        onBackPressedDispatcher.addCallback(this@LoginActivity,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    val builder =
                        SweetAlertDialog(this@LoginActivity, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Alert")
                            .setContentText("Are you sure want to exit?")
                            .setConfirmButton("Confirm") {
                                finish()
                            }
                            .setCancelButton("Cancel") {
                                it.dismiss()
                            }
                            .show()
                }

            })

    }

    private fun enrollmentFocusListener() {
        binding.apply {
            enrollmentEditText.setOnFocusChangeListener { _, focus_not_changed ->
                if (!focus_not_changed) {
                    enrollmentNumContainer.helperText = validEnrollment()
                }
            }
        }
    }

    private fun validEnrollment(): String? {
        binding.apply {
            val received_enroll = enrollmentEditText.text.toString()

            if (received_enroll == "") {
                return "Required*"
            }
            return null
        }
    }

    private fun passwordFocusListener(){
        binding.apply {
            passwordEditText.setOnFocusChangeListener{_,focused->
                if(!focused){
                    passwordContainer.helperText=validPassword()
                }
            }
        }

    }
    private fun validPassword():String?{
        binding.apply {
            var received_password=passwordEditText.text.toString()

            if(received_password==""){
                return "Required*"
            }
            return null
        }

    }

}
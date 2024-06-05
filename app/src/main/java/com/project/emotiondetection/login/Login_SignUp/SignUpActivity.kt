package com.project.emotiondetection.login.Login_SignUp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import cn.pedant.SweetAlert.SweetAlertDialog
import com.project.emotiondetection.databinding.ActivitySignUpBinding
import com.project.emotiondetection.login.Database.InsertDataPref
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class SignUpActivity : AppCompatActivity() {
    private val binding: ActivitySignUpBinding by lazy {
        ActivitySignUpBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        enrollmentFocusListener()
        passwordFocusListener()


        binding.apply {
            SignUpSubmitButton.setOnClickListener {

                val enrolment = SignUpEnrollmentEdt.text.toString()
                val password = SignUpPasswordEdt.text.toString()
                val confirm_password = SignUpConfirmPasswordEdt.text.toString()

                CoroutineScope(Dispatchers.IO).launch {
                    val wasAcknowledge = InsertDataPref.insertData(this@SignUpActivity,enrolment,password,confirm_password)
                    withContext(Dispatchers.Main) {
                        if (wasAcknowledge) {
                            val redirect_sign_up_login = Intent(this@SignUpActivity, LoginActivity::class.java)
                            startActivity(redirect_sign_up_login)
                            finishAffinity()
                        } else {
                            Toast.makeText(this@SignUpActivity, "Unable to create account", Toast.LENGTH_LONG).show()
                        }
                    }
                }


            }

           onBackPressedDispatcher.addCallback(this@SignUpActivity,object:OnBackPressedCallback(true){
               override fun handleOnBackPressed() {
                   val builder=SweetAlertDialog(this@SignUpActivity,SweetAlertDialog.WARNING_TYPE)
                       .setTitleText("Alert")
                       .setContentText("Are you sure want to go back?")
                       .setConfirmButton("Confirm"){
                           finish()
                       }
                       .setCancelButton("Cancel"){
                           it.dismiss()
                       }
                       .show()
               }
           })
        }

    }

    private fun enrollmentFocusListener() {
        binding.apply {
            SignUpEnrollmentEdt.setOnFocusChangeListener { _, focus_not_changed ->
                if (!focus_not_changed) {
                    SignUpEnrollmentContainer.helperText = validEnrollment()
                }
            }
        }
    }

    private fun validEnrollment(): String? {
        binding.apply {
            val received_enroll = SignUpEnrollmentEdt.text.toString()

            if (received_enroll == "") {
                return "Required*"
            }
            if (received_enroll.length < 11) {
                return "Enrollment must be of length 11"
            }
            if (!received_enroll.matches("^2[0-9]{10}".toRegex())) {
                return "Invalid enrollment number"
            }
            return null
        }
    }
    private fun passwordFocusListener(){
        binding.apply {
            SignUpPasswordEdt.setOnFocusChangeListener{_,focused->
                if(!focused){
                    SignUpPasswordContainer.helperText=validPassword()
                }
            }
            SignUpConfirmPasswordEdt.setOnFocusChangeListener{_,focused->
                if(!focused){
                    SignUpConfirmPasswordContainer.helperText=validConfirmPassword()
                }

            }

        }

    }
    private fun validPassword():String?{
        binding.apply {
            var received_password=SignUpPasswordEdt.text.toString()

            if(received_password==""){
                return "Required*"
            }

            if(received_password.length<6){
                return "Minimum 6 Character Password"
            }

            if(received_password.length>6 && received_password.matches(".*[A-Z].*".toRegex()) && received_password.matches(".*[a-z].*".toRegex()) && received_password.matches(".*[*@#\$&^%+=].*".toRegex())){
                return "Strong Password..."
            }
            if(!received_password.matches(".*[A-Z].*".toRegex())){
                return "Must Contain 1 Upper-Case Letter"
            }
            if(!received_password.matches(".*[a-z].*".toRegex())){
                return "Must Contain 1 Lower-Case Letter"
            }
            if(!received_password.matches(".*[*@#\$&^%+=].*".toRegex())){
                return "Must Contain 1 Special Character"
            }
            return null
        }

    }
    private fun validConfirmPassword():String?{
        binding.apply {
            var received_confirm_password=SignUpConfirmPasswordEdt.text.toString()

            return if(received_confirm_password==""){
                return "Required*"
            }else null
        }
    }

}
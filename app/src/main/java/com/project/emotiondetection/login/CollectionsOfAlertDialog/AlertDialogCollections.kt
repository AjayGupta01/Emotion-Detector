package com.project.emotiondetection.login.CollectionsOfAlertDialog

import android.content.Context

import cn.pedant.SweetAlert.SweetAlertDialog


object AlertDialogCollections {
        fun successAlertDialog(context:Context,enrolment:String){
            val builder= SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("Login Successful")
                .setContentText("Welcome $enrolment")
                .show()
        }
        fun failAlertDialog(context:Context){
            val builder= SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                .setContentText("Login Failed")
                .show()
        }


}
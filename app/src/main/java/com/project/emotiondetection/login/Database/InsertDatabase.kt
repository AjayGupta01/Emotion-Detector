package com.project.emotiondetection.login.Database

import android.content.Context

object InsertDatabase {
    fun insertMainDatabase(context: Context){

        val main_database= MainDatabase(context)

        main_database.insertData("21012571002","ajay",)

    }
}
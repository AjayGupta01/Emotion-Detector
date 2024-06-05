package com.project.emotiondetection.login.Database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore

class MainDatabase(context: Context?) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object{
        private const val DATABASE_NAME:String="MainDataBase"
        private const val DATABASE_VERSION:Int=1
        private const val TABLE_NAME:String="UserDatabase"

        private const val KEY_ENROLLMENT:String="Enrolment"
        private const val KEY_PASSWORD:String="Password"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("create table $TABLE_NAME($KEY_ENROLLMENT TEXT primary key,$KEY_PASSWORD TEXT)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("drop table if exists $TABLE_NAME")
        onCreate(db)
    }

    fun insertData(enrolment:String,password:String):Long{
        val values=ContentValues()
        values.put(KEY_ENROLLMENT,enrolment)
        values.put(KEY_PASSWORD,password)

        val db=writableDatabase
        return db.insert(TABLE_NAME,null,values)
    }

    fun isUserExist(enrolment: String?,password: String):Boolean{
        var bool=false
        val db=readableDatabase
        var cursor=db.rawQuery("select $KEY_ENROLLMENT,$KEY_PASSWORD from $TABLE_NAME",null)

        while (cursor.moveToNext()){
            if (enrolment==cursor.getString(0) && password==cursor.getString(1))
                bool=true
        }
        return bool
    }

   }


//class MainDataBase(context: Context) {
//    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "userData")
//
//    companion object {
//        val enrolmentKey = stringPreferencesKey("Enrolment")
//        val passwordKey = stringPreferencesKey("Password")
//        val confirmPasswordKey = stringPreferencesKey("Password")
//    }
//
//    suspend fun insertData(
//        context: Context,
//        enrolment: String,
//        password: String,
//        confirmPassword: String
//    ): Boolean {
//        if (enrolment.isBlank() || password.isBlank() || confirmPassword.isBlank()) return false
//        context.dataStore.edit {
//            it[enrolmentKey] = enrolment
//            it[passwordKey] = password
//            it[confirmPasswordKey] = confirmPassword
//        }
//        return true
//    }
//
//    fun isUserExist(enrolment: String?,password: String):Boolean{
//        val bool=
//
//        return bool
//    }
//
//}
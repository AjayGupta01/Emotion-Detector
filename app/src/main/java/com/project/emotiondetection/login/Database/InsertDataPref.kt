package com.project.emotiondetection.login.Database

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

object InsertDataPref {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "userData")

    private val enrolmentKey = stringPreferencesKey("Enrolment")
    private val passwordKey = stringPreferencesKey("Password")
    private val confirmPasswordKey = stringPreferencesKey("confirmPassword")


    suspend fun insertData(
        context: Context,
        enrolment: String,
        password: String,
        confirmPassword: String
    ): Boolean {
        if (enrolment.isBlank() || password.isBlank() || confirmPassword.isBlank()) return false
        context.dataStore.edit {
            it[enrolmentKey] = enrolment
            it[passwordKey] = password
            it[confirmPasswordKey] = confirmPassword
        }
        return true
    }

    suspend fun isUserExist(
        context: Context,
        enteredEnrolment: String?,
        enteredPassword: String?
    ): Boolean {
        if (enteredEnrolment == null && enteredPassword == null) {
            return false
        }
        val enrollment = context.dataStore.data.map {
            it[enrolmentKey]

        }.first()
        val password = context.dataStore.data.map {
            it[passwordKey]
        }.first()

        return enteredEnrolment == enrollment && enteredPassword == password

    }

}
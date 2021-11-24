package com.zk.timetracker

import android.content.Context
import android.util.Log
import android.util.Patterns
import androidx.compose.material.TextFieldColors
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.io.IOException
import java.time.format.DateTimeFormatter
import java.util.*

fun getJsonDataFromAsset(context: Context, fileName: String): String? {
    val jsonString: String
    try {
        jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
    } catch (ioException: IOException) {
        ioException.printStackTrace()
        return null
    }
    return jsonString
}

val DATE_TIME_FORM_FORMAT = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm", Locale.ENGLISH)

@Composable
fun TextFieldDefaults.error(isError: Boolean): TextFieldColors {
    return this.outlinedTextFieldColors(
        focusedBorderColor = if (isError) Color.Red else Color.Black,
        unfocusedBorderColor = if (isError) Color.Red else Color.Black,
        focusedLabelColor = if (isError) Color.Red else Color.Black,
        unfocusedLabelColor = if (isError) Color.Red else Color.Black,
    )
}

//@HiltViewModel
//class FormValidationViewModel @Inject constructor() : ViewModel() {
//
//    val emailAddress = MutableLiveData<String>("")
//
//    val valid = MediatorLiveData<Boolean>().apply {
//        addSource(emailAddress) {
//            val valid = isFormValid(it)
//            Log.d(it, valid.toString())
//            value = valid
//        }
//    }
//
//    fun isFormValid(emailAddress: String): Boolean {
//        return Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches()
//    }
//}
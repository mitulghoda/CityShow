package com.app.cityshow.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import java.lang.StringBuilder

@Keep
open class ErrorBaseModel {
    val code:String=""
    val message:String=""
    val data : Data?=null

    data class Data(
        val params: HashMap<String,Any>?=null,
        val status: Int
    )

    fun getDataMessage(): String {
        if(data==null) return message
        if(data.params.isNullOrEmpty()) return  message
        val formattedMsg = StringBuilder()
        data.params.forEach { any ->
            formattedMsg.append(any.value)
            formattedMsg.append("\n")
            formattedMsg.append("\n")
        }
        return  formattedMsg.toString().trim()
    }
}
package com.app.cityshow.utility

import android.text.TextUtils
import android.util.Patterns
import android.widget.EditText
import com.google.android.material.textfield.TextInputLayout
import java.util.regex.Pattern


object Validator {
    private const val PASSWORD_PATTERN =
        "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$"

    fun isValidPassword(password: String?): Boolean {
        if (password.isNullOrEmpty()) return false
        val matcher = Pattern.compile(PASSWORD_PATTERN).matcher(password)
        return matcher.matches()
    }

    fun isValidEmail(target: CharSequence?): Boolean {
        if (target == null) return false
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches()
    }

    fun isEmptyFieldValidate(strField: String): Boolean {
        var isEmptyField = false
        if (strField.isEmpty()) isEmptyField = true
        return isEmptyField
    }

    fun isPasswordValidate(password: String): Boolean {
        var isValid = false
        isValid = password.length >= 8
        return isValid
    }

    fun isPhoneNumberValidate(number: String): Boolean {
        return number.isNotEmpty() && number.length >= 9
    }

    fun setError(edtView: EditText, msg: String?) {
        edtView.error = msg
        edtView.requestFocus()
    }

    fun setError(edtView: TextInputLayout, msg: String?) {
        edtView.error = msg
        edtView.requestFocus()
    }
}
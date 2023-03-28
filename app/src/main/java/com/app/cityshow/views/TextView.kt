package com.app.cityshow.views

import android.app.TimePickerDialog
import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class TextView : AppCompatTextView {
    fun setTime() {
        TimePickerDialog(
            this.context,
            { p0, hourOfDay, minute ->
                val time = when {
                    hourOfDay == 0 -> {
                        if (minute < 10) {
                            "${hourOfDay + 12}:0${minute} am"
                        } else {
                            "${hourOfDay + 12}:${minute} am"
                        }
                    }
                    hourOfDay > 12 -> {
                        if (minute < 10) {
                            "${hourOfDay - 12}:0${minute} pm"
                        } else {
                            "${hourOfDay - 12}:${minute} pm"
                        }
                    }
                    hourOfDay == 12 -> {
                        if (minute < 10) {
                            "${hourOfDay}:0${minute} pm"
                        } else {
                            "${hourOfDay}:${minute} pm"
                        }
                    }
                    else -> {
                        if (minute < 10) {
                            "${hourOfDay}:${minute} am"
                        } else {
                            "${hourOfDay}:${minute} am"
                        }
                    }
                }
                this.text = time
            },
            12,
            10,
            false
        ).show()
    }

    constructor(context: Context?) : super(context!!)

    constructor(context: Context?, attrs: AttributeSet?) : super(context!!, attrs)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) :
            super(context!!, attrs, defStyleAttr)

    val string: String
        get() {
            val charSequence = super.getText() ?: return ""
            return charSequence.toString()
        }

//    override fun setText(charSequence: CharSequence, type: BufferType) {
//        var text = charSequence
//        if (text.isNotEmpty()) {
//            text = text[0].toString().uppercase() + text.subSequence(1, text.length)
//        }
//        super.setText(text, type)
//    }
}
package com.dicoding.submission1intermediate.ui.customview

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.dicoding.submission1intermediate.R

class PasswordEditText: AppCompatEditText {
    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private val errorColor = resources.getColor(R.color.red)
    private val defaultColor = resources.getColor(R.color.white)
    private var isValid = false

    private fun init() {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.length < 8) {
                    error = resources.getString(R.string.password_error)
                    setTextColor(errorColor)
                    isValid = false
                } else {
                    error = null
                    setTextColor(defaultColor)
                    isValid = true
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
    }

}
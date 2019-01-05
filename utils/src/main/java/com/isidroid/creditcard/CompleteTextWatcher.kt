package com.isidroid.creditcard

import android.text.Editable
import android.widget.EditText

internal class CompleteTextWatcher(editText: EditText, private val afterTextChanged: (String?) -> Unit) : ICreditCardTextWatcher(editText) {
    override fun afterTextChanged(s: Editable?) {
        val text = s?.toString()
        afterTextChanged(text)
    }
}
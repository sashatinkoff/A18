package com.isidroid.utils.utils.views

import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import androidx.appcompat.widget.AppCompatEditText

class CostInputFilter(private var editText: AppCompatEditText) : TextWatcher {
    private var delimiter = "."
    private var maxFractionLength = 2
    private var selection = 0
    private var value = 0.0
    private var textSelected = true
    private var hasAlreadyFocused = false

    fun withMaxFractionLength(max: Int) = apply { maxFractionLength = max }
    fun withDelimiter(value: String) = apply { delimiter = value }
    fun withTextSelected(value: Boolean) = apply { textSelected = value }

    fun create() = apply {
        editText.setRawInputType(InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL)
        editText.addTextChangedListener(this)

        if (textSelected) {
            editText.isFocusable = true
            editText.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus && !hasAlreadyFocused) {
                    editText.setSelection(0, editText.text?.length ?: 0)
                    hasAlreadyFocused = true
                }
            }
        }
    }

    fun destroy() = apply { editText.removeTextChangedListener(this) }

    override fun afterTextChanged(s: Editable?) {}
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        selection = start + count
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        editText.removeTextChangedListener(this)

        var text = s.toString()
        if (text.contains(delimiter)) {
            // calc a number of digits after delimiter
            val delimiterPosition = text.indexOf(delimiter) + 1
            val fractionLength = text.length - delimiterPosition

            if (fractionLength > maxFractionLength) {
                text = text.substring(0, delimiterPosition + maxFractionLength)
                selection = text.length - 1
            }
        }


//        val regex = Regex("[^0-9.]")
//        value = regex.replace(text, "").toDouble()
//        text = NumberFormat.getCurrencyInstance(Locale.getDefault()).format(value)


        try {
            editText.setText(text)
            editText.setSelection(selection + 1)
        } catch (e: Exception) {
            editText.setSelection(text.length)
        }
        editText.addTextChangedListener(this)
    }
}
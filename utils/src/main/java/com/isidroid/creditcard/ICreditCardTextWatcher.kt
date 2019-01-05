package com.isidroid.creditcard

import android.text.TextWatcher
import android.widget.EditText

internal abstract class ICreditCardTextWatcher(protected val editText: EditText) : TextWatcher {
    protected var changingText = false
    protected var cursorPos: Int = 0
    protected var editVelocity: Int = 0

    fun addTo(textWatchers: MutableMap<EditText, MutableList<TextWatcher>>) = apply {
        editText.addTextChangedListener(this)

        if (!textWatchers.containsKey(editText)) textWatchers[editText] = mutableListOf()
        textWatchers[editText]?.add(this)
    }

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        if (this.changingText) return
        this.editVelocity = count - before
        this.cursorPos = start + count
    }


    protected fun formatBlock(builder: StringBuilder,
                              cleaned: String, origLen: Int,
                              cursor: Pair<Int, Int>,
                              completed: Boolean,
                              delimiter: String = " "): String? {
        return when {
            origLen <= cursor.second -> {
                builder.append(cleaned.substring(cursor.first))
                builder.toString()
            }
            completed -> {
                builder.append(cleaned.substring(cursor.first, cursor.second))
                null
            }
            else -> {
                builder.append(cleaned.substring(cursor.first, cursor.second) + delimiter)
                null
            }
        }
    }
}
package com.isidroid.a18

import android.text.Editable
import androidx.annotation.XmlRes
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import com.google.android.material.chip.ChipDrawable
import timber.log.Timber
import android.text.Spanned
import android.text.TextWatcher
import android.text.method.KeyListener
import android.text.style.ImageSpan
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast


class TextTag(
    private val editText: AppCompatEditText,
    @XmlRes private val chipResource: Int,
    private val imeAction: Int = EditorInfo.IME_ACTION_DONE
) {
    private val context = editText.context
    private var spannedLength = 0


    init {
        editText.imeOptions = imeAction

        create()
    }

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun afterTextChanged(editable: Editable?) {
            editable ?: return
            if (editable.length == 4) addChip(editable)
        }

        override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
            text ?: return
            if (text.length == spannedLength - 0) spannedLength = text.length
        }
    }

    private fun create() {
        editText.setOnEditorActionListener { _, actionId, event ->
            if (actionId == imeAction) addChip(editText.editableText)
            true
        }
    }

    private fun addChip(editable: Editable) {
        ChipDrawable.createFromResource(context, chipResource).apply {
            text = editable.subSequence(0, editable.length)
            setBounds(0, 0, intrinsicWidth, intrinsicHeight)

            val span = ImageSpan(this)
            editable.setSpan(span, 0, editable.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            spannedLength = editable.length
        }
    }
}
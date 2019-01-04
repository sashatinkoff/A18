package com.isidroid.a18.creditcard

import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.widget.AppCompatEditText
import com.isidroid.a18.creditcard.CreditCardUtil.CC_LEN_FOR_TYPE
import java.lang.ref.WeakReference

class CreditCardTextWatcher(editText: AppCompatEditText, private val onCardTypeCallback: (CreditCardUtil.CardType) -> Unit) : TextWatcher {
    private var changingText = false
    private var cursorPos: Int = 0
    private var editVelocity: Int = 0
    private val editText: WeakReference<AppCompatEditText> = WeakReference(editText)

    init {
        editText.addTextChangedListener(this)
    }

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        if (this.changingText) return
        this.editVelocity = count - before
        this.cursorPos = start + count
    }

    override fun afterTextChanged(s: Editable) {
        if (changingText) return
        changingText = true

        val type = if (s.length >= CC_LEN_FOR_TYPE)
            CreditCardUtil.findCardType(s.toString().replace("[^\\d]".toRegex(), ""))
        else CreditCardUtil.CardType.UNKNOWN
        onCardTypeCallback(type)

        val formatted = CreditCardUtil.formatForViewing(s, type)
        s.replace(0, s.length, formatted)

        val i = this.cursorPos
        if (this.cursorPos >= formatted.length) {
            this.cursorPos = formatted.length
        }
        if (this.editVelocity > 0
                && this.cursorPos > 0
                && formatted[-1 + this.cursorPos] == ' ') {
            this.cursorPos += 1
        }
        if (this.editVelocity < 0
                && this.cursorPos > 1
                && formatted[-1 + this.cursorPos] == ' ') {
            this.cursorPos -= 1
        }
        val et = editText.get()
        if (this.cursorPos != i && et != null) {
            val position = if (this.cursorPos > formatted.length) formatted.length else this.cursorPos
            et.setSelection(position)
        }


        changingText = false
    }

}

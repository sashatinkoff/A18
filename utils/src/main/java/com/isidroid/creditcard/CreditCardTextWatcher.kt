package com.isidroid.creditcard

import android.text.Editable
import android.widget.EditText
import com.isidroid.creditcard.CreditCardForm.Companion.CC_LEN_FOR_TYPE

internal class CreditCardTextWatcher(editText: EditText,
                                     private val creditCardForm: CreditCardForm,
                                     private val onCardTypeCallback: (CardType?) -> Unit) : ICreditCardTextWatcher(editText) {

    override fun afterTextChanged(s: Editable) {
        if (changingText) return
        changingText = true

        val creditCardType = creditCardForm.findCardType(s.toString().replace("[^\\d]".toRegex(), ""))
        onCardTypeCallback(creditCardType)

        val formatted = formatForViewing(s, creditCardType)
        s.replace(0, s.length, formatted)

        val i = cursorPos
        if (cursorPos >= formatted.length) cursorPos = formatted.length
        if (editVelocity > 0 && cursorPos > 0 && formatted[-1 + cursorPos] == ' ') cursorPos += 1
        if (editVelocity < 0 && cursorPos > 1 && formatted[-1 + cursorPos] == ' ') cursorPos -= 1

        if (cursorPos != i) {
            val position = if (cursorPos > formatted.length) formatted.length else cursorPos
            editText.setSelection(position)
        }

        changingText = false
    }

    private fun formatForViewing(cc: CharSequence, type: CardType?): String {
        val cleaned = cc.toString().replace("[^\\d]".toRegex(), "")
        val origLen = cleaned.length

        if (origLen <= CC_LEN_FOR_TYPE) return cleaned
        val builder = StringBuilder(20)

        if (type != null)
            type.rules.forEachIndexed { index, pair ->
                val force = index == type.rules.size - 1
                val itResult = formatBlock(builder, cleaned, origLen, pair, force)
                if (itResult != null) {
                    return if (index == type.rules.size) itResult.substring(0, itResult.length - 1)
                    else itResult
                }
            }
        else builder.append(cleaned)
        return builder.toString()
    }
}

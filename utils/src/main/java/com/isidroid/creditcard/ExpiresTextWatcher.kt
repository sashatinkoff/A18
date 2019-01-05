package com.isidroid.utils.creditcard

import android.text.Editable
import android.widget.EditText
import com.isidroid.creditcard.ICreditCardTextWatcher
import java.text.SimpleDateFormat
import java.util.*

internal class ExpiresTextWatcher(editText: EditText) : ICreditCardTextWatcher(editText) {

    override fun afterTextChanged(s: Editable) {
        if (changingText) return
        changingText = true

        val formatted = formatExpires(s)
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

    private fun checkExpiresDate(date: List<Int>): Boolean {
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val nowString = simpleDateFormat.format(Date())

        val expires = simpleDateFormat.parse("20${date[1]}-${date[0]}-01")
        val now = simpleDateFormat.parse(nowString)
        return expires > now
    }

    fun formatExpires(cc: CharSequence): String {
        var cleaned = cc.toString().replace("[^\\d]".toRegex(), "")
        val origLen = cleaned.length

        val date = arrayListOf(0, 0)
        val hasData = arrayListOf(cleaned.length >= 2, cleaned.length == 4)

        if (hasData[0]) date[0] = cleaned.substring(0, 2).toInt()
        if (hasData[1]) date[1] = cleaned.substring(2, 4).toInt()

        val monthValid = !hasData[0] || date[0] in 1..12
        val yearValid = !hasData[1] || checkExpiresDate(date)

        if (!monthValid) return ""
        if (!yearValid) cleaned = cleaned.substring(0, 2)

        val builder = StringBuilder(5)
        val rules = arrayListOf(Pair(0, 2), Pair(2, 4))

        rules.forEachIndexed { index, pair ->
            val force = index == rules.size - 1
            val itResult = formatBlock(builder, cleaned, origLen, pair, force, "/")
            if (itResult != null) {
                return if (index == rules.size) itResult.substring(0, itResult.length - 1)
                else itResult
            }
        }

        return builder.toString()
    }

}

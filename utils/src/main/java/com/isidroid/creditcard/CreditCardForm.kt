package com.isidroid.creditcard

import android.text.InputFilter
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import com.isidroid.utils.creditcard.ExpiresTextWatcher
import java.text.SimpleDateFormat
import java.util.*

class CreditCardForm(private val cardInput: EditText) {
    private var onCardType: ((CardType?) -> Unit)? = null
    private var onFormComplete: ((Boolean) -> Unit)? = null

    private var cvsInput: EditText? = null
    private var expiresInput: EditText? = null
    private val cardTypes = mutableListOf<CardType>()
    var selectedCardType: CardType? = null

    private var textWatchers = mutableMapOf<EditText, MutableList<TextWatcher>>()
    private var focusChains = mutableMapOf<View, View?>()

    fun onCardType(action: (CardType?) -> Unit) = apply { onCardType = action }
    fun onFormComplete(action: (Boolean) -> Unit) = apply { onFormComplete = action }

    fun appendCard(creditCard: CardType) = apply {
        if (creditCard.valid()) {
            cardTypes.firstOrNull { it == creditCard }?.let { cardTypes.remove(it) }
            cardTypes.add(creditCard)
        }
    }

    fun cvsInput(input: EditText) = apply { cvsInput = input }
    fun expiresInput(input: EditText) = apply {
        expiresInput = input
        cvsInput?.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(3))
    }

    fun focusChains(vararg inputs: View) = apply {
        inputs.forEachIndexed { index, view ->
            val nextInput = inputs.getOrNull(index + 1)
            focusChains[view] = nextInput
        }
    }

    private fun inputCompleteWatcher(input: EditText, condition: Boolean) {
        if (condition) focusChains[input]?.requestFocus()
        onFormComplete?.invoke(isComplete())
    }

    fun create() = apply {
        enableCardFormat()
        CompleteTextWatcher(cardInput) { inputCompleteWatcher(cardInput, isCardNumberComplete()) }.addTo(textWatchers)
        cvsInput?.let { view -> CompleteTextWatcher(view) { inputCompleteWatcher(view, isCvsComplete()) }.addTo(textWatchers) }

        expiresInput?.let { view ->
            ExpiresTextWatcher(view).addTo(textWatchers)
            CompleteTextWatcher(view) { inputCompleteWatcher(view, isExpiresComplete()) }.addTo(textWatchers)
        }
    }

    fun disableCardFormat() = textWatchers[cardInput]?.forEach { cardInput.removeTextChangedListener(it) }
    fun enableCardFormat() = apply {
        CreditCardTextWatcher(cardInput, this) { cardType ->
            val length = cardType?.cvsLength ?: 0
            cvsInput?.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(length))

            selectedCardType = cardType
            onCardType?.invoke(cardType)
        }.addTo(textWatchers)
    }

    fun destroy() = apply {
        textWatchers.keys.forEach { input ->
            textWatchers[input]?.forEach { input.removeTextChangedListener(it) }
        }
    }

    fun isComplete() = arrayListOf(isCardNumberComplete(), isExpiresComplete(), isCvsComplete()).none { !it }
    fun isCardNumberComplete() = selectedCardType?.length() == cardNumberRaw().length
    fun isExpiresComplete() = expiresInput == null || (expiresDate() ?: Date(0) > Date())
    fun isCvsComplete() = cvsInput == null || cvs()?.toString()?.length ?: -1 == selectedCardType?.cvsLength

    fun cardNumberRaw() = cardInput.text.toString().replace("[^\\d]".toRegex(), "")
    fun cardNumberFormatted() = cardInput.text.toString()
    fun expires() = expiresInput?.text.toString()
    fun expiresDate(): Date? {
        val simpleDateFormat = SimpleDateFormat("MM/yy", Locale.getDefault())
        return try {
            simpleDateFormat.parse(expiresInput?.text?.toString())
        } catch (e: Exception) {
            null
        }
    }

    fun cvs() = try {
        cvsInput?.text?.toString()?.toInt()
    } catch (e: Exception) {
        0
    }

    fun findCardType(number: String): CardType? {
        if (number.length < CC_LEN_FOR_TYPE) return null

        cardTypes.forEach {
            val matcher = it.pattern?.matcher(number.subSequence(0, CC_LEN_FOR_TYPE))
            if (matcher?.matches() == true) return it
        }
        return null
    }

    fun defaults() = apply {
        appendCard(CardType().name(AMEX).cvsLength(4).pattern("^3[47][0-9]{2}$").addRule(4, 6, 5))
        appendCard(CardType().name(VISA).pattern("^4[0-9]{3}?").addRule(4, 4, 4, 4))
        appendCard(CardType().name(MASTER_CARD).pattern("^5[1-5][0-9]{2}$").addRule(4, 4, 4, 4))
        appendCard(CardType().name(DISCOVER).pattern("^6(?:011|5[0-9]{2})$").addRule(4, 4, 4, 4))
        appendCard(CardType().name(DINERS_CLUB).pattern("^3(?:0[0-5]|[68][0-9])[0-9]$").addRule(4, 4, 4, 2))

        cardTypes.forEach { it.length() }
    }

    companion object {
        const val CC_LEN_FOR_TYPE = 4

        const val AMEX = "American Express"
        const val VISA = "Visa"
        const val MASTER_CARD = "Master Card"
        const val DISCOVER = "Discover"
        const val DINERS_CLUB = "Diners club"
    }
}
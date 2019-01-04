package com.isidroid.a18.creditcard

import java.util.regex.Pattern

object CreditCardUtil {
    const val CC_LEN_FOR_TYPE = 4

    // See: http://www.regular-expressions.info/creditcard.html
    val TYPE_AMEX = Pattern.compile("^3[47][0-9]{2}$")// AMEX 15
    val TYPE_VISA = Pattern.compile("^4[0-9]{3}?")// VISA 16
    val TYPE_MC = Pattern.compile("^5[1-5][0-9]{2}$")// MC 16
    val TYPE_DISCOVER = Pattern.compile("^6(?:011|5[0-9]{2})$") // Discover
    val TYPE_DINERS_CLUB = Pattern.compile("^3(?:0[0-5]|[68][0-9])[0-9]$")

    enum class CardType {
        VISA, MASTERCARD, AMEX, DISCOVER, DINERS_CLUB, UNKNOWN
    }

    fun cvvLength(card: CardType) = when (card) {
        CardType.AMEX -> 4
        CardType.VISA, CardType.MASTERCARD, CardType.DISCOVER, CardType.DINERS_CLUB -> 3
        else -> 0
    }

    private fun formatBlock(builder: StringBuilder, cleaned: String, origLen: Int, cursor: Pair<Int, Int>, completed: Boolean): String? {
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
                builder.append(cleaned.substring(cursor.first, cursor.second) + " ")
                null
            }
        }
    }

    fun formatForViewing(cc: CharSequence, type: CardType): String {
        val cleaned = cc.toString().replace("[^\\d]".toRegex(), "")
        val origLen = cleaned.length

        if (origLen <= CC_LEN_FOR_TYPE) return cleaned
        val builder = StringBuilder(20)
        val rules = when (type) {
            // { 4-4-4-4}
            CardType.VISA, CardType.MASTERCARD, CardType.DISCOVER ->
                arrayListOf(Pair(0, 4), Pair(4, 8), Pair(8, 12), Pair(12, 16))

            // {4-4-4-2}
            CardType.DINERS_CLUB -> arrayListOf(Pair(0, 4), Pair(4, 8), Pair(8, 12), Pair(12, 14))

            // {4-6-5}
            CardType.AMEX -> arrayListOf(Pair(0, 4), Pair(4, 10), Pair(10, 15))

            else -> null
        }

        rules?.forEachIndexed { index, pair ->
            val force = index == rules.size - 1
            val itResult = formatBlock(builder, cleaned, origLen, pair, force)
            if (itResult != null) {
                return if (index == rules.size) itResult.substring(0, itResult.length - 1)
                else itResult
            }
        }

        return builder.toString()
    }

    fun findCardType(s: CharSequence): CreditCardUtil.CardType {
        if (s.length < CC_LEN_FOR_TYPE)
            return CreditCardUtil.CardType.UNKNOWN

        for (cardType in CreditCardUtil.CardType.values()) {
            val pattern: Pattern? = when (cardType) {
                CreditCardUtil.CardType.AMEX -> TYPE_AMEX
                CreditCardUtil.CardType.DISCOVER -> TYPE_DISCOVER
                CreditCardUtil.CardType.MASTERCARD -> TYPE_MC
                CreditCardUtil.CardType.VISA -> TYPE_VISA
                CreditCardUtil.CardType.DINERS_CLUB -> TYPE_DINERS_CLUB
                else -> null
            }

            val matcher = pattern?.matcher(s.subSequence(0, CC_LEN_FOR_TYPE))
            if (matcher?.matches() == true) return cardType
        }

        return CreditCardUtil.CardType.UNKNOWN
    }
}
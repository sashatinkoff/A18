package com.isidroid.creditcard

import java.util.regex.Pattern

class CardType {
    var name = ""
    var cvsLength = 3

    internal var pattern: Pattern? = null
    internal val rules = mutableListOf<Pair<Int, Int>>()

    fun name(value: String) = apply { name = value }
    fun pattern(value: String) = apply { pattern = Pattern.compile(value) }
    fun cvsLength(value: Int) = apply { cvsLength = value }
    fun length() = rules.lastOrNull()?.second ?: 0

    fun addRule(vararg data: Int) = apply {
        var start = 0
        data.forEach { size ->
            val end = start + size
            rules.add(Pair(start, end))
            start = end
        }
    }

    fun valid() = name.isNotEmpty() && pattern != null && rules.isNotEmpty() && cvsLength > 0
    override fun toString() = "CardType(name='$name', cvsLength=$cvsLength, length=${length()})"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as CardType
        if (name != other.name) return false
        return true
    }

    override fun hashCode() = name.hashCode()
}
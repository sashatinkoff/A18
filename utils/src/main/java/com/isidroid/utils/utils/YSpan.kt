package com.isidroid.utils.utils

import android.content.Context
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.*
import android.text.style.DynamicDrawableSpan.ALIGN_BASELINE
import android.view.View
import com.isidroid.utils.extensions.resourceFromAttr

open class YSpan(private val context: Context? = null) {
    protected val builder = SpannableStringBuilder()
    protected var start = 0

    fun append(str: String) = apply {
        start = builder.length
        builder.append(str)
    }

    fun append(res: Int?) = apply {
        try {
            append(context!!.getString(res!!))
        } catch (e: Exception) {
        }
    }

    fun isEmpty() = builder.isEmpty()
    fun br() = apply { builder.append("\n") }

    fun style(vararg spans: Any) = apply {
        spans.forEach {
            try {
                builder.setSpan(it, start, builder.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            } catch (e: Exception) {
            }
        }
    }

    fun image(res: Int, after: Int = 1, before: Int = 1) = apply {
        if (context == null) throw Exception("Context is missing in the constructor")
        imagespan(ImageSpan(context, res, ALIGN_BASELINE), after, before)
    }

    fun imageSpan(
        attr: Int,
        verticalAlign: Int = ALIGN_BASELINE
    ) = apply {

        append(" ")
        style(ImageSpan(context!!, context.resourceFromAttr(attr), verticalAlign))
    }

    fun linkify() = apply {
        style(UnderlineSpan())
    }

    fun imagespan(span: ImageSpan, after: Int = 1, before: Int = 1) = apply {
        val spaces = arrayListOf("", "")
        (0..before).forEach { spaces[0] += " " }
        (0..after).forEach { spaces[1] += " " }
        append(spaces[0]).style(span).append(spaces[1])
    }

    fun onclick(callback: (url: String) -> Unit) = apply {
        val span = object : URLSpan("") {
            override fun onClick(widget: View) {
                val text = builder.substring(start, builder.length)
                callback(text)
            }
        }

        style(span)
    }


    fun build() = builder
}
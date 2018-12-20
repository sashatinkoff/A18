package com.isidroid.a18

import android.content.Context
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.method.LinkMovementMethod
import android.text.style.DynamicDrawableSpan.ALIGN_BASELINE
import android.text.style.ImageSpan
import android.text.style.URLSpan
import android.view.View
import android.widget.TextView

class YSpan(private val context: Context? = null) {
    private val builder = SpannableStringBuilder()
    private var start = 0

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
package com.isidroid.a18.extensions

import android.content.res.TypedArray
import androidx.annotation.StyleableRes
import com.isidroid.a18.utils.Utils.tryCatch

fun TypedArray.bool(
    @StyleableRes index: Int,
    cb: (Boolean) -> Unit
) = tryCatch(block = { if (hasValue(index)) cb(getBoolean(index, false)) })

fun TypedArray.resourceId(
    @StyleableRes index: Int,
    cb: (Int) -> Unit,
    default: Int = -1
) = tryCatch(block = { if (hasValue(index)) cb(getResourceId(index, default)) })

fun TypedArray.int(
    @StyleableRes index: Int,
    cb: (Int) -> Unit,
    default: Int = -1
) = tryCatch(block = { if (hasValue(index)) cb(getInt(index, default)) })


fun TypedArray.size(@StyleableRes index: Int, cb: (Int) -> Unit) =
    tryCatch(block = {
        if (hasValue(index)) {
            val size = getDimension(index, -1f)
            if (size > 0) cb(size.toInt())
        }
    })
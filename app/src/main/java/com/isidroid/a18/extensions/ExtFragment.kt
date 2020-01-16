package com.isidroid.a18.extensions

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import java.io.InvalidClassException
import java.io.Serializable

fun Fragment.put(key: String, value: Any?) =
    apply { putArument(fragment = this, key = key, value = value) }

fun DialogFragment.put(key: String, value: Any?) =
    apply { putArument(fragment = this, key = key, value = value) }

fun Context.showFragmentDialog(
    tag: String,
    fragment: DialogFragment
) = when {
    this !is AppCompatActivity -> throw InvalidClassException("context is not AppCompatActivity")
    (supportFragmentManager.findFragmentByTag(tag) as? DialogFragment)?.dialog?.isShowing == true -> false
    else -> {
        supportFragmentManager.beginTransaction().apply { fragment.show(this, tag) }
        true
    }
}


private fun putArument(fragment: Fragment, key: String, value: Any?) = fragment.apply {
    value ?: return@apply
    arguments = arguments ?: Bundle()

    when (value) {
        is String -> arguments?.putString(key, value)
        is Serializable -> arguments?.putSerializable(key, value)
        is Long -> arguments?.putLong(key, value)
        is Double -> arguments?.putDouble(key, value)
        is Boolean -> arguments?.putBoolean(key, value)
        is Float -> arguments?.putFloat(key, value)
    }
}

fun Fragment.isActive() = activity?.isFinishing == false
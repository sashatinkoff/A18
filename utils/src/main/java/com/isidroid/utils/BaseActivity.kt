package com.isidroid.utils

import android.os.Bundle
import android.view.View
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.lifecycle.LifecycleObserver
import com.isidroid.utils.extensions.hideSoftKeyboard
import com.isidroid.views.BottomsheetHelper


/**
 * Need to call onCreateBinding() onCreateViewModel()
 */
abstract class BaseActivity : AppCompatActivity(), LifecycleObserver {
    protected var bottomsheetHelper: BottomsheetHelper? = null

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycle.addObserver(this)
        onCreateViewModel()
    }

    override fun onPause() {
        super.onPause()
        hideSoftKeyboard()
    }

    /**
     * @param view: View - a view with bottom sheet
     * @param content: CoordinatorLayout? - a view to be dimmed
     * @param dimConfig: (BottomsheetHelper.Dim) - a config for dim (alpha / color / interpolator / duration)
     */
    protected fun createBottomsheet(
        view: View,
        content: CoordinatorLayout? = null,
        dimConfig: ((BottomsheetHelper.Dim?) -> Unit)? = null
    ) =
        BottomsheetHelper(view)
            .withDim(content)
            .create().apply {
                dimConfig?.invoke(dim)
                dim?.create()
                bottomsheetHelper = this
            }

    protected fun createBottomsheet(view: View, config: (BottomsheetHelper) -> Unit) =
        BottomsheetHelper(view).apply {
            config(this)
            bottomsheetHelper = this
        }

    open fun onCreateViewModel() {}
    override fun onBackPressed() {
        if (bottomsheetHelper?.isExpanded() == true) bottomsheetHelper?.collapse()
        else super.onBackPressed()
    }
}
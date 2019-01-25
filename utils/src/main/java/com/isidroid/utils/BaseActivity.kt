package com.isidroid.utils

import android.os.Bundle
import android.view.View
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.lifecycle.LifecycleObserver
import com.isidroid.utils.utils.views.BottomsheetHelper
import com.isidroid.utils.utils.views.YViewUtils


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
        YViewUtils.hideSoftKeyboard(this)
    }

    protected fun createBottomsheet(view: View,
                                    content: CoordinatorLayout? = null) =
            BottomsheetHelper(view).withDim(content).create().apply { bottomsheetHelper = this }

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
package com.isidroid.a18

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.isidroid.utilsmodule.utils.YViewUtils
import com.isidroid.utilsmodule.utils.views.BackdropHandler
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private var back1Height = 0
    private var back2Height = 0
    private var parentHeight = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        backgroundView.post {
            parentHeight = YViewUtils.height(backgroundView)
        }
        back1.post {
            back1Height = YViewUtils.height(back1)
        }
        back2.post { back2Height = YViewUtils.height(back2) }

        backgroundSwitcher.setOnClickListener {
            val first = back1.visibility == View.VISIBLE

            back1.visibility = if (first) View.GONE else View.VISIBLE
            back2.visibility = if (!first) View.GONE else View.VISIBLE
        }

        val listener = BackdropHandler(container, backgroundView)
                .apply {
                    duration = 300L
                }

        toolbar.setNavigationOnClickListener {
            listener.apply {
                val height = if (back1.visibility == View.VISIBLE) back1Height else back2Height
                backgroundContainerHeight(height + parentHeight)
                onClick()
            }
        }
    }
}

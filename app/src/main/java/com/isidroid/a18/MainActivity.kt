package com.isidroid.a18

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.isidroid.utilsmodule.utils.views.BackdropHandler
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val listener = BackdropHandler(this, appbar).apply { animateIcons(R.drawable.vector_back_arrow_to_cross) }

        if(listener.isBackdropShown()) listener.hide()
        else listener.show()
    }
}

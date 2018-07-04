package com.isidroid.a18

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.isidroid.utilsmodule.utils.views.BackdropClickListener
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_main.*


@RequiresApi(Build.VERSION_CODES.M)
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        object : BackdropClickListener(this, appbar) {
            override fun onClick(view: View) {
                super.onClick(view)


            }
        }

    }


}

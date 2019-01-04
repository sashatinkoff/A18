package com.isidroid.a18

import android.os.Bundle
import com.isidroid.a18.creditcard.CreditCardTextWatcher
import com.isidroid.a18.creditcard.CreditCardUtil
import com.isidroid.a18.databinding.ActivityMainBinding
import com.isidroid.utils.BindActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BindActivity<ActivityMainBinding>() {
    override val resource = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        CreditCardTextWatcher(creditCardInput) { type -> textview.text = "${type.name}, length=${CreditCardUtil.cvvLength(type)}" }
    }
}
package com.isidroid.a18

import android.os.Bundle
import com.isidroid.a18.databinding.ActivityMainBinding
import com.isidroid.creditcard.CreditCardForm
import com.isidroid.utils.BindActivity
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber


class MainActivity : BindActivity<ActivityMainBinding>() {
    override val resource = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val creditCards = CreditCardForm(creditCardInput)
                .defaults()
                .cvsInput(cvsInput)
                .expiresInput(expiresInput)
                .focusChains(creditCardInput, expiresInput, cvsInput, btnRead)
                .onCardType { type -> textview.text = "$type" }
                .onFormComplete { btnCar.isEnabled = it }
                .create()

//        expiresInput.post { expiresInput.requestFocus() }

        btnRead.setOnClickListener {
            Timber.i("card='${creditCards.cardNumberRaw()}'\n" +
                    "formatted=${creditCards.cardNumberFormatted()}\n" +
                    "expires=${creditCards.expires()}\n" +
                    "expiresDate=${creditCards.expiresDate()}\n" +
                    "cvs=${creditCards.cvs()}\n" +
                    "isComplete=${creditCards.isComplete()}")
        }
    }
}
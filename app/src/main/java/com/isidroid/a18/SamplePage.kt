package com.isidroid.a18

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.isidroid.a18.databinding.SamplePageBinding
import com.isidroid.utils.BasePage
import timber.log.Timber


class SamplePage: BasePage<SamplePageBinding>() {
    override val resource = R.layout.sample_page

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Timber.i("onCreateView")
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Timber.i("onViewCreated")
        super.onViewCreated(view, savedInstanceState)
    }
}
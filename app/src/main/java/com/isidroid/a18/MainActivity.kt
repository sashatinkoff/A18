package com.isidroid.a18

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.animation.*
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.isidroid.a18.backdrop.*
import com.isidroid.a18.databinding.SampleBackdropBinding
import com.isidroid.utils.BaseActivity
import com.isidroid.utils.addTo
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.sample_backdrop.view.*
import timber.log.Timber
import java.util.*


class MainActivity : BaseActivity() {
    private val compositeDisposable = CompositeDisposable()
    private lateinit var backdrop: Backdrop2
    private var colorPosition = 0
    private var interpolatorPosition = 0
    private val interpolatorsList = arrayListOf<Interpolator>(
            LinearInterpolator(), AccelerateInterpolator(), DecelerateInterpolator(),
            AccelerateDecelerateInterpolator(), AnticipateInterpolator(),
            OvershootInterpolator(), AnticipateOvershootInterpolator(), BounceInterpolator(), CycleInterpolator(1f)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        backdrop = Backdrop2(container, backdropContainer)
                .duration(300L)
                .interpolator(interpolatorsList[2])

                .onCollapse { log("onCollapse") }
                .onExpand { log("onExpand") }
                .onCollapseStarted { log("onCollapseStarted") }
                .onExpandStarted { log("onExpandStarted") }
                .onCollapseDone { log("onCollapseDone") }
                .onExpandDone { log("onExpandDone") }
                .onDestroyStarted { log("onDestroyStarted") }
                .onDestroy { log("onDestroy") }

        BackdropActionDecorator(button)
                .icons(android.R.drawable.ic_delete, android.R.drawable.ic_dialog_alert)
                .add(backdrop)

        BackdropActionDecorator(imageview)
                .withAnimation()
                .icons(R.drawable.vector_cross_to_back_arrow, R.drawable.vector_back_arrow_to_cross)
                .add(backdrop)


        button.setOnClickListener { backdrop.expand(true) }
        colorButton.setOnClickListener { changeColor() }
        loadContentButton.setOnClickListener { loadContent() }
        interpolators.setOnClickListener { changeInterpolator() }
        destroyButton.setOnClickListener { backdrop.destroy() }
    }

    private fun changeInterpolator() {
        interpolatorPosition++
        val interpolator = try {
            interpolatorsList[interpolatorPosition]
        } catch (e: Exception) {
            interpolatorPosition = 0
            interpolatorsList[interpolatorPosition]
        }

        backdrop.interpolator(interpolator)
        Toast.makeText(this, "Switched interpolator to ${interpolator.javaClass}", Toast.LENGTH_LONG).show()
    }

    private fun changeColor() {
        val colors = arrayListOf(
                ContextCompat.getColor(this, R.color.colorPrimaryDark),
                Color.parseColor("#FFFFFF"))


        val color = if (colorPosition % 2 == 0) colors[0] else colors[1]
        colorPosition++


        toolbar.setBackgroundColor(color)
        (backdropContainer.parent as View).setBackgroundColor(color)
    }

    private fun loadContent() {
        val binder: SampleBackdropBinding = DataBindingUtil.inflate(LayoutInflater.from(this),
                R.layout.sample_backdrop, backdropContainer, false
        )
        binder.message = "Hey, it's a DataBinder sample"

        backdrop
                .view(binder.root)
                .expand()

        Flowable.range(1, 15)
                .doOnNext { Thread.sleep(50) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete { backdrop.view()?.imageview?.let { showImage(it) } }
                .subscribe {
                    val chipView = Chip(this).apply { text = UUID.randomUUID().toString().substring(0, 12) }
                    backdrop.view()?.findViewById<ChipGroup>(R.id.chipGroup)?.addView(chipView)
                }
                .addTo(compositeDisposable)
    }

    private fun showImage(imageview: ImageView) {
        imageview.visibility = View.VISIBLE
        Glide.with(imageview)
                .load("https://sun3-1.userapi.com/c635101/v635101996/39762/_PirP42ztb0.jpg")
                .into(imageview)
    }


    override fun onBackPressed() {
        Timber.i("Backdrop.Activity onBack isExpanded=${backdrop.isExpanded()}")
        if (backdrop.isExpanded()) {
            backdrop.collapse()
        } else super.onBackPressed()
    }

    override fun onPause() {
        super.onPause()
        backdrop.destroy()
    }

    private fun log(msg: String) {
//        Timber.tag("Backdrop").i(msg)
    }
}
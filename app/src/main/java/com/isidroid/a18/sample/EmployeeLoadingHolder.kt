package com.isidroid.a18.sample

import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.isidroid.utilsmodule.adapters.CoreLoadingHolder
import kotlinx.android.synthetic.main.sample_item_employee_loading.view.*

class EmployeeLoadingHolder(v: View) : CoreLoadingHolder(v) {
    override fun bind(position: Int) {
        if (position == 0) showInitial()
        else showLoadmore()
    }

    private fun showLoadmore() {
        itemView.flipper.displayedChild = 1
    }

    private fun showInitial() {
        itemView.flipper.displayedChild = 0

        val container = itemView.flipper.getChildAt(0) as ViewGroup
        val count = container.childCount
        (0 until count).forEach {
            val view = container.getChildAt(it)
            if(view is TextView || view is ImageView) view.setBackgroundColor(Color.GRAY)
        }
    }
}
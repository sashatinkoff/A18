package com.isidroid.a18.sample

import android.view.View
import com.isidroid.a18.R
import com.isidroid.a18.databinding.SampleItemEmployeeBinding
import com.isidroid.utilsmodule.adapters.CoreBindAdapter
import com.isidroid.utilsmodule.adapters.CoreHolder
import com.isidroid.utilsmodule.adapters.CoreLoadingHolder
import timber.log.Timber

class EmployeeAdapter : CoreBindAdapter<Employee, SampleItemEmployeeBinding>() {
    override val loadingResource = R.layout.sample_item_employee_loading
    override val hasInitialLoading = true

    override fun resource(viewType: Int): Int {
        return R.layout.sample_item_employee
    }

    override fun createHolder(binding: SampleItemEmployeeBinding, viewType: Int): CoreHolder {
        return EmployeeHolder(binding)
    }

    override fun createLoadingHolder(view: View): CoreLoadingHolder {
        return EmployeeLoadingHolder(view)
    }
}
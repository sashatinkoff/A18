package com.isidroid.loggermodule

import com.isidroid.loggermodule.Diagnostics.Companion.LOGTAG
import timber.log.Timber


fun Timber.Tree.diagnostics(): Timber.Tree {
    return Timber.tag(LOGTAG)
}

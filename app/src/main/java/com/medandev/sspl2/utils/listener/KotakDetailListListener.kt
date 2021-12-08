package com.medandev.sspl2.utils.listener

import android.view.View
import com.medandev.sspl2.model.KotakDetailChildList

interface KotakDetailListListener {

    fun onItemDeleted(view: View, data: KotakDetailChildList, position: Int)

}

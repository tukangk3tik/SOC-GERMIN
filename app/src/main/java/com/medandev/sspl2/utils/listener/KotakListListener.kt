package com.medandev.sspl2.utils.listener

import android.view.View
import com.medandev.sspl2.model.KotakList

interface KotakListListener {

    fun onItemClick(view: View, kotakData: KotakList)

}

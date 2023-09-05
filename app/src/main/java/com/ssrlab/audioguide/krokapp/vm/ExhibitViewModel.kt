package com.ssrlab.audioguide.krokapp.vm

import androidx.lifecycle.ViewModel
import com.ssrlab.audioguide.krokapp.db.objects.PointObject

class ExhibitViewModel: ViewModel() {

    private var pointObject = PointObject(0, "", "", mapOf(), "")
    fun getPointObject() = pointObject
    fun setPointObject(obj: PointObject) { pointObject = obj }

    private var tabLink = ""
    fun setTabLink(link: String) { tabLink = link }
    fun getTabLink() = tabLink
}
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

    private var currentObject: PointObject? = null
    fun getCurrentObject() = currentObject
    fun setCurrentObject(obj: PointObject?) { currentObject = obj }

    private var buttonValue = false
    fun getButtonValue() = buttonValue
    fun setButtonValue(value: Boolean) { buttonValue = value }
}
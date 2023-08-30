package com.ssrlab.audioguide.krokapp.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.ssrlab.audioguide.krokapp.MainActivity
import kotlinx.coroutines.CoroutineScope

open class BaseFragment: Fragment() {

    lateinit var mainActivity: MainActivity
    lateinit var navController: NavController
    lateinit var scope: CoroutineScope

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainActivity = activity as MainActivity
        scope = mainActivity.getScope()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = findNavController()
    }
}
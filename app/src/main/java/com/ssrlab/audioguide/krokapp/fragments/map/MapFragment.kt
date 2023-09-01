package com.ssrlab.audioguide.krokapp.fragments.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ssrlab.audioguide.krokapp.R
import com.ssrlab.audioguide.krokapp.databinding.FragmentMapBinding
import com.ssrlab.audioguide.krokapp.fragments.BaseFragment

class MapFragment: BaseFragment() {

    private lateinit var binding: FragmentMapBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentMapBinding.inflate(layoutInflater)

        mainActivity.setToolbar(resources.getString(R.string.map)){}

        return binding.root
    }
}
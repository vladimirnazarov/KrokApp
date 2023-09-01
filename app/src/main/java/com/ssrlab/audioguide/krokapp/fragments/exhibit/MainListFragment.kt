package com.ssrlab.audioguide.krokapp.fragments.exhibit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ssrlab.audioguide.krokapp.R
import com.ssrlab.audioguide.krokapp.databinding.FragmentMainListBinding
import com.ssrlab.audioguide.krokapp.fragments.BaseFragment

class MainListFragment: BaseFragment() {

    private lateinit var binding: FragmentMainListBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentMainListBinding.inflate(layoutInflater)

        mainActivity.setToolbar(resources.getString(R.string.app_name)){}

        return binding.root
    }
}
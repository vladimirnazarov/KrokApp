package com.ssrlab.audioguide.krokapp.fragments.exhibit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.ssrlab.audioguide.krokapp.R
import com.ssrlab.audioguide.krokapp.databinding.FragmentExhibitBinding
import com.ssrlab.audioguide.krokapp.db.objects.AdditionalObject
import com.ssrlab.audioguide.krokapp.db.objects.PointObject
import com.ssrlab.audioguide.krokapp.fragments.BaseFragment
import com.ssrlab.audioguide.krokapp.vm.ExhibitViewModel

class ExhibitFragment: BaseFragment() {

    private lateinit var binding: FragmentExhibitBinding

    private lateinit var mainPartOfObject: PointObject
    private lateinit var additionalPartOfObject: AdditionalObject

    private val exhibitVM: ExhibitViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentExhibitBinding.inflate(layoutInflater)



        return binding.root
    }

    override fun onResume() {
        super.onResume()

        mainActivity.setToolbar(resources.getString(R.string.exhibit), isBackButtonShown = true)
    }
}
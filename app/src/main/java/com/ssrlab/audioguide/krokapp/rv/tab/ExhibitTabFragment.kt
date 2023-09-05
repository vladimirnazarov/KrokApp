package com.ssrlab.audioguide.krokapp.rv.tab

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import coil.load
import coil.transform.RoundedCornersTransformation
import com.ssrlab.audioguide.krokapp.databinding.TabExhibitImageBinding
import com.ssrlab.audioguide.krokapp.vm.ExhibitViewModel

class ExhibitTabFragment: Fragment() {

    private lateinit var binding: TabExhibitImageBinding
    private val exhibitVM: ExhibitViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = TabExhibitImageBinding.inflate(layoutInflater)

        binding.tabImage.load(exhibitVM.getTabLink()) {
            crossfade(true)
            transformations(RoundedCornersTransformation(16f))
        }

        return binding.root
    }
}
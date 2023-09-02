package com.ssrlab.audioguide.krokapp.fragments.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import coil.load
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ssrlab.audioguide.krokapp.MainActivity
import com.ssrlab.audioguide.krokapp.R
import com.ssrlab.audioguide.krokapp.databinding.BottomSheetMapBinding
import com.ssrlab.audioguide.krokapp.db.objects.PointObject
import com.ssrlab.audioguide.krokapp.vm.ExhibitViewModel

class MapBottomSheet(
    private val point: PointObject
): BottomSheetDialogFragment() {

    private lateinit var binding: BottomSheetMapBinding
    private lateinit var mainActivity: MainActivity

    private val exhibitVM: ExhibitViewModel by activityViewModels()

    override fun getTheme() = R.style.BottomSheetDialogTheme

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainActivity = activity as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = BottomSheetMapBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        binding.apply {
            bottomSheetImage.load(point.logo)
            bottomSheetTitle.text = point.name

            bottomSheetShowExhibition.setOnClickListener {
                exhibitVM.apply {
                    setPointObject(point)
                    setButtonValue(false)
                }
                findNavController().navigate(R.id.exhibitFragment2)
                dismiss()
            }
        }
    }
}
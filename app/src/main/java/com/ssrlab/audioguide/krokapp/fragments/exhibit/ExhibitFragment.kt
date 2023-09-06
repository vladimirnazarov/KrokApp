package com.ssrlab.audioguide.krokapp.fragments.exhibit

import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.google.android.material.tabs.TabLayoutMediator
import com.ssrlab.audioguide.krokapp.R
import com.ssrlab.audioguide.krokapp.client.AdditionalClient
import com.ssrlab.audioguide.krokapp.databinding.FragmentExhibitBinding
import com.ssrlab.audioguide.krokapp.db.objects.AdditionalObject
import com.ssrlab.audioguide.krokapp.db.objects.PointObject
import com.ssrlab.audioguide.krokapp.fragments.BaseFragment
import com.ssrlab.audioguide.krokapp.rv.tab.ExhibitTabAdapter
import com.ssrlab.audioguide.krokapp.vm.ExhibitViewModel
import com.ssrlab.audioguide.krokapp.vm.PlayerViewModel
import kotlinx.coroutines.launch

class ExhibitFragment: BaseFragment() {

    private lateinit var binding: FragmentExhibitBinding

    private lateinit var mainPartOfObject: PointObject
    private var additionalPartOfObject: AdditionalObject? = null

    private val exhibitVM: ExhibitViewModel by activityViewModels()
    private val playerViewModel: PlayerViewModel by viewModels()

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

        if (exhibitVM.getButtonValue()) {
            binding.exhibitShowOnTheMap.apply {
                visibility = View.VISIBLE
                setOnClickListener {
                    exhibitVM.setCurrentObject(mainPartOfObject)
                    mainActivity.getController().navigate(R.id.map_graph)
                }
            }
        } else {
            binding.exhibitShowOnTheMap.visibility = View.GONE
            mainActivity.controlNavigationUi(false)
        }

        mainPartOfObject = exhibitVM.getPointObject()
        scope.launch {

            additionalPartOfObject = mainActivity.additionalDao().getAdditionalInfo(mainPartOfObject.id)

            if (additionalPartOfObject == null || additionalPartOfObject?.language != mainActivity.getMainApplication().getLocaleString()) {
                AdditionalClient.getAdditionalInfo(mainActivity.getMainApplication().getLocaleString(), mainPartOfObject.id, mainActivity.additionalDao(), scope) {
                    additionalPartOfObject = it

                    loadObject()
                }
            } else {
                loadObject()
            }
        }
    }

    override fun onStop() {
        super.onStop()

        playerViewModel.mpPause()
        if (!exhibitVM.getButtonValue()) mainActivity.controlNavigationUi(true)
    }

    private fun loadObject() {
        val imagesMap = additionalPartOfObject?.images
        val imageSource = arrayListOf<String>()
        for (i in imagesMap?.keys!!) imagesMap[i]?.let { imageSource.add(it) }

        if (additionalPartOfObject != null) {
            mainActivity.runOnUiThread {
                binding.exhibitTitle.text = mainPartOfObject.name

                var description = additionalPartOfObject!!.description
                description = Html.fromHtml(description, Html.FROM_HTML_MODE_LEGACY).toString()
                binding.exhibitDescription.text = description

                val tabAdapter = ExhibitTabAdapter(mainActivity, imageSource, exhibitVM)
                binding.exhibitPager.adapter = tabAdapter
                TabLayoutMediator(binding.exhibitTabs, binding.exhibitPager) { _, _ -> }.attach()

                if (additionalPartOfObject!!.audio != "null") {
                    binding.exhibitPlayerHolder.visibility = View.VISIBLE

                    playerViewModel.initializeMediaPlayer(additionalPartOfObject!!.audio, binding)
                    binding.exhibitPlayerButton.setOnClickListener {
                        playerViewModel.playAudio(binding)
                    }
                }
            }
        }
    }
}
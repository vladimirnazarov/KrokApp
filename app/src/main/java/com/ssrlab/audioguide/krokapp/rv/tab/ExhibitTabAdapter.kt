package com.ssrlab.audioguide.krokapp.rv.tab

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.ssrlab.audioguide.krokapp.vm.ExhibitViewModel

class ExhibitTabAdapter(
    fragment: FragmentActivity,
    private val imageList: ArrayList<String>,
    private val viewModel: ExhibitViewModel
    ) : FragmentStateAdapter(fragment) {

    override fun getItemCount() = imageList.size

    override fun createFragment(position: Int): Fragment {
        viewModel.setTabLink(imageList[position])
        return ExhibitTabFragment()
    }
}
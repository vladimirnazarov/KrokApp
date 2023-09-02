package com.ssrlab.audioguide.krokapp.fragments.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ssrlab.audioguide.krokapp.R
import com.ssrlab.audioguide.krokapp.databinding.FragmentContactsBinding
import com.ssrlab.audioguide.krokapp.fragments.BaseFragment

class ContactsFragment: BaseFragment() {

    private lateinit var binding: FragmentContactsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentContactsBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        mainActivity.setToolbar(resources.getString(R.string.about_us), true)

        binding.apply {
            contactsDeveloperLink.setOnClickListener { mainActivity.intentToWeb() }
            contactsContactLink.setOnClickListener { mainActivity.intentToMail() }
        }
    }
}
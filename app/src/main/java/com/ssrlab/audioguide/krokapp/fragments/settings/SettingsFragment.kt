package com.ssrlab.audioguide.krokapp.fragments.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ssrlab.audioguide.krokapp.R
import com.ssrlab.audioguide.krokapp.databinding.FragmentSettingsBinding
import com.ssrlab.audioguide.krokapp.fragments.BaseFragment

class SettingsFragment: BaseFragment() {

    private lateinit var binding: FragmentSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentSettingsBinding.inflate(layoutInflater)

        setUpBinding()

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        mainActivity.setToolbar(resources.getString(R.string.settings))
    }

    private fun setUpBinding() {
        binding.apply {
            settingsButtonContacts.setOnClickListener { navController.navigate(R.id.contactsFragment) }
            settingsButtonLanguage.setOnClickListener { mainActivity.showLanguageDialog(true) { mainActivity.intentToSplash() } }
        }
    }
}
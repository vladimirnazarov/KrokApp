package com.ssrlab.audioguide.krokapp.fragments.additional

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.navigation.fragment.findNavController
import com.ssrlab.audioguide.krokapp.R
import com.ssrlab.audioguide.krokapp.databinding.FragmentSplashBinding
import com.ssrlab.audioguide.krokapp.db.objects.CityObject
import com.ssrlab.audioguide.krokapp.db.objects.PointObject
import com.ssrlab.audioguide.krokapp.fragments.BaseFragment
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class FragmentSplash: BaseFragment() {

    private lateinit var binding: FragmentSplashBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentSplashBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        controlLaunch()
    }

    private fun animateLogo() {
        val rotationAnim = AnimationUtils.loadAnimation(mainActivity, R.anim.animation_spin)
        binding.splashText.visibility = View.VISIBLE
        binding.splashLogo.apply {
            visibility = View.VISIBLE
            startAnimation(rotationAnim)
        }
    }

    private fun controlLaunch() {
        if (mainActivity.isFirstLaunch()) {
            mainActivity.showLanguageDialog(false) {
                animateLogo()
                checkDatabase()
            }
        }
        else {
            animateLogo()
            checkDatabase()
        }
    }

    private suspend fun navigateForward(delayValue: Long) {
        delay(delayValue)
        mainActivity.runOnUiThread {
            findNavController().navigate(R.id.mainListFragment)
            mainActivity.controlNavigationUi(true)
        }
    }

    private fun checkDatabase() {
        scope.launch {
            mainActivity.apply {
                val citiesList = this.cityDao().getCities()
                val pointsList = this.pointDao().getPoints()
                if (citiesList == listOf<CityObject>() || pointsList == listOf<PointObject>()) {
                    mainActivity.runOnUiThread { this.loadData { scope.launch { navigateForward(0) } } }
                } else if (citiesList[0].language != mainActivity.getMainApplication().getLocaleString() || pointsList[0].language != mainActivity.getMainApplication().getLocaleString()) {
                    mainActivity.runOnUiThread { this.loadData { scope.launch { navigateForward(0) } } }
                } else scope.launch { navigateForward(5000) }
            }
        }
    }
}
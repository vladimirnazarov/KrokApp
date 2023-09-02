package com.ssrlab.audioguide.krokapp

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import com.ssrlab.audioguide.krokapp.app.MainApplication
import com.ssrlab.audioguide.krokapp.client.CityClient
import com.ssrlab.audioguide.krokapp.client.PointClient
import com.ssrlab.audioguide.krokapp.databinding.ActivitySplashBinding
import com.ssrlab.audioguide.krokapp.databinding.DialogLanguageBinding
import com.ssrlab.audioguide.krokapp.db.DatabaseClient
import com.ssrlab.audioguide.krokapp.db.dao.CityDao
import com.ssrlab.audioguide.krokapp.db.dao.PointDao
import com.ssrlab.audioguide.krokapp.db.objects.CityObject
import com.ssrlab.audioguide.krokapp.db.objects.PointObject
import kotlinx.coroutines.*
import java.util.*

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    private lateinit var mainApplication: MainApplication

    private val counter = MutableLiveData<Int>()
    private var firstLaunch = false

    private val job = Job()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    private lateinit var cityDao: CityDao
    private lateinit var pointDao: PointDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainApplication = MainApplication()
        mainApplication.setContext(this@SplashActivity)

        loadPreferences()
        setUpUtils()
    }

    override fun onResume() {
        super.onResume()

        controlLaunch()
    }

    private fun setUpUtils() {

        counter.value = 0

        val database = Room.databaseBuilder(mainApplication.getContext(), DatabaseClient::class.java, mainApplication.constDatabaseName)
            .fallbackToDestructiveMigration()
            .build()
        cityDao = database.cityDao()
        pointDao = database.pointDao()
    }

    @Suppress("DEPRECATION")
    private fun loadPreferences() {
        val sharedPreferences = getSharedPreferences(mainApplication.constPreferences, MODE_PRIVATE)

        val locale = sharedPreferences.getString(mainApplication.constLocale, "en")
        firstLaunch = sharedPreferences.getBoolean(mainApplication.constLaunch, true)

        with (sharedPreferences.edit()) {
            putBoolean(mainApplication.constLaunch, false)
            apply()
        }

        locale?.let { Locale(it) }?.let { mainApplication.setLocale(it) }
        mainApplication.setLocaleString(locale!!)

        val config = mainApplication.getContext().resources.configuration
        config.setLocale(Locale(locale))
        Locale.setDefault(Locale(locale))

        mainApplication.getContext().resources.updateConfiguration(config, resources.displayMetrics)
    }

    private fun saveLocale(locale: String) {
        val sharedPreferences = getSharedPreferences(mainApplication.constPreferences, MODE_PRIVATE)
        with (sharedPreferences.edit()) {
            putString(mainApplication.constLocale, locale)
            apply()
        }

        recreate()
    }

    private fun loadData(onSuccess: () -> Unit) {
        CityClient.getCities(mainApplication.getLocaleString(), cityDao, scope) { runOnUiThread { counter.value = counter.value?.plus(1) } }
        PointClient.getPoints(mainApplication.getLocaleString(), pointDao, scope) { runOnUiThread { counter.value = counter.value?.plus(1) } }

        counter.observe(this@SplashActivity) {
            if (it == 2) onSuccess()
        }
    }

    private fun animateLogo() {
        val rotationAnim = AnimationUtils.loadAnimation(this, R.anim.animation_spin)
        binding.splashText.visibility = View.VISIBLE
        binding.splashLogo.apply {
            visibility = View.VISIBLE
            startAnimation(rotationAnim)
        }
    }

    private fun controlLaunch() {
        if (firstLaunch) showLanguageDialog()
        else {
            animateLogo()
            checkDatabase()
        }
    }

    private suspend fun navigateForward(delayValue: Long) {
        delay(delayValue)
        intentToMain()
    }

    @Suppress("DEPRECATION")
    private fun showLanguageDialog() {
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)

        val width = displayMetrics.widthPixels

        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        val dialogBinding = DialogLanguageBinding.inflate(LayoutInflater.from(this))
        dialog.setContentView(dialogBinding.root)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)

        val layoutParams = WindowManager.LayoutParams()
        layoutParams.copyFrom(dialog.window!!.attributes)
        layoutParams.width = width - (width/5)
        dialog.window?.attributes = layoutParams

        dialogBinding.apply {
            dialogLanguageButtonEn.setOnClickListener {
                saveLocale("en")
                dialog.dismiss()
            }
            dialogLanguageButtonBe.setOnClickListener {
                saveLocale("be")
                dialog.dismiss()
            }
            dialogLanguageButtonRu.setOnClickListener {
                saveLocale("ru")
                dialog.dismiss()
            }
        }

        dialog.show()
    }

    private fun checkDatabase() {
        scope.launch {
            val citiesList = cityDao.getCities()
            val pointsList = pointDao.getPoints()
            if (citiesList == listOf<CityObject>() || pointsList == listOf<PointObject>()) {
                runOnUiThread { loadData { scope.launch { navigateForward(0) } } }
            } else if (citiesList[0].language != mainApplication.getLocaleString() || pointsList[0].language != mainApplication.getLocaleString()) {
                runOnUiThread { loadData { scope.launch { navigateForward(0) } } }
            } else scope.launch { navigateForward(5000) }
        }
    }

    private fun intentToMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
package com.ssrlab.audioguide.krokapp

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import androidx.room.Room
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.ssrlab.audioguide.krokapp.app.MainApplication
import com.ssrlab.audioguide.krokapp.client.CityClient
import com.ssrlab.audioguide.krokapp.client.PointClient
import com.ssrlab.audioguide.krokapp.databinding.ActivityMainBinding
import com.ssrlab.audioguide.krokapp.databinding.DialogLanguageBinding
import com.ssrlab.audioguide.krokapp.db.DatabaseClient
import com.ssrlab.audioguide.krokapp.db.dao.AdditionalDao
import com.ssrlab.audioguide.krokapp.db.dao.CityDao
import com.ssrlab.audioguide.krokapp.db.dao.PointDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainApplication: MainApplication
    private var firstLaunch = false

    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var navController: NavController

    private val job = Job()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    private lateinit var cityDao: CityDao
    private lateinit var pointDao: PointDao
    private lateinit var additionalDao: AdditionalDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainApplication = MainApplication()
        mainApplication.setContext(this@MainActivity)

        setUpUtils()
        loadPreferences()
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

    @Suppress("DEPRECATION")
    private fun initLanguageDialog(isCancelable: Boolean, onEndFun: (() -> Unit)? = null) {
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)

        val width = displayMetrics.widthPixels

        val dialog = Dialog(this@MainActivity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        val dialogBinding = DialogLanguageBinding.inflate(LayoutInflater.from(this@MainActivity))
        dialog.setContentView(dialogBinding.root)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(isCancelable)

        val layoutParams = WindowManager.LayoutParams()
        layoutParams.copyFrom(dialog.window!!.attributes)
        layoutParams.width = width - (width/5)
        dialog.window?.attributes = layoutParams

        dialogBinding.apply {
            dialogLanguageButtonEn.setOnClickListener {
                saveLocale("en")
                dialog.dismiss()

                if (onEndFun != null) onEndFun()
            }
            dialogLanguageButtonBe.setOnClickListener {
                saveLocale("be")
                dialog.dismiss()

                if (onEndFun != null) onEndFun()
            }
            dialogLanguageButtonRu.setOnClickListener {
                saveLocale("ru")
                dialog.dismiss()

                if (onEndFun != null) onEndFun()
            }
        }

        dialog.show()
    }

    private fun setUpUtils() {

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.main_nav_host) as NavHostFragment
        navController = navHostFragment.navController

        bottomNavigationView = binding.mainBottomNav
        bottomNavigationView.inflateMenu(R.menu.main_graph_menu)
        bottomNavigationView.setupWithNavController(navController)

        val database = Room.databaseBuilder(mainApplication.getContext(), DatabaseClient::class.java, mainApplication.constDatabaseName).build()
        cityDao = database.cityDao()
        pointDao = database.pointDao()
        additionalDao = database.additionalDao()
    }

    fun loadData(onSuccess: () -> Unit) {
        val counter = MutableLiveData<Int>()
        CityClient.getCities(mainApplication.getLocaleString()) { counter.value?.plus(1) }
        PointClient.getPoints(mainApplication.getLocaleString(), pointDao, scope) { counter.value?.plus(1) }

        counter.observe(this@MainActivity) {
            if (it == 2) onSuccess()
        }
    }

    fun setToolbar(title: String, isBackButtonShown: Boolean = false, isFavouritesButtonShow: Boolean = false, action: () -> Unit) {
        binding.apply {
            mainToolbarTitle.text = title

            if (isBackButtonShown) mainToolbarBackButton.visibility = View.VISIBLE
            else mainToolbarBackButton.visibility = View.GONE

            if (isFavouritesButtonShow) mainToolbarFavouritesButton.visibility = View.VISIBLE
            else mainToolbarFavouritesButton.visibility = View.GONE

            mainToolbarBackButton.setOnClickListener { action() }
        }
    }

    fun controlNavigationUi(isShowing: Boolean) {
        binding.apply {
            if (isShowing) {
                bottomNavigationView.visibility = View.VISIBLE
                mainToolbar.visibility = View.VISIBLE
            } else {
                bottomNavigationView.visibility = View.GONE
                mainToolbar.visibility = View.GONE
            }
        }
    }

    fun getMainApplication() = mainApplication
    fun isFirstLaunch() = firstLaunch
    fun showLanguageDialog(isCancelable: Boolean, onEndFun: (() -> Unit)? = null) {
        initLanguageDialog(isCancelable, onEndFun)
    }
    fun cityDao() = cityDao
    fun pointDao() = pointDao
    fun additionalDao() = additionalDao
    fun getScope() = scope
}
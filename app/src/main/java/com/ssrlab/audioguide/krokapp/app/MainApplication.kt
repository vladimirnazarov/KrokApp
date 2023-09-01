package com.ssrlab.audioguide.krokapp.app

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import java.util.Locale

class MainApplication: Application() {


    val constDatabaseName = "database"
    val constPreferences = "preferences"
    val constLocale = "locale"
    val constLaunch = "launch"

    private lateinit var context: Context

    fun getContext() = context
    fun setContext(context: Context) { this.context = context }

    private var locale = Locale("en")
    private var localeString = "en"
    private val config = Configuration()

    override fun onCreate() {
        super.onCreate()

        Locale.setDefault(locale)
    }

    fun setLocale(locale: Locale) {
        this.locale = locale
        config.setLocale(locale)
        context.resources.configuration.setLocale(locale)
    }

    fun setLocaleString(locale: String) { this.localeString = locale }
    fun getLocaleString() = localeString
}
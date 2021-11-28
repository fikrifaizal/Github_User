package com.sinau.githubuser.ui.splashscreen

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.sinau.githubuser.ui.setting.SettingPreferences

@SuppressLint("CustomSplashScreen")
class SplashscreenViewModel(private val pref: SettingPreferences) : ViewModel() {

    fun getThemeSetting(): LiveData<Boolean> {
        return pref.getThemeSetting().asLiveData()
    }

}
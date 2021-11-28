package com.sinau.githubuser.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sinau.githubuser.ui.detail.DetailViewModel
import com.sinau.githubuser.ui.favorite.FavoriteViewModel
import com.sinau.githubuser.ui.home.HomeViewModel
import com.sinau.githubuser.ui.setting.SettingPreferences
import com.sinau.githubuser.ui.setting.SettingViewModel
import com.sinau.githubuser.ui.splashscreen.SplashscreenViewModel
import java.lang.IllegalArgumentException

class ViewModelFactory(private val mApplication: Any) : ViewModelProvider.NewInstanceFactory() {
    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        @JvmStatic
        fun getInstance(application: Application): ViewModelFactory {
            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class.java) {
                    INSTANCE = ViewModelFactory(application)
                }
            }
            return INSTANCE as ViewModelFactory
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(DetailViewModel::class.java) -> {
                DetailViewModel(mApplication as Application) as T
            }
            modelClass.isAssignableFrom(FavoriteViewModel::class.java) -> {
                FavoriteViewModel(mApplication as Application) as T
            }
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(mApplication as Application) as T
            }
            modelClass.isAssignableFrom(SettingViewModel::class.java) -> {
                SettingViewModel(mApplication as SettingPreferences) as T
            }
            modelClass.isAssignableFrom(SplashscreenViewModel::class.java) -> {
                SplashscreenViewModel(mApplication as SettingPreferences) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }
}
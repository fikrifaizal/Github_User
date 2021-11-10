package com.sinau.githubuser.ui.insert

import android.app.Application
import androidx.lifecycle.ViewModel
import com.sinau.githubuser.data.repository.FavoriteUserRepository
import com.sinau.githubuser.data.database.FavoriteUser

class FavoriteAddViewModel(application: Application) : ViewModel() {
    private val mFavoriteUserRepository: FavoriteUserRepository = FavoriteUserRepository(application)

    fun insert(favoriteUser: FavoriteUser) {
        mFavoriteUserRepository.insert(favoriteUser)
    }

    fun delete(favoriteUser: FavoriteUser) {
        mFavoriteUserRepository.delete(favoriteUser)
    }

}
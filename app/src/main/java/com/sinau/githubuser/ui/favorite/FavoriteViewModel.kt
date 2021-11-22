package com.sinau.githubuser.ui.favorite

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.sinau.githubuser.data.database.FavoriteUser
import com.sinau.githubuser.data.repository.FavoriteUserRepository

class FavoriteViewModel(application: Application) : ViewModel(){
    private val mFavoriteUserRepository: FavoriteUserRepository = FavoriteUserRepository(application)

    fun getAllNotes(): LiveData<List<FavoriteUser>> = mFavoriteUserRepository.getAllFavoriteUser()
}
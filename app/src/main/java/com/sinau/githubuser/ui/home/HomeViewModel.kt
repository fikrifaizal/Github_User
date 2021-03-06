package com.sinau.githubuser.ui.home

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sinau.githubuser.data.api.ApiConfig
import com.sinau.githubuser.data.database.FavoriteUser
import com.sinau.githubuser.data.repository.FavoriteUserRepository
import com.sinau.githubuser.model.User
import com.sinau.githubuser.model.UserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel(application: Application) : ViewModel() {

    private val _user = MutableLiveData<ArrayList<User>>()
    val user : LiveData<ArrayList<User>> = _user

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> = _isLoading

    private val _isOnline = MutableLiveData<Boolean>()
    val isOnline : LiveData<Boolean> = _isOnline

    companion object {
        private const val DEFAULT_SEARCH = "fikri"
    }

    init {
        getUser(DEFAULT_SEARCH)
    }

    fun getUser(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUser(username)
        client.enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                _isLoading.value = false
                _isOnline.value = true
                if (response.isSuccessful) {
                    _user.value = response.body()?.items
                } else {
                    _isOnline.value = false
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                _isLoading.value = false
                _isOnline.value = false
            }
        })
    }

    private val mFavoriteUserRepository: FavoriteUserRepository = FavoriteUserRepository(application)

    fun getAllFavorites(): LiveData<List<FavoriteUser>> = mFavoriteUserRepository.getAllFavoriteUser()
}
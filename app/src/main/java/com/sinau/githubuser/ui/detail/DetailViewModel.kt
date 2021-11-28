package com.sinau.githubuser.ui.detail

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sinau.githubuser.data.api.ApiConfig
import com.sinau.githubuser.data.database.FavoriteUser
import com.sinau.githubuser.data.repository.FavoriteUserRepository
import com.sinau.githubuser.model.DetailUserResponse
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel(application: Application) : ViewModel() {

    private val _userDetail = MutableLiveData<DetailUserResponse>()

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> = _isLoading

    private val _isOnline = MutableLiveData<Boolean>()
    val isOnline : LiveData<Boolean> = _isOnline

    private val mFavoriteUserRepository: FavoriteUserRepository = FavoriteUserRepository(application)

    fun isFavoriteUser(id: Int): Boolean = mFavoriteUserRepository.isFavoriteUser(id) >= 1

    fun getDetailUser(username: String) : LiveData<DetailUserResponse> {
        val client = ApiConfig.getApiService().getDetail(username)
        client.enqueue(object : Callback<DetailUserResponse> {
            override fun onResponse(call: Call<DetailUserResponse>, response: Response<DetailUserResponse>) {
                _isLoading.value = false
                _isOnline.value = true
                if (response.isSuccessful) {
                    _userDetail.value = response.body()
                }
            }

            override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                _isLoading.value = false
                _isOnline.value = false
            }

        })
        return _userDetail
    }

    fun insert(user: DetailUserResponse) {
        viewModelScope.launch {
            val login = user.login.trim()
            val avatarUrl = user.avatarUrl.trim()
            val id = user.id
            val type = user.type.trim()

            val favoriteUser = FavoriteUser(login, avatarUrl, id, type)
            mFavoriteUserRepository.insert(favoriteUser)
        }
    }

    fun delete(id: Int) {
        mFavoriteUserRepository.delete(id)
    }
}
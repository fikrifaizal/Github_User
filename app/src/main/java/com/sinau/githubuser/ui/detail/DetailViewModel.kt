package com.sinau.githubuser.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sinau.githubuser.data.api.ApiConfig
import com.sinau.githubuser.model.DetailUserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel : ViewModel() {

    private val _userDetail = MutableLiveData<DetailUserResponse>()

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> = _isLoading

    private val _isOnline = MutableLiveData<Boolean>()
    val isOnline : LiveData<Boolean> = _isOnline

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
}
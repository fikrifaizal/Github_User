package com.sinau.githubuser.data.api

import com.sinau.githubuser.BuildConfig
import com.sinau.githubuser.model.DetailUserResponse
import com.sinau.githubuser.model.User
import com.sinau.githubuser.model.UserResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("search/users")
    @Headers("Authorization: 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")
    fun getUser(
            @Query("q") query: String
    ) : Call<UserResponse>

    @GET("users/{username}")
    @Headers("Authorization: token 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")
    fun getDetail(
            @Path("username") username: String
    ) : Call<DetailUserResponse>

    @GET("users/{username}/followers")
    @Headers("Authorization: token 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")
    fun getFollower(
            @Path("username") username: String
    ) : Call<ArrayList<User>>

    @GET("users/{username}/following")
    @Headers("Authorization: token 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")
    fun getFollowing(
            @Path("username") username: String
    ) : Call<ArrayList<User>>
}
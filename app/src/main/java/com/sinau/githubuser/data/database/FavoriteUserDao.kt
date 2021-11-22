package com.sinau.githubuser.data.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface FavoriteUserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(favoriteUser: FavoriteUser)

    @Query("DELETE FROM favoriteuser WHERE favoriteuser.id = :id")
    fun delete(id: Int)

    @Query("SELECT * FROM favoriteuser ORDER BY id ASC")
    fun getAllFavoriteUser(): LiveData<List<FavoriteUser>>

    @Query("SELECT COUNT(*) FROM favoriteuser WHERE favoriteuser.id = :id")
    fun isFavoriteUser(id: Int): Int
}
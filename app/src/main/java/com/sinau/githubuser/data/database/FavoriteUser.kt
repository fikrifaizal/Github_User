package com.sinau.githubuser.data.database

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class FavoriteUser (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "nomor")
    var nomor: Int = 0,

    @ColumnInfo(name = "login")
    var login: String? = null,

    @ColumnInfo(name = "avatarUrl")
    var avatarUrl: String? = null,

    @ColumnInfo(name = "id")
    var id: Int? = null,

    @ColumnInfo(name = "type")
    var type: String? = null,

    @ColumnInfo(name = "isFavorite")
    var isFavorite: Boolean? = null
) : Parcelable
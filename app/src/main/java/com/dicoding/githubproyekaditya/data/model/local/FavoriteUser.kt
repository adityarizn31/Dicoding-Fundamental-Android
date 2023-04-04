package com.dicoding.githubproyekaditya.data.model.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "favorite_user")
data class FavoriteUser(
    val login: String,

    //Dijadikan Primary Key
    @PrimaryKey
    val id: Int,

    //Digunakan untuk menampilkan foto
    val avatar_url: String
) : Serializable
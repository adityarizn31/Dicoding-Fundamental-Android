package com.dicoding.githubproyekaditya.fav

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.dicoding.githubproyekaditya.data.model.local.DatabaseUser
import com.dicoding.githubproyekaditya.data.model.local.FavoriteUser
import com.dicoding.githubproyekaditya.data.model.local.FavoriteUserDao

class FavoriteViewModel(application: Application) : AndroidViewModel(application){

    private var userDao: FavoriteUserDao?
    private var userDb: DatabaseUser?

    init {
        userDb = DatabaseUser.getDatabase(application)
        userDao = userDb?.favoriteUserDao()
    }

    fun getFavoriteUser(): LiveData<List<FavoriteUser>>? {
        return userDao?.getFavoriteUser()
    }
}
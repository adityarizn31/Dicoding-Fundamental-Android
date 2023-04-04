package com.dicoding.githubproyekaditya.detail

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.githubproyekaditya.api.RetrofitClient
import com.dicoding.githubproyekaditya.data.model.DetailUserReponse
import com.dicoding.githubproyekaditya.data.model.local.DatabaseUser
import com.dicoding.githubproyekaditya.data.model.local.FavoriteUser
import com.dicoding.githubproyekaditya.data.model.local.FavoriteUserDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailUserViewModel(application: Application) : AndroidViewModel(application) {
    val user = MutableLiveData<DetailUserReponse>()

    private var userDao: FavoriteUserDao?
    private var userDb: DatabaseUser?

    init {
        userDb = DatabaseUser.getDatabase(application)
        userDao = userDb?.favoriteUserDao()
    }

    fun setUserDetail(username: String) {
        RetrofitClient.apiInstance
            .getUserDetail(username)
            .enqueue(object : Callback<DetailUserReponse> {
                override fun onResponse(
                    call: Call<DetailUserReponse>,
                    response: Response<DetailUserReponse>
                ) {
                    if (response.isSuccessful) {
                        user.postValue(response.body())
                    }
                }

                override fun onFailure(call: Call<DetailUserReponse>, t: Throwable) {
                    t.message?.let { Log.d("Failure", it) }
                }

            })
    }

    fun getUserDetail(): LiveData<DetailUserReponse> {
        return user
    }

    fun addToFavorite(username: String, id: Int, avatarUrl: String){
        CoroutineScope(Dispatchers.IO).launch {
            var user = FavoriteUser(
                username,
                id,
                avatarUrl
            )
            userDao?.addToFavorite(user)
        }
    }

    suspend fun checkUser(id: Int) = userDao?.checkUser(id)

    fun deleteUserFavorite(id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            userDao?.deleteUserFavorite(id)
        }
    }


}
package com.dicoding.githubproyekaditya.data.model

data class DetailUserReponse(
    val login: String,
    val id: Int,
    val avatar_url: String,
    val followers_url: String,
    val _url: String,
    val name: String,
    val following: Int,
    val followers: Int
)

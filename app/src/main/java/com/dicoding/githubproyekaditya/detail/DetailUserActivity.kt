package com.dicoding.githubproyekaditya.detail

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.dicoding.githubproyekaditya.DarkMode
import com.dicoding.githubproyekaditya.R
import com.dicoding.githubproyekaditya.databinding.ActivityDetailUserBinding
import com.dicoding.githubproyekaditya.fav.FavoriteActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailUserActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_USERNAME = "extra_username"
        const val EXTRA_ID = "extra_id"
        const val EXTRA_URL = "extra_url"
    }

    private lateinit var binding: ActivityDetailUserBinding
    private lateinit var viewModel: DetailUserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = intent.getStringExtra(EXTRA_USERNAME)
        val id = intent.getIntExtra(EXTRA_ID, 0)
        val avatar_Url = intent.getStringExtra(EXTRA_URL)

        val bundle = Bundle()
        bundle.putString(EXTRA_USERNAME, username)

        viewModel = ViewModelProvider(this).get(DetailUserViewModel::class.java)

        username?.let { viewModel.setUserDetail(it) }
        showLoading(true)
        viewModel.getUserDetail().observe(this, {
            if (it != null) {
                showLoading(false)
                binding.apply {
                    tvName.text = it.name
                    tvUsername.text = it.login
                    tvFollowers.text = "${it.followers} Followers"
                    tvFollowing.text = "${it.following} Following"
                    showLoading(false)
                    Glide.with(this@DetailUserActivity).load(it.avatar_url)
                        .transition(DrawableTransitionOptions.withCrossFade()).centerCrop()
                        .into(ivProfile)
                }
            }
        })

        //Penambahan AddToFavorite
        var _isChecked = false
        CoroutineScope(Dispatchers.IO).launch {
            val count = viewModel.checkUser(id)
            withContext(Dispatchers.Main) {
                if (count != null) {
                    if (count > 0) {
                        binding.btnToogle.isChecked = true
                        _isChecked = true
                    } else {
                        binding.btnToogle.isChecked = false
                        _isChecked = false
                    }
                }
            }
        }

        // Digunakan untuk Favorite dan UnFavorite
        binding.btnToogle.setOnClickListener {
            _isChecked = !_isChecked
            if (_isChecked) {
                if (username != null) {
                    avatar_Url?.let { it1 -> viewModel.addToFavorite(username, id, it1) }
                }
            } else {
                viewModel.deleteUserFavorite(id)
            }
        }

        val sectionPagerAdapter = SectionPagerAdapter(this, supportFragmentManager, bundle)
        binding.apply {
            viewPager.adapter = sectionPagerAdapter
            tabs.setupWithViewPager(viewPager)
        }
    }

    private fun showLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE
    }

}


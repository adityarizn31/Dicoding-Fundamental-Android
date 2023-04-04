package com.dicoding.githubproyekaditya.fav

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubproyekaditya.UserAdapter
import com.dicoding.githubproyekaditya.data.model.User
import com.dicoding.githubproyekaditya.data.model.local.FavoriteUser
import com.dicoding.githubproyekaditya.databinding.ActivityFavoriteBinding
import com.dicoding.githubproyekaditya.detail.DetailUserActivity

class FavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var adapter: UserAdapter
    private lateinit var viewModel: FavoriteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = UserAdapter()
        adapter.notifyDataSetChanged()

        viewModel = ViewModelProvider(this).get(FavoriteViewModel::class.java)

        adapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClick(data: User) {
                Intent(this@FavoriteActivity, DetailUserActivity::class.java).also {
                    it.putExtra(DetailUserActivity.EXTRA_USERNAME, data.login)
                    it.putExtra(DetailUserActivity.EXTRA_ID, data.id)
                    it.putExtra(DetailUserActivity.EXTRA_URL, data.avatar_url)
                    startActivity(it)
                }
            }

        })

        //Mengeset RecyclerView
        binding.apply {
            rvUserr.setHasFixedSize(true)
            rvUserr.layoutManager = LinearLayoutManager(this@FavoriteActivity)
            rvUserr.adapter = adapter
        }

        viewModel.getFavoriteUser()?.observe(this, {
            if (it != null){
                val list = mapList(it)
                adapter.setList(list)
            }

        })


    }

    private fun mapList(users: List<FavoriteUser>): ArrayList<User> {
        val listUser = ArrayList<User>()
        for (user in users){
            val userMap = User(
                user.login,
                user.id,
                user.avatar_url
            )
            listUser.add(userMap)
        }
        return listUser
    }
}
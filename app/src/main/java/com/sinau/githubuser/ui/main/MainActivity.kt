package com.sinau.githubuser.ui.main

import android.annotation.SuppressLint
import android.app.SearchManager
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.sinau.githubuser.*
import com.sinau.githubuser.databinding.ActivityMainBinding
import com.sinau.githubuser.model.User
import com.sinau.githubuser.ui.adapter.UserAdapter
import com.sinau.githubuser.ui.setting.SettingActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val mainViewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainViewModel.isLoading.observe(this, { loading ->
            showLoading(loading)
        })
        mainViewModel.isOnline.observe(this, { status ->
            showStatus(status)
        })
        mainViewModel.user.observe(this, {
            if (it.size == 0) {
                showStatus(false)
                binding.textStatus.text = getString(R.string.status_nothing)
            } else {
                showRecycleView(it)
            }
        })
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showRecycleView(list: ArrayList<User>) {
        val listUser : ArrayList<User> = arrayListOf()
        for (id in list) {
            val user = User(id.login, id.avatarUrl, id.id, id.type)
            listUser.add(user)
        }

        binding.rvUser.layoutManager = LinearLayoutManager(this)

        val userAdapter = UserAdapter(listUser)
        userAdapter.notifyDataSetChanged()

        binding.rvUser.adapter = userAdapter
        binding.rvUser.setHasFixedSize(true)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showStatus(isOnline: Boolean) {
        if (isOnline) {
            binding.rvUser.visibility = View.VISIBLE
            binding.status.visibility = View.GONE
        } else {
            binding.rvUser.visibility = View.GONE
            binding.status.visibility = View.VISIBLE
            binding.textStatus.text = getString(R.string.status_offline)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)

        val searchManager = getSystemService(SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                mainViewModel.getUser(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.setting -> {
                val moveActivity = Intent(this, SettingActivity::class.java)
                startActivity(moveActivity)
                true
            }
            else -> true
        }
    }
}
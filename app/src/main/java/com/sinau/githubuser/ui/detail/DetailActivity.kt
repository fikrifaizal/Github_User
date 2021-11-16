package com.sinau.githubuser.ui.detail

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.sinau.githubuser.R
import com.sinau.githubuser.ui.adapter.SectionsPagerAdapter
import com.sinau.githubuser.databinding.ActivityDetailBinding
import com.sinau.githubuser.model.DetailUserResponse
import com.sinau.githubuser.model.User
import com.sinau.githubuser.ui.ViewModelFactory
import java.util.*

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var detailViewModel: DetailViewModel

    private var nameUser : String = ""
    private var locUser : String = ""
    private var compUser : String = ""
    private var isFavorite : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        detailViewModel = obtainViewModel(this@DetailActivity)

        val user = intent.getParcelableExtra<User>(EXTRA_USERNAME) as User

        detailViewModel.getDetailUser(user.login).observe(this, {
            showDetailUser(it)
        })
        detailViewModel.isOnline.observe(this, { status ->
            showStatus(status)
        })
        detailViewModel.isLoading.observe(this, { loading ->
            showLoading(loading)
        })

        setViewPager(user.login)
        val actionBar = supportActionBar
        actionBar?.title = user.login
        actionBar?.setHomeAsUpIndicator(R.drawable.ic_back_button)
    }

    private fun obtainViewModel(activity: AppCompatActivity): DetailViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[DetailViewModel::class.java]
    }

    private fun showDetailUser(user: DetailUserResponse) {
        nameUser = if (user.name == "null") "-" else user.name
        locUser = if (user.location == "null") "-" else user.location
        compUser = if (user.company == "null") "-" else user.company

        binding.apply {
            userName.text = nameUser
            userRepository.text = user.repository.toString()
            userFollowing.text = user.following.toString()
            userFollowers.text = user.followers.toString()
            userLocation.text = locUser
            userCompany.text = compUser
        }

        Glide.with(this@DetailActivity)
            .load(user.avatarUrl)
            .into(binding.userPhoto)
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
            binding.detailLayout.visibility = View.GONE
        } else {
            binding.progressBar.visibility = View.GONE
            binding.detailLayout.visibility = View.VISIBLE
        }
    }

    private fun showStatus(isOnline: Boolean) {
        if (isOnline) {
            binding.detailLayout.visibility = View.VISIBLE
            binding.status.visibility = View.GONE
        } else {
            binding.detailLayout.visibility = View.GONE
            binding.status.visibility = View.VISIBLE
            binding.textStatus.text = getString(R.string.status_offline)
        }
    }

    private fun setViewPager(username: String) {
        val bundle = Bundle()
        bundle.putString(EXTRA_DATA, username)
        val sectionsPagerAdapter = SectionsPagerAdapter(this, bundle)
        val viewPager : ViewPager2 = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter

        val tabs : TabLayout = binding.tabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_FILES[position])
        }.attach()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_detail, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.favorite -> {
                if (isFavorite) {
                    isFavorite = false
                    item.setIcon(R.drawable.ic_favorite_white)
                } else {
                    isFavorite = true
                    item.setIcon(R.drawable.ic_favorite_red)
                }
                true
            }
            R.id.share -> {
                val shareText = "User *$nameUser* tinggal di $locUser dan bekerja di $compUser"

                val shareActivity = Intent(Intent.ACTION_SEND)
                shareActivity.putExtra(Intent.EXTRA_TEXT, shareText)
                shareActivity.type = "text/plain"
                startActivity(Intent.createChooser(shareActivity, "Bagikan dengan"))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    companion object {
        const val EXTRA_USERNAME = "extra_username"
        const val EXTRA_DATA = "extra_data"

        @StringRes
        private val TAB_FILES = intArrayOf(
            R.string.followers,
            R.string.following
        )
    }
}
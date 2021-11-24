package com.sinau.githubuser.ui.home

import android.annotation.SuppressLint
import android.app.SearchManager
import android.content.Context.SEARCH_SERVICE
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.sinau.githubuser.R
import com.sinau.githubuser.databinding.FragmentHomeBinding
import com.sinau.githubuser.model.User
import com.sinau.githubuser.ui.ViewModelFactory
import com.sinau.githubuser.ui.adapter.UserAdapter
import com.sinau.githubuser.ui.setting.SettingActivity

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding as FragmentHomeBinding
    private lateinit var homeViewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        homeViewModel = obtainViewModel(activity as AppCompatActivity)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeViewModel.isLoading.observe(viewLifecycleOwner, { loading ->
            showLoading(loading)
        })
        homeViewModel.isOnline.observe(viewLifecycleOwner, { status ->
            showStatus(status)
        })
        homeViewModel.user.observe(viewLifecycleOwner, {
            if (it.size == 0) {
                showStatus(false)
                binding.textStatus.text = getString(R.string.status_nothing)
            } else {
                showRecycleView(it)
            }
        })
        homeViewModel.getAllFavorites().observe(viewLifecycleOwner, {

        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun obtainViewModel(activity: AppCompatActivity): HomeViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[HomeViewModel::class.java]
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showRecycleView(list: ArrayList<User>) {
        binding.rvUser.layoutManager = LinearLayoutManager(activity)

        val userAdapter = UserAdapter(list)
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)

        val searchManager = activity?.getSystemService(SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(activity?.componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                homeViewModel.getUser(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == R.id.setting) {
            val moveActivity = Intent(activity, SettingActivity::class.java)
            startActivity(moveActivity)
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }
}
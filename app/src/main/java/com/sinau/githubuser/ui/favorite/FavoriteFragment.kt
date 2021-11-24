package com.sinau.githubuser.ui.favorite

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.sinau.githubuser.R
import com.sinau.githubuser.data.database.FavoriteUser
import com.sinau.githubuser.databinding.FragmentFavoriteBinding
import com.sinau.githubuser.model.User
import com.sinau.githubuser.ui.ViewModelFactory
import com.sinau.githubuser.ui.adapter.UserAdapter

class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding as FragmentFavoriteBinding
    private lateinit var favoriteViewModel: FavoriteViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        favoriteViewModel = obtainViewModel(activity as AppCompatActivity)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        favoriteViewModel.getAllNotes().observe(viewLifecycleOwner, {
            showRecycleView(it)
            if (it.isEmpty()) {
                showStatus(true)
            } else {
                showStatus(false)
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun obtainViewModel(activity: AppCompatActivity): FavoriteViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[FavoriteViewModel::class.java]
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showRecycleView(list: List<FavoriteUser>) {
        val listUser : ArrayList<User> = arrayListOf()
        for (id in list) {
            val user = User(id.login, id.avatarUrl, id.id, id.type)
            listUser.add(user)
        }

        val userAdapter = UserAdapter(listUser)
        userAdapter.notifyDataSetChanged()

        binding.rvFavorite.layoutManager = LinearLayoutManager(activity)
        binding.rvFavorite.adapter = userAdapter
        binding.rvFavorite.setHasFixedSize(true)
    }

    private fun showStatus(isEmpty: Boolean) {
        if (isEmpty) {
            binding.status.visibility = View.VISIBLE
            binding.textStatus.text = getString(R.string.favorite_nothing)
        } else {
            binding.status.visibility = View.GONE
        }
    }
}
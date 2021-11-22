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
            if (it != null) {
                showRecycleView(it)
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
            val user = id.login?.let { id.avatarUrl?.let { it1 -> id.id?.let { it2 ->
                id.type?.let { it3 ->
                    User(it, it1,
                        it2, it3
                    )
                }
            } } }
            if (user != null) {
                listUser.add(user)
            }
        }

        val userAdapter = UserAdapter(listUser)
        userAdapter.notifyDataSetChanged()

        binding.rvFavorite.layoutManager = LinearLayoutManager(activity)
        binding.rvFavorite.adapter = userAdapter
        binding.rvFavorite.setHasFixedSize(true)
    }
}
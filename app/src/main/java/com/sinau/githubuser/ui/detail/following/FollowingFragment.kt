package com.sinau.githubuser.ui.detail.following

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.sinau.githubuser.R
import com.sinau.githubuser.databinding.FragmentFollowingBinding
import com.sinau.githubuser.model.User
import com.sinau.githubuser.ui.adapter.UserAdapter
import com.sinau.githubuser.ui.detail.DetailActivity

class FollowingFragment : Fragment() {

    private var _binding: FragmentFollowingBinding? = null
    private val binding get() = _binding as FragmentFollowingBinding
    private val followingViewModel by viewModels<FollowingViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        _binding = FragmentFollowingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val dataFromBundle = arguments?.getString(DetailActivity.EXTRA_DATA).toString()
        followingViewModel.getFollowingUser(dataFromBundle)
        followingViewModel.userFollowing.observe(viewLifecycleOwner, {
            if (it.size == 0) {
                isEmpty(true)
                binding.textIsempty.text = getString(R.string.empty_following)
            } else {
                isEmpty(false)
                showFollowingRecyclerView(it)
            }
        })
        followingViewModel.isLoading.observe(viewLifecycleOwner, { loading ->
            showLoading(loading)
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showFollowingRecyclerView(list: ArrayList<User>?) {
        val listUser : ArrayList<User> = arrayListOf()
        if (list != null) {
            for (id in list) {
                val user = User(id.login, id.avatarUrl, id.id, id.type)
                listUser.add(user)
            }
        }

        binding.rvFollowing.layoutManager = LinearLayoutManager(activity)

        val userAdapter = UserAdapter(listUser)
        userAdapter.notifyDataSetChanged()

        binding.rvFollowing.adapter = userAdapter
        binding.rvFollowing.setHasFixedSize(true)
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
            binding.rvFollowing.visibility = View.GONE
        } else {
            binding.progressBar.visibility = View.GONE
            binding.rvFollowing.visibility = View.VISIBLE
        }
    }

    private fun isEmpty(empty: Boolean) {
        if (empty) {
            binding.textIsempty.visibility = View.VISIBLE
            binding.rvFollowing.visibility = View.GONE
        } else {
            binding.textIsempty.visibility = View.GONE
            binding.rvFollowing.visibility = View.VISIBLE
        }
    }
}
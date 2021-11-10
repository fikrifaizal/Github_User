package com.sinau.githubuser.ui.detail.followers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.sinau.githubuser.R
import com.sinau.githubuser.databinding.FragmentFollowersBinding
import com.sinau.githubuser.model.User
import com.sinau.githubuser.ui.adapter.UserAdapter
import com.sinau.githubuser.ui.detail.DetailActivity

class FollowersFragment : Fragment() {

    private var _binding: FragmentFollowersBinding? = null
    private val binding get() = _binding as FragmentFollowersBinding
    private val followersViewModel by viewModels<FollowersViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        _binding = FragmentFollowersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val dataFromBundle = arguments?.getString(DetailActivity.EXTRA_DATA).toString()
        followersViewModel.getFollowersUser(dataFromBundle)
        followersViewModel.userFollowers.observe(viewLifecycleOwner, {
            if (it.size == 0) {
                isEmpty(true)
                binding.textIsempty.text = getString(R.string.empty_followers)
            } else {
                isEmpty(false)
                showFollowersRecyclerView(it)
            }
        })
        followersViewModel.isLoading.observe(viewLifecycleOwner, { loading ->
            showLoading(loading)
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showFollowersRecyclerView(list: ArrayList<User>?) {
        val listUser : ArrayList<User> = arrayListOf()
        if (list != null) {
            for (id in list) {
                val user = User(id.login, id.avatarUrl, id.id, id.type)
                listUser.add(user)
            }
        }

        binding.rvFollowers.layoutManager = LinearLayoutManager(activity)

        val userAdapter = UserAdapter(listUser)
        userAdapter.notifyDataSetChanged()

        binding.rvFollowers.adapter = userAdapter
        binding.rvFollowers.setHasFixedSize(true)
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
            binding.rvFollowers.visibility = View.GONE
        } else {
            binding.progressBar.visibility = View.GONE
            binding.rvFollowers.visibility = View.VISIBLE
        }
    }

    private fun isEmpty(isLoading: Boolean) {
        if (isLoading) {
            binding.textIsempty.visibility = View.VISIBLE
            binding.rvFollowers.visibility = View.GONE
        } else {
            binding.textIsempty.visibility = View.GONE
            binding.rvFollowers.visibility = View.VISIBLE
        }
    }
}
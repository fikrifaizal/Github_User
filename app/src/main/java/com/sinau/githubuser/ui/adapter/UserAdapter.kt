package com.sinau.githubuser.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.sinau.githubuser.R
import com.sinau.githubuser.databinding.ItemRowUserBinding
import com.sinau.githubuser.model.User
import com.sinau.githubuser.ui.detail.DetailActivity

class UserAdapter(private val listUser: ArrayList<User>) : RecyclerView.Adapter<UserAdapter.ListViewHolder>() {

    inner class ListViewHolder(private val binding: ItemRowUserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(listUser: User) {
            with(binding) {
                Glide.with(itemView.context)
                    .load(listUser.avatarUrl)
                    .placeholder(R.drawable.iv_placeholder)
                    .error(R.drawable.iv_placeholder)
                    .apply(RequestOptions().override(55, 55))
                    .into(ivItemPhoto)

                tvItemName.text = listUser.login
                tvItemLocation.text = listUser.type

                itemRv.setOnClickListener{
                    val moveActivity = Intent(it.context, DetailActivity::class.java)
                    moveActivity.putExtra(DetailActivity.EXTRA_USERNAME, listUser)

                    it.context.startActivity(moveActivity)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemRowUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listUser[position])
    }

    override fun getItemCount(): Int = listUser.size
}
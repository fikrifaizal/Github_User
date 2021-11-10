package com.sinau.githubuser.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.sinau.githubuser.R
import com.sinau.githubuser.databinding.ItemRowUserBinding
import com.sinau.githubuser.model.User
import com.sinau.githubuser.ui.detail.DetailActivity

class UserAdapter(private val listUser: ArrayList<User>) : RecyclerView.Adapter<UserAdapter.ListViewHolder>() {

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var ivPhoto: ImageView = itemView.findViewById(R.id.iv_item_photo)
        var tvName: TextView = itemView.findViewById(R.id.tv_item_name)
        var tvLocation: TextView = itemView.findViewById(R.id.tv_item_location)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_row_user, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val data = listUser[position]

        Glide.with(holder.itemView.context)
            .load(data.avatarUrl)
            .apply(RequestOptions().override(55, 55))
            .into(holder.ivPhoto)

        holder.tvName.text = data.login
        holder.tvLocation.text = data.type

        holder.itemView.setOnClickListener{
            val moveActivity = Intent(holder.itemView.context, DetailActivity::class.java)
            moveActivity.putExtra(DetailActivity.EXTRA_USERNAME, listUser[position])

            holder.itemView.context.startActivity(moveActivity)
        }
    }

    override fun getItemCount(): Int = listUser.size
}
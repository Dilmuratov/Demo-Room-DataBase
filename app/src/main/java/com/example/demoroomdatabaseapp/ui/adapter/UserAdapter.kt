package com.example.demoroomdatabaseapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.demoroomdatabaseapp.data.models.User
import com.example.demoroomdatabaseapp.databinding.ItemUserBinding

class UserAdapter : ListAdapter<User, UserAdapter.ViewHolder>(myDiffUtil) {

    private var onClickUserListener: ((User) -> Unit)? = null

    fun setOnUserClickListener(block: (User) -> Unit) {
        onClickUserListener = block
    }

    inner class ViewHolder(private val binding: ItemUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            val user = getItem(position)
            binding.tvName.text = user.name
            binding.tvSurname.text = user.surname
            binding.ivProfile.setImageResource(user.profile)

            binding.root.setOnClickListener {
                onClickUserListener?.invoke(user)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    object myDiffUtil : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User) = oldItem == newItem

        override fun areContentsTheSame(oldItem: User, newItem: User) = oldItem.id == newItem.id
                && oldItem.name == newItem.name
                && oldItem.surname == newItem.surname
                && oldItem.profile == newItem.profile
    }

    fun getItemById(position: Int) = getItem(position)

    fun removeItem(item: User) {
        val currentList = currentList.toMutableList()
        currentList.remove(item)
        submitList(currentList)
    }

    fun addItem(item: User) {
        val currentList = currentList.toMutableList()
        currentList.add(item)
        submitList(currentList)
    }
}

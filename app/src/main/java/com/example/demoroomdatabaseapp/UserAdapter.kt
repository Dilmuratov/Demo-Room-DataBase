package com.example.demoroomdatabaseapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.demoroomdatabaseapp.databinding.ItemUserBinding

class UserAdapter : RecyclerView.Adapter<UserAdapter.ViewHolder>() {
    var models = mutableListOf<User>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    private var onClickUserListener: ((User) -> Unit)? = null

    fun setOnUserClickListener(block: (User) -> Unit) {
        onClickUserListener = block
    }

    inner class ViewHolder(private val binding: ItemUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            val user = models[adapterPosition]
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
        holder.bind()
    }

    override fun getItemCount() = models.size
}

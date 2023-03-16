package com.example.demoroomdatabaseapp

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.demoroomdatabaseapp.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var dao: UserDao
    var adapter = UserAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerView.adapter = adapter

        dao = UserDataBase.getInstance(this).getUsersDao()

        btnAdd()

        search()

        editStudent()

        val itemTouchHelperCallBack = object :
        ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ) = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int){
                val position = viewHolder.adapterPosition
                val user: User = adapter.models[position]

                Toast.makeText(this@MainActivity, "Deleted", Toast.LENGTH_SHORT).show()

                lifecycleScope.launchWhenResumed {
                    dao.deleteUser(user)
                }

                adapter.models.removeAt(position)
                adapter.notifyItemRemoved(position)

                Snackbar.make(
                    viewHolder.itemView,
                    "Deleted",
                    Snackbar.LENGTH_SHORT
                ).apply {
                    setAction("Undo") {
                        lifecycleScope.launchWhenResumed {
                            dao.addUser(user)
                        }
                        adapter.models.add(position, user)
                        adapter.notifyItemInserted(position)

                        //binding.recyclerView.adapter = adapter

                        binding.recyclerView.scrollToPosition(position)
                    }
                    setActionTextColor(Color.YELLOW)
                }.show()
            }
        }

        ItemTouchHelper(itemTouchHelperCallBack).apply {
            attachToRecyclerView(binding.recyclerView)
        }
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launchWhenResumed {
            adapter.models = dao.getListOfUsers().toMutableList()
        }
    }

    /**
     * Add knopkasinin funksiyasi
     */
    private fun btnAdd() {
        binding.ivAdd.setOnClickListener {
            val intent = Intent(this@MainActivity, AddUserActivity::class.java)
            startActivity(intent)
        }
    }

    private fun search() {
        binding.etSearch.addTextChangedListener {
            val str = binding.etSearch.text.toString()
            lifecycleScope.launchWhenResumed {
                if (str.isNotEmpty()) {

                    val list = dao.searchUser(str)
                    adapter.models = list.toMutableList()

                } else adapter.models = dao.getListOfUsers().toMutableList()
            }
        }
    }

    private fun editStudent() {
        adapter.setOnUserClickListener {user ->
            val intent = Intent(this@MainActivity, AddUserActivity::class.java)
            intent.putExtra("id", user.id)
            intent.putExtra("isEdit", true)
            intent.putExtra("name", user.name)
            intent.putExtra("surname", user.surname)
            intent.putExtra("profile", user.profile)
            startActivity(intent)
        }
    }

}
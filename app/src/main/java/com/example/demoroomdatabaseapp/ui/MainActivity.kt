package com.example.demoroomdatabaseapp.ui

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.demoroomdatabaseapp.data.models.User
import com.example.demoroomdatabaseapp.databinding.ActivityMainBinding
import com.example.demoroomdatabaseapp.presentation.MainViewModel
import com.example.demoroomdatabaseapp.ui.adapter.UserAdapter
import com.google.android.material.snackbar.Snackbar
import java.util.*

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    var adapter = UserAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val pref = getSharedPreferences("pref", Context.MODE_PRIVATE)
        setAppLocale(pref.getString("language", "en"), this@MainActivity)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        binding.recyclerView.adapter = adapter

        initObserves()

        lifecycleScope.launchWhenResumed {
            viewModel.getAllUsers()
        }

        btnAdd()

        search()

        editStudent()

        changeLanguage()

        deleteUser()

    }

    fun initObserves() {
        viewModel.getAllUserLiveData.observe(this) {
            adapter.submitList(it)
        }
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launchWhenResumed {
            viewModel.getAllUsers()
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.getAllUserLiveData.observe(this) {
            adapter.submitList(it.toMutableList())
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
                    viewModel.searchUsers(str)
                }
            }
        }
    }

    private fun editStudent() {
        adapter.setOnUserClickListener { user ->
            val intent = Intent(this@MainActivity, AddUserActivity::class.java)
            intent.putExtra("id", user.id)
            intent.putExtra("isEdit", true)
            intent.putExtra("name", user.name)
            intent.putExtra("surname", user.surname)
            intent.putExtra("profile", user.profile)
            startActivity(intent)
        }
    }

    private fun deleteUser() {
        val itemTouchHelperCallBack = object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ) = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val user: User = adapter.getItemById(position)

                Toast.makeText(this@MainActivity, "Deleted", Toast.LENGTH_SHORT).show()

                lifecycleScope.launchWhenResumed {
                    viewModel.deleteUser(user)
                }

                adapter.removeItem(user)
                adapter.notifyItemRemoved(position)

                Snackbar.make(
                    viewHolder.itemView,
                    "Deleted",
                    Snackbar.LENGTH_SHORT
                ).apply {
                    setAction("Undo") {
                        lifecycleScope.launchWhenResumed {
                            viewModel.addUser(user)
                        }
                        adapter.addItem(user)
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

    private fun changeLanguage() {
        binding.ivLanguage.setOnClickListener {
            binding.changeLanguage.visibility = View.VISIBLE

            binding.ivEng.setOnClickListener {
                val pref = getSharedPreferences("pref", Context.MODE_PRIVATE)
                pref.edit().putString("language", "en").apply()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }

            binding.ivRuss.setOnClickListener {
                val pref = getSharedPreferences("pref", Context.MODE_PRIVATE)
                pref.edit().putString("language", "ru").apply()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }

            binding.ivClose.setOnClickListener {
                binding.changeLanguage.visibility = View.GONE
            }
        }
    }

    private fun setAppLocale(languageFromPreference: String?, context: Context) {
        if (languageFromPreference != null) {
            val resources: Resources = context.resources
            val dm: DisplayMetrics = resources.displayMetrics
            val config: Configuration = resources.configuration
            config.setLocale(Locale(languageFromPreference.lowercase(Locale.ROOT)))
            val pref = getSharedPreferences("pref", Context.MODE_PRIVATE)
            pref.edit().putString("language", languageFromPreference.toString().lowercase()).apply()
            resources.updateConfiguration(config, dm)
        }
    }

}
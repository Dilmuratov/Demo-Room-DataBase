package com.example.demoroomdatabaseapp

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.demoroomdatabaseapp.databinding.ActivityAddUserBinding
import java.util.*

class AddUserActivity : AppCompatActivity() {
    lateinit var binding: ActivityAddUserBinding
    lateinit var dao: UserDao
    lateinit var imageList: MutableList<Int>
    private var selectedImage = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val pref = getSharedPreferences("pref", Context.MODE_PRIVATE)
        setAppLocale(pref.getString("language","en"),this@AddUserActivity)
        binding = ActivityAddUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dao = UserDataBase.getInstance(this).getUsersDao()

        val id = intent.getIntExtra("id", 0)

        selectImage()

        val isEdit = intent.getBooleanExtra("isEdit", false)
        if (isEdit) {

            //binding.tvTitle.text = "Edit User"
            binding.tvTitle.setText(R.string.tv_title2)
            //binding.btnAddOrSave.text = "Save"
            binding.btnAddOrSave.setText(R.string.btn_add_or_save2)
            binding.tietName.setText(intent.getStringExtra("name"))
            binding.tietSurname.setText(intent.getStringExtra("surname"))
            binding.ivProfile.setImageResource(intent.getIntExtra("profile", R.drawable.harry_potter))

            Log.d("AddUser", "editUser()")

        } else {
            //addUser()

            //binding.tvTitle.text = "New User"
            //binding.btnAddOrSave.text = "Add"
            binding.tvTitle.setText(R.string.tv_title)
            //binding.btnAddOrSave.text = "Save"
            binding.btnAddOrSave.setText(R.string.btn_add_or_save)

            Log.d("AddUser", "else jagdayi")
        }

        binding.btnAddOrSave.setOnClickListener {
            if (intent.getBooleanExtra("isEdit", false)) {

                val name = binding.tietName.text.toString()
                val surname = binding.tietSurname.text.toString()

                if (name.isNotEmpty() && surname.isNotEmpty()) {
                    lifecycleScope.launchWhenResumed {
                        dao.editUser(
                            User(
                                id = id,
                                profile = imageList[selectedImage],
                                name = name,
                                surname = surname
                            )
                        )
                        Log.d("SSSS", "$name, $surname")
                    }
                    Toast.makeText(this@AddUserActivity, "Succesfully edited", Toast.LENGTH_SHORT)
                        .show()
                    finish()
                } else Toast.makeText(
                    this@AddUserActivity,
                    "Enter your name and surname",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val name = binding.tietName.text.toString()
                val surname = binding.tietSurname.text.toString()

                if (name.isNotEmpty() && surname.isNotEmpty()) {
                    lifecycleScope.launchWhenResumed {
                        dao.addUser(User(0, imageList[selectedImage], name, surname))
                    }
                    Toast.makeText(this@AddUserActivity, "Succesfully added", Toast.LENGTH_SHORT).show()
                    finish()
                } else Toast.makeText(
                    this@AddUserActivity,
                    "Enter your name and surname",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        backButton()
    }


    /**
     * backButton
     */
    private fun backButton() {
        binding.ivBack.setOnClickListener {
            finish()
        }
    }

    /**
     * Select Image
     */
    private fun selectImage() {
        imageList = mutableListOf<Int>()

        imageList.add(R.drawable.harry_potter)
        imageList.add(R.drawable.ron_weasley)
        imageList.add(R.drawable.hermione)
        imageList.add(R.drawable.draco)
        imageList.add(R.drawable.albus_dambldor)
        imageList.add(R.drawable.mcgonall)
        imageList.add(R.drawable.dobby)
        imageList.add(R.drawable.lord_voldemort)

        selectedImage = 0

        binding.tvChange.setOnClickListener {
            if (selectedImage == imageList.size - 1) {
                selectedImage = 0
            } else selectedImage++

            binding.ivProfile.setImageResource(imageList[selectedImage])
        }
    }

    private fun setAppLocale(languageFromPreference: String?, context: Context) {
        if (languageFromPreference != null) {
            val resources: Resources = context.resources
            val dm: DisplayMetrics = resources.displayMetrics
            val config: Configuration = resources.configuration
            config.setLocale(Locale(languageFromPreference.lowercase(Locale.ROOT)))
            val pref = getSharedPreferences("pref", Context.MODE_PRIVATE)
            pref.edit().putString("language",languageFromPreference.toString().lowercase()).apply()
            resources.updateConfiguration(config, dm)
        }
    }
}
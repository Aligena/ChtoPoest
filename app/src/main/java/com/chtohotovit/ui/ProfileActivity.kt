package com.chtohotovit.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.chtohotovit.R
import com.chtohotovit.data.UserStore
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText

/** Настройки профиля пользователя. */
class ProfileActivity : AppCompatActivity() {

    private val store by lazy { UserStore(this) }

    private lateinit var tvAvatar: TextView
    private lateinit var tvName: TextView
    private lateinit var tvEmail: TextView
    private lateinit var etName: TextInputEditText
    private lateinit var etEmail: TextInputEditText
    private lateinit var etGoal: TextInputEditText
    private lateinit var tvError: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        tvAvatar = findViewById(R.id.tv_avatar)
        tvName = findViewById(R.id.tv_profile_name)
        tvEmail = findViewById(R.id.tv_profile_email)
        etName = findViewById(R.id.et_name)
        etEmail = findViewById(R.id.et_email)
        etGoal = findViewById(R.id.et_goal)
        tvError = findViewById(R.id.tv_profile_error)

        findViewById<ImageButton>(R.id.btn_back).setOnClickListener { finish() }

        fillFields()

        findViewById<MaterialButton>(R.id.btn_save).setOnClickListener { save() }

        findViewById<TextView>(R.id.btn_favorites).setOnClickListener {
            val intent = Intent(this, DishListActivity::class.java)
            intent.putExtra(DishListActivity.EXTRA_MODE, DishListActivity.MODE_FAVORITES)
            startActivity(intent)
        }

        findViewById<TextView>(R.id.btn_logout).setOnClickListener {
            MaterialAlertDialogBuilder(this)
                .setTitle("Выйти из аккаунта?")
                .setMessage("Выбранные продукты не сохранятся.")
                .setNegativeButton("Отмена", null)
                .setPositiveButton("Выйти") { _, _ ->
                    store.logout()
                    val intent = Intent(this, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                }
                .show()
        }

        findViewById<TextView>(R.id.btn_delete).setOnClickListener {
            MaterialAlertDialogBuilder(this)
                .setTitle("Удалить аккаунт?")
                .setMessage("Аккаунт, профиль и избранные блюда будут удалены с устройства.")
                .setNegativeButton("Отмена", null)
                .setPositiveButton("Удалить") { _, _ ->
                    store.deleteCurrentAccount()
                    val intent = Intent(this, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                }
                .show()
        }
    }

    private fun fillFields() {
        val name = store.userName()
        tvName.text = name
        tvEmail.text = store.currentEmail ?: ""
        tvAvatar.text = if (name.isNotEmpty()) name.first().uppercase() else "?"
        etName.setText(name)
        etEmail.setText(store.currentEmail ?: "")
        etGoal.setText(store.calorieGoal().toString())
    }

    private fun save() {
        val name = etName.text?.toString().orEmpty()
        val email = etEmail.text?.toString().orEmpty()
        val goal = etGoal.text?.toString().orEmpty().toIntOrNull() ?: 0

        val error = store.updateProfile(name, email, goal)
        if (error != null) {
            tvError.text = error
            tvError.visibility = View.VISIBLE
            return
        }

        tvError.visibility = View.GONE
        fillFields()
        Toast.makeText(this, "Профиль сохранён", Toast.LENGTH_SHORT).show()
    }
}

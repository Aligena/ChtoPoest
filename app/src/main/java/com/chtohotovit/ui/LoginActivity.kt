package com.chtohotovit.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.chtohotovit.R
import com.chtohotovit.data.UserStore
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

/** Экран входа и регистрации. */
class LoginActivity : AppCompatActivity() {

    private val store by lazy { UserStore(this) }

    private lateinit var boxName: TextInputLayout
    private lateinit var etName: TextInputEditText
    private lateinit var etEmail: TextInputEditText
    private lateinit var etPassword: TextInputEditText
    private lateinit var tvError: TextView
    private lateinit var tvHint: TextView
    private lateinit var btnSubmit: MaterialButton
    private lateinit var btnSwitch: MaterialButton

    private var registerMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Если пользователь уже вошёл — сразу на главный экран
        if (store.isLoggedIn()) {
            openMain()
            return
        }

        setContentView(R.layout.activity_login)

        boxName = findViewById(R.id.box_name)
        etName = findViewById(R.id.et_name)
        etEmail = findViewById(R.id.et_email)
        etPassword = findViewById(R.id.et_password)
        tvError = findViewById(R.id.tv_error)
        tvHint = findViewById(R.id.tv_hint)
        btnSubmit = findViewById(R.id.btn_submit)
        btnSwitch = findViewById(R.id.btn_switch)

        btnSwitch.setOnClickListener {
            registerMode = !registerMode
            applyMode()
        }

        btnSubmit.setOnClickListener { submit() }
    }

    private fun applyMode() {
        tvError.visibility = View.GONE
        if (registerMode) {
            boxName.visibility = View.VISIBLE
            tvHint.text = "Создайте аккаунт — и подберём, что приготовить"
            btnSubmit.text = "Зарегистрироваться"
            btnSwitch.text = "Уже есть аккаунт? Войти"
        } else {
            boxName.visibility = View.GONE
            tvHint.text = "Готовьте из того, что уже есть дома"
            btnSubmit.text = "Войти"
            btnSwitch.text = "Нет аккаунта? Зарегистрироваться"
        }
    }

    private fun submit() {
        val name = etName.text?.toString().orEmpty()
        val email = etEmail.text?.toString().orEmpty()
        val password = etPassword.text?.toString().orEmpty()

        val error = if (registerMode) {
            store.register(name, email, password)
        } else {
            store.login(email, password)
        }

        if (error != null) {
            tvError.text = error
            tvError.visibility = View.VISIBLE
        } else {
            tvError.visibility = View.GONE
            openMain()
        }
    }

    private fun openMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}

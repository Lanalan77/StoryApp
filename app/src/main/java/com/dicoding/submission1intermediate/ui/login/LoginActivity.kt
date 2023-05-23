package com.dicoding.submission1intermediate.ui.login

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.dicoding.submission1intermediate.MainActivity
import com.dicoding.submission1intermediate.R
import com.dicoding.submission1intermediate.databinding.ActivityLoginBinding
import com.dicoding.submission1intermediate.util.UserPrefData
import com.dicoding.submission1intermediate.util.UserPreference

class LoginActivity : AppCompatActivity() {

    private lateinit var loginBinding: ActivityLoginBinding
    private lateinit var loginViewModel: LoginViewModel

    private val userPrefData: UserPrefData = UserPrefData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(loginBinding.root)
        supportActionBar?.hide()
        supportActionBar?.title = "Login"


        loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        loginBinding.edLoginPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s?.length!! < 8) {
                    btnDisabled()
                } else {
                    btnEnabled()
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })

        loginBinding.btLogin.setOnClickListener {
            val email = loginBinding.edLoginEmail.text.toString()
            val password = loginBinding.edLoginPassword.text.toString()
            loginViewModel.procLogin(this, email, password)

            loginViewModel.message.observe(this){ message ->
                if (message.isNotEmpty()) {
                    checkLogin(message)
                }
            }

            loginViewModel.isLoading.observe(this) { loading ->
                showLoading(loading)
            }
        }


        loginViewModel.token.observe(this) {
            if (it != null) {
                saveSession(it)
            }
        }

    }

    private fun checkLogin(msg: String) {
        if (msg == "success") {
            Toast.makeText(this, "Login success!", Toast.LENGTH_SHORT).show()
            finish()
            startActivity(Intent(this, MainActivity::class.java))
        } else {
            Toast.makeText(this, "Login failed!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun btnEnabled() {
        loginBinding.btLogin.isEnabled = true
        loginBinding.btLogin.isClickable = true
        loginBinding.btLogin.alpha = 1f
        loginBinding.btLogin.setTextColor(ContextCompat.getColor(this, R.color.enabled_text_color))
    }

    private fun btnDisabled() {
        loginBinding.btLogin.isEnabled = false
        loginBinding.btLogin.isClickable = false
        loginBinding.btLogin.alpha = 0.5f
        loginBinding.btLogin.setTextColor(ContextCompat.getColor(this, R.color.disabled_text_color))
    }

    private fun saveSession(apiToken: String){
        val saveSession = UserPreference(this)
        userPrefData.apiToken = apiToken
        userPrefData.isLogin = apiToken != ""
        saveSession.setSession(userPrefData)
    }

    private fun showLoading(isLoading: Boolean) {
        loginBinding.pbLogin.visibility = if (isLoading) View.VISIBLE else View.GONE

    }
}



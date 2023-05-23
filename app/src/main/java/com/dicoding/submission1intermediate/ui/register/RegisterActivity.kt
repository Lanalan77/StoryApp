package com.dicoding.submission1intermediate.ui.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.dicoding.submission1intermediate.R
import com.dicoding.submission1intermediate.databinding.ActivityRegisterBinding
import com.dicoding.submission1intermediate.ui.login.LoginActivity

class RegisterActivity : AppCompatActivity() {

    private lateinit var registerBinding: ActivityRegisterBinding
    private lateinit var registerViewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerBinding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(registerBinding.root)
        supportActionBar?.hide()
        supportActionBar?.title = "Register"

        registerViewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)

        registerBinding.edRegisterPassword.addTextChangedListener(object : TextWatcher {
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

        registerBinding.btRegister.setOnClickListener {
            val name = registerBinding.edRegisterName.text.toString()
            val email = registerBinding.edRegisterEmail.text.toString()
            val password = registerBinding.edRegisterPassword.text.toString()

            if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                registerViewModel.procRegister(name, email, password)

                registerViewModel.message.observe(this) { message ->
                    messageRegister(message)
                }
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }

            registerViewModel.isLoading.observe(this) { loading ->
                showLoading(loading)
            }

        }
    }

    private fun showLoading(isLoading: Boolean) {
        registerBinding.pbRegister.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun messageRegister(message: String){
        Log.d(TAG, message.toString())
        if (message != "User created"){
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        } else{
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            finish()
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    private fun btnEnabled() {
        registerBinding.btRegister.isEnabled = true
        registerBinding.btRegister.isClickable = true
        registerBinding.btRegister.alpha = 1f
        registerBinding.btRegister.setTextColor(ContextCompat.getColor(this, R.color.enabled_text_color))
    }

    private fun btnDisabled() {
        registerBinding.btRegister.isEnabled = false
        registerBinding.btRegister.isClickable = false
        registerBinding.btRegister.alpha = 0.5f
        registerBinding.btRegister.setTextColor(ContextCompat.getColor(this, R.color.disabled_text_color))
    }


    companion object {
        const val TAG = "Email is taken"
    }

}
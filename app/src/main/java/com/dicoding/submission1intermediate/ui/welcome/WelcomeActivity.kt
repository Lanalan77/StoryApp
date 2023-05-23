package com.dicoding.submission1intermediate.ui.welcome

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.submission1intermediate.databinding.ActivityWelcomeBinding
import com.dicoding.submission1intermediate.ui.login.LoginActivity
import com.dicoding.submission1intermediate.ui.register.RegisterActivity

class WelcomeActivity : AppCompatActivity() {

    private lateinit var welcomeBinding: ActivityWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        welcomeBinding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(welcomeBinding.root)
        supportActionBar?.title = "Welcome"

        supportActionBar?.elevation = 0f

        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        setupAction()
        playAnimation()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }

    private fun setupAction() {
        welcomeBinding.loginButton.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        welcomeBinding.registerButton.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun playAnimation() {
        /** Animasi untuk imageView**/
        ObjectAnimator.ofFloat(welcomeBinding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        /** Animasi textView dan button **/
        val login = ObjectAnimator.ofFloat(welcomeBinding.loginButton, View.ALPHA, 1f).setDuration(500)
        val signup = ObjectAnimator.ofFloat(welcomeBinding.registerButton, View.ALPHA, 1f).setDuration(500)
        val title = ObjectAnimator.ofFloat(welcomeBinding.titleTextView, View.ALPHA, 1f).setDuration(500)
        val desc = ObjectAnimator.ofFloat(welcomeBinding.descTextView, View.ALPHA, 1f).setDuration(500)

        val together = AnimatorSet().apply {
            playTogether(login, signup)
        }


        AnimatorSet().apply {
            playSequentially(title, desc, together)
            start()
        }
    }

}
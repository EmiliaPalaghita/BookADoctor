package com.example.pemil.bookadoctor.Activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.example.pemil.bookadoctor.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.login_activity.*

class LoginActivity : AppCompatActivity() {

    private var mAuth: FirebaseAuth? = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)

        login_button.setOnClickListener {
            loginWithEmail()
        }

        signup_button.setOnClickListener {
            val intent = Intent(applicationContext, SignUpActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loginWithEmail() {
        val email = input_user_email.text.toString()
        val password = input_user_password.text.toString()

        mAuth?.signInWithEmailAndPassword(email, password)?.addOnCompleteListener {
            if (it.isSuccessful) {
                startMainActivity()
            } else {
                Toast.makeText(this, "Authentification failed. Please try again", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val currentUser = mAuth?.currentUser
        if (currentUser != null) {
            startMainActivity()
        }
    }

    override fun onResume() {
        super.onResume()
        val currentUser = mAuth?.currentUser
        if (currentUser != null) {
            startMainActivity()
        }
    }

    private fun startMainActivity() {
        val intent = Intent(applicationContext, MainActivity::class.java)
        startActivity(intent)
    }
}
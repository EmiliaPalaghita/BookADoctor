package com.example.pemil.bookadoctor.Activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.example.pemil.bookadoctor.R
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.login_activity.*

class LoginActivity : AppCompatActivity() {

    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        FirebaseApp.initializeApp(this)
        auth = FirebaseAuth.getInstance()

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

        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this@LoginActivity, "Login successful", Toast.LENGTH_SHORT).show()
                updateUI(auth.currentUser)
            } else {
                Toast.makeText(this@LoginActivity, "Login failed", Toast.LENGTH_SHORT).show()
                updateUI(null)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val firebaseUser = auth.currentUser
        updateUI(firebaseUser)
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            val newIntent = Intent(this, MainActivity::class.java).apply {
                putExtra("username", user.displayName)
            }
            startActivity(newIntent)

        }
    }
}
package com.example.pemil.bookadoctor.Activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.pemil.bookadoctor.R
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        register_button.setOnClickListener {
            registerNewUser()
        }

    }

    private fun registerNewUser() {

        val name = input_name.text.toString()
        val email = input_email.text.toString()

    }
}

package com.example.pemil.bookadoctor.Activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.example.pemil.bookadoctor.Models.Doctor
import com.example.pemil.bookadoctor.Models.Patient
import com.example.pemil.bookadoctor.R
import com.example.pemil.bookadoctor.R.id.input_specialty
import com.example.pemil.bookadoctor.R.id.isDoctor
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        input_specialty.visibility = View.GONE

        register_button.setOnClickListener {
            registerNewUser()
        }

        isDoctor.setOnClickListener {
            if (isDoctor.isPressed) {
                input_specialty.visibility = View.VISIBLE
            } else {
                input_specialty.visibility = View.GONE
            }
        }

    }

    private fun registerNewUser() {
        val name = input_name.text.toString()
        val email = input_email.text.toString()
        val isDoctorBool = isDoctor.isPressed
        if (!isDoctorBool) {
            val patient = Patient(name = name, email = email)
        } else {
            val specialty = input_specialty.text.toString()
            val address = input_location.text.toString()
            val doctor = Doctor(name = name, email = email, specialty = specialty, locationAddress = address)
        }

    }
}

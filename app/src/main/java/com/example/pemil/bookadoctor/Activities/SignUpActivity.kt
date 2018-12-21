package com.example.pemil.bookadoctor.Activities

import android.graphics.PorterDuff
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.example.pemil.bookadoctor.Models.Doctor
import com.example.pemil.bookadoctor.Models.Patient
import com.example.pemil.bookadoctor.R
import kotlinx.android.synthetic.main.activity_sign_up.*
import java.text.SimpleDateFormat
import java.util.*


class SignUpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        register_button.setOnClickListener {
            registerNewUser()
        }

        password_confirm.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val pass = password.text.toString()
                val cpass = s.toString()
                if (pass.isNotEmpty() && cpass.isNotEmpty()) {
                    if (pass != cpass) {
                        password_confirm.background.setColorFilter(resources.getColor(R.color.red),
                                PorterDuff.Mode.SRC_ATOP)
                    } else {
                        password_confirm.background.setColorFilter(resources.getColor(R.color.colorAccent),
                                PorterDuff.Mode.SRC_ATOP)
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        isPatient.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                doctor_info.visibility = View.VISIBLE
            } else {
                doctor_info.visibility = View.GONE
            }
        }
    }

    private fun isEmpty(editText: EditText): Boolean {
        val value = editText.text.toString()
        if (value.isEmpty()) {
            editText.error = "This field can't be empty"
            return true
        }
        return false
    }

    private fun checkIfInputsEmpty(): Boolean {
        val basicInput = isEmpty(input_name) &&
                isEmpty(date_of_birth) &&
                isEmpty(sex) &&
                isEmpty(address) &&
                isEmpty(ssn) &&
                isEmpty(series) &&
                isEmpty(ssn_number) &&
                isEmpty(health_card_number) &&
                isEmpty(username) &&
                isEmpty(input_email) &&
                isEmpty(password) &&
                isEmpty(password_confirm)
        if (isPatient.isChecked) {
            val doctorBoolean = isEmpty(input_speciality) &&
                    isEmpty(health_clinic) &&
                    isEmpty(health_clinic_address)
            return basicInput && doctorBoolean
        }

        return basicInput
    }

    private fun registerNewUser() {
        val emptyInput = checkIfInputsEmpty()

        if (!emptyInput) {
            val birthday = date_of_birth.text.toString()
            val format = SimpleDateFormat("dd/MM/YYYY", Locale.ENGLISH)
            val birthdayDate = format.parse(birthday)

            if (!isPatient.isChecked) {
                val patient = Patient(input_name.text.toString(),
                        birthdayDate,
                        sex.text.toString(),
                        address.text.toString(),
                        ssn.text.toString(),
                        series.text.toString() + ssn_number.text.toString(),
                        health_card_number.text.toString(),
                        username.text.toString(),
                        input_email.text.toString()
                )
            } else {
                val doctor = Doctor(input_name.text.toString(),
                        birthdayDate,
                        sex.text.toString(),
                        address.text.toString(),
                        ssn.text.toString(),
                        series.text.toString() + ssn_number.text.toString(),
                        health_card_number.text.toString(),
                        username.text.toString(),
                        input_email.text.toString(),
                        input_speciality.text.toString(),
                        health_clinic.text.toString(),
                        health_clinic_address.text.toString())
            }

            if (!agreesData.isChecked or !agreesTerms.isChecked) {
                Toast.makeText(applicationContext,
                        "Please agree with data processing and with our terms",
                        Toast.LENGTH_LONG)
                        .show()
            }
        }

    }
}

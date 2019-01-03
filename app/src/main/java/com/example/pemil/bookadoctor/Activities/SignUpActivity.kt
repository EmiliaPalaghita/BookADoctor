package com.example.pemil.bookadoctor.Activities

import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import com.example.pemil.bookadoctor.Models.Doctor
import com.example.pemil.bookadoctor.Models.Patient
import com.example.pemil.bookadoctor.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.sign_up_activity.*
import java.text.SimpleDateFormat
import java.util.*


class SignUpActivity : AppCompatActivity() {

    private var auth = FirebaseAuth.getInstance()
    private var database = FirebaseDatabase.getInstance()
    private var patientDatabase = database.getReference("patients")
    private var doctorDatabase = database.getReference("doctors")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sign_up_activity)

        register_button.setOnClickListener {
            registerNewUser()
        }

        ArrayAdapter.createFromResource(
                this,
                R.array.sex_array,
                android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            sex.adapter = adapter
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

            if (!agreesData.isChecked or !agreesTerms.isChecked) {
                Toast.makeText(applicationContext,
                        "Please agree with data processing and with our terms",
                        Toast.LENGTH_LONG)
                        .show()
            } else {
                createAccount(input_email.text.toString(), password.text.toString())
            }
        }

    }

    private fun createAccount(email: String, password: String) {

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                val user = auth.currentUser

                createUser(user?.uid!!)

                updateUI(user)
            } else {
                Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                updateUI(null)
            }
        }
    }

    private fun createUser(uid: String) {

        if (!isPatient.isChecked) {
            val patient = Patient(uid,
                    input_name.text.toString(),
                    date_of_birth.text.toString(),
                    sex.selectedItem.toString(),
                    address.text.toString(),
                    ssn.text.toString(),
                    series.text.toString() + ssn_number.text.toString(),
                    health_card_number.text.toString(),
                    username.text.toString(),
                    input_email.text.toString())

            patientDatabase.child(uid).setValue(patient)
        } else {
            val doctor = Doctor(uid,
                    input_name.text.toString(),
                    date_of_birth.text.toString(),
                    sex.selectedItem.toString(),
                    address.text.toString(),
                    ssn.text.toString(),
                    series.text.toString() + ssn_number.text.toString(),
                    health_card_number.text.toString(),
                    username.text.toString(),
                    input_email.text.toString(),
                    input_speciality.text.toString(),
                    health_clinic.text.toString(),
                    health_clinic_address.text.toString())

            doctorDatabase.child(uid).setValue(doctor)
        }
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            val newIntent = Intent(this, MainActivity::class.java).apply {
            }
            startActivity(newIntent)
        }
    }
}

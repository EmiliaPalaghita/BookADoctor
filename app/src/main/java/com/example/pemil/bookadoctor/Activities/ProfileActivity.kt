package com.example.pemil.bookadoctor.Activities

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.example.pemil.bookadoctor.Models.Doctor
import com.example.pemil.bookadoctor.Models.Patient
import com.example.pemil.bookadoctor.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.profile_activity.*

class ProfileActivity : AppCompatActivity() {

    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private var database = FirebaseDatabase.getInstance()
    var doctorsReference = database.getReference("doctors")
    var patientsReference = database.getReference("patients")
    private val user = mAuth.currentUser
    var patient: Patient? = null
    var doctor: Doctor? = null
    var isEditEnabled: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile_activity)

        edit_tv.setOnClickListener {
            isEditEnabled = !isEditEnabled
            activateEditMode()
        }

        save_edit_button.setOnClickListener { saveChanges() }

        phone_layout.visibility = View.GONE

        retrieveDataFromDB()
    }

    private fun saveChanges() {
        if (isEditEnabled) {
            if (patient != null) {
                val newPatient = Patient(user!!.uid,
                        input_name.text.toString(),
                        patient!!.birthday,
                        patient!!.sex,
                        address.text.toString(),
                        ssn_et.text.toString(),
                        series_et.text.toString(),
                        health_et.text.toString(),
                        patient!!.username,
                        email_et.text.toString())

                patientsReference.child(user.uid).setValue(newPatient)
            } else if (doctor != null) {
                val healthClinic = health_clinic_address.text.toString().split(", ")
                val newDoctor = Doctor(user!!.uid,
                        input_name.text.toString(),
                        doctor!!.birthday,
                        doctor!!.sex,
                        address.text.toString(),
                        ssn_et.text.toString(),
                        series_et.text.toString(),
                        health_et.text.toString(),
                        doctor!!.username,
                        email_et.text.toString(),
                        spec_et.text.toString(),
                        healthClinic[0],
                        healthClinic[1])

                doctorsReference.child(user.uid).setValue(newDoctor)
            }
        }
    }

    private fun modifyEditTextEnabled(view: EditText, isEnabled: Boolean) {
        view.isEnabled = isEnabled
        view.isFocusableInTouchMode = isEnabled
    }

    private fun activateEditMode() {
        if (isEditEnabled) {
            modifyEditTextEnabled(input_name, true)
            modifyEditTextEnabled(address, true)
            modifyEditTextEnabled(email_et, true)
            modifyEditTextEnabled(ssn_et, true)
            modifyEditTextEnabled(series_et, true)
            modifyEditTextEnabled(health_et, true)

            if (doctor != null) {
                modifyEditTextEnabled(spec_et, true)
                modifyEditTextEnabled(health_clinic_address, true)
            }

            save_edit_button.visibility = View.VISIBLE

            edit_tv.setText(R.string.cancel)
        } else {
            modifyEditTextEnabled(input_name, false)
            modifyEditTextEnabled(address, false)
            modifyEditTextEnabled(email_et, false)
            modifyEditTextEnabled(ssn_et, false)
            modifyEditTextEnabled(series_et, false)
            modifyEditTextEnabled(health_et, false)

            if (doctor != null) {
                modifyEditTextEnabled(spec_et, false)
                modifyEditTextEnabled(health_clinic_address, false)
            }

            save_edit_button.visibility = View.GONE
            edit_tv.setText(R.string.edit)
        }


    }

    private fun setEditText(view: EditText, value: String) {
        view.setTextColor(Color.BLACK)
        view.setText(value)
    }

    private fun updateUI() {
        if (patient != null) {
            setEditText(input_name, patient!!.fullName)
            setEditText(address, patient!!.address)
            setEditText(email_et, patient!!.email)
            setEditText(ssn_et, patient!!.socialSecurityNumber)
            setEditText(series_et, patient!!.seriesNumber)
            setEditText(health_et, patient!!.healthCardNumber)

            health_clinic_layout.visibility = View.GONE
            spec_layout.visibility = View.GONE

        } else if (doctor != null) {

            health_clinic_layout.visibility = View.VISIBLE
            spec_layout.visibility = View.VISIBLE

            setEditText(input_name, doctor!!.fullName)
            setEditText(address, doctor!!.address)
            setEditText(email_et, doctor!!.email)
            setEditText(ssn_et, doctor!!.socialSecurityNumber)
            setEditText(series_et, doctor!!.seriesNumber)
            setEditText(health_et, doctor!!.healthCardNumber)
            setEditText(spec_et, doctor!!.specialty)
            setEditText(health_clinic_address, doctor!!.healthClinicName + ", " + doctor!!.healthClinicAddress)


        }
    }

    private fun displayFirebaseError() {
        Toast.makeText(this@ProfileActivity, "Failed to load data", Toast.LENGTH_LONG).show()
    }

    private fun retrieveDataFromDB() {
        doctorsReference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                displayFirebaseError()
            }

            override fun onDataChange(p0: DataSnapshot) {
                for (data in p0.children) {
                    if (data.key == user!!.uid) {
                        doctor = data.getValue(Doctor::class.java)!!
                    }
                }

                updateUI()
            }
        })

        patientsReference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                displayFirebaseError()
            }

            override fun onDataChange(p0: DataSnapshot) {
                for (data in p0.children) {
                    if (data.value == user!!.uid) {
                        patient = data.getValue(Patient::class.java)!!
                    }
                }

                updateUI()
            }
        })
    }
}
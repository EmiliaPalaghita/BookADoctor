package com.example.pemil.bookadoctor.Activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.example.pemil.bookadoctor.R
import kotlinx.android.synthetic.main.appointment_activity.*


class AppointmentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.appointment_activity)

        edit_cancel_tv.visibility = View.GONE

        back_ib.setOnClickListener { goBackWithoutSaving() }

    }

    private fun goBackWithoutSaving() {
        val alertDialog = AlertDialog.Builder(this)
                .setTitle("Unsaved changes")
                .setMessage("Are you sure you want to discard changes?")
                .setNegativeButton("No") { _, _ -> }
                .setPositiveButton("Yes") { _, _ ->
                    val newIntent = Intent(this@AppointmentActivity, MainActivity::class.java)
                    startActivity(newIntent)
                }.create()
        alertDialog.show()
    }

    override fun onBackPressed() {
        goBackWithoutSaving()
    }
}
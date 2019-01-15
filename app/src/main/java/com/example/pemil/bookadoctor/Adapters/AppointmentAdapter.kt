package com.example.pemil.bookadoctor.Adapters

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.example.pemil.bookadoctor.Models.Appointment
import com.example.pemil.bookadoctor.R
import kotlinx.android.synthetic.main.appointment_item.view.*

class AppointmentAdapter(val context: Context, private val elems: List<Appointment>) : BaseAdapter() {

    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val rowView = inflater.inflate(R.layout.appointment_item, parent, false)
        val appointment = elems[position]

        return rowView.apply {
            appointmentTypeTextView.text = appointment.appointmentType
            appointmentDateTextView.text = appointment.date
            appointmentTimeTextView.text = appointment.time
            clinicAddressTextView.text = appointment.locationName
            doctorNameTextView.text = appointment.doctorName
            medicalSpecialtyTextView.text = appointment.specialty
            cardTopPart.setOnClickListener {
                val alertDialog = AlertDialog.Builder(context)
                        .setTitle("Update appointment")
                        .setMessage("Please choose an option from below")
                        .setNegativeButton("Cancel") { _, _ -> }
                        .setPositiveButton("I'm here") { _, _ ->
                            updateAppointment(position)
                        }.create()
                alertDialog.show()
            }
        }
    }

    private fun updateAppointment(position: Int) {


    }

    private fun deleteAppointment(position: Int) {
        val alertDialog = AlertDialog.Builder(context)
                .setTitle("Are you sure?")
                .setMessage("Are you sure you want to cancel your appointment?")
                .setNegativeButton("No") { _, _ -> }
                .setPositiveButton("Yes") { _, _ ->
                    safeDeleteAppointment(position)
                }.create()
        alertDialog.show()
    }

    private fun safeDeleteAppointment(position: Int) {


    }

    override fun getItem(position: Int) = elems[position]

    override fun getItemId(position: Int) = elems[position].hashCode().toLong()

    override fun getCount() = elems.size
}
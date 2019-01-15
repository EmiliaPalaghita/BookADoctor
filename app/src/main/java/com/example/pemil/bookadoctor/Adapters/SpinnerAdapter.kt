package com.example.pemil.bookadoctor.Adapters

import android.content.Context
import android.graphics.Typeface
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class SpinnerAdapter(context: Context, textViewResourceId: Int) : ArrayAdapter<Any>(context, textViewResourceId) {

    var myFont = Typeface.createFromAsset(getContext().assets, "fonts/quick_sans_light.otf")

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): TextView {
        val v = super.getView(position, convertView, parent) as TextView
        v.typeface = myFont
        return v
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): TextView {
        val v = super.getView(position, convertView, parent) as TextView
        v.typeface = myFont
        return v
    }

}
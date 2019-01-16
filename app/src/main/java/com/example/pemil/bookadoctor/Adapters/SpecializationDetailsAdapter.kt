package com.example.pemil.bookadoctor.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.example.pemil.bookadoctor.R
import kotlinx.android.synthetic.main.specialization_info_item.view.*

class SpecializationDetailsAdapter(context: Context, private val elems: MutableList<Pair<String, String>>) : BaseAdapter() {

    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    private var shown = false

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val rowView = inflater.inflate(R.layout.specialization_info_item, parent, false)
        val specialization = elems[position]

        return rowView.apply {
            spec_name.text = specialization.first
            spec_description.text = specialization.second
            drop_down_button.setOnClickListener {
                shown = !shown
                if (shown) {
                    spec_description.visibility = View.VISIBLE
                } else {
                    spec_description.visibility = View.GONE
                }
            }
        }
    }

    override fun getItem(position: Int) = elems[position]


    override fun getItemId(position: Int) = elems[position].hashCode().toLong()

    override fun getCount() = elems.size

}
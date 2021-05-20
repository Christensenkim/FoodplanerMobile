package com.example.foodplanermobile.services.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.foodplanermobile.R
import com.example.foodplanermobile.model.WeekDto

class WeekOverviewAdapter(context: Context, private val weeks: ArrayList<WeekDto>) :
    ArrayAdapter<WeekDto>(context, 0, weeks)
{

    override fun getView(position: Int, v: View?, parent: ViewGroup): View {
        var v1: View? = v
        if (v1 == null) {
            val li = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)
                    as LayoutInflater
            v1 = li.inflate(R.layout.listview_cell_weekoverview, null)
        }
        val customView: View = v1!!
        val w = weeks[position]
        val weekNumberView = customView.findViewById<TextView>(R.id.weekNumber)
        val daysPlannedView = customView.findViewById<TextView>(R.id.daysPlanned)
        weekNumberView.text = w.weekNumber.toString()
        daysPlannedView.text = w.daysPlanned.toString()
        return customView
    }
}
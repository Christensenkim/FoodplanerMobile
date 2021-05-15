package com.example.foodplanermobile.services.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.foodplanermobile.R
import com.example.foodplanermobile.model.BEMealOverview

class WeekDetailAdapter(context: Context, private val meals: Array<BEMealOverview?>) : ArrayAdapter<BEMealOverview?>(context, 0, meals)
{
    val weekDaysDanish = arrayOf("mandag", "tirsdag", "onsdag", "Torsdag", "fredag", "lørdag", "søndag")
    val nothingPlanned = "Ikke noget planlagt endnu"
    override fun getView(position: Int, v: View?, parent: ViewGroup): View {
        var v1: View? = v
        if (v1 == null) {
            val li = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)
                    as LayoutInflater
            v1 = li.inflate(R.layout.listview_cell_weekdetail, null)
        }
        val customView: View = v1!!
        val m = meals[position]
        val weekDayView = customView.findViewById<TextView>(R.id.weekdetailDay)
        val mealView = customView.findViewById<TextView>(R.id.weekdetailMeal)

        weekDayView.text = weekDaysDanish[position].capitalize()

        if (m != null) {
            mealView.text = m.name
        } else
        {
            mealView.text = nothingPlanned
        }

        return customView
    }
}
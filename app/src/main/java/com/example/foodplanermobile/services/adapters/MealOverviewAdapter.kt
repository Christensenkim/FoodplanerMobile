package com.example.foodplanermobile.services.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.foodplanermobile.R
import com.example.foodplanermobile.model.Meal


class MealOverviewAdapter (context: Context, private val meals: ArrayList<Meal>) :
    ArrayAdapter<Meal>(context, 0, meals)
{

    override fun getView(position: Int, v: View?, parent: ViewGroup): View {
        var v1: View? = v
        if (v1 == null) {
            val li = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)
                    as LayoutInflater
            v1 = li.inflate(R.layout.listview_cell_mealoverview, null)
        }
        val customView: View = v1!!
        val m = meals[position]
        val mealsName = customView.findViewById<TextView>(R.id.mealName)
        val mealsDescription = customView.findViewById<TextView>(R.id.mealDescription)
        mealsName.text = m.name
        mealsDescription.text = m.description
        return customView
    }
}
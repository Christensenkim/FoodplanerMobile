package com.example.foodplanermobile

import android.app.ListActivity
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.foodplanermobile.model.BEMeal
import com.example.foodplanermobile.model.Meal

class MealOverviewActivity : ListActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        listAdapter = MealAdapter(this, Meal().getAll())
    }

    override fun onListItemClick(parent: ListView?,
                                 v: View?, position: Int, id: Long) {
        // position is in the list!
        // first get the name of the person clicked
        val name = Meal().getAll()[position].name
        // and a greeting
        Toast.makeText(
                this,
                "Hi $name! Have you done your homework?",
                Toast.LENGTH_LONG
        ).show()
    }
}

internal class MealAdapter(context: Context, private val meals: Array<BEMeal>) : ArrayAdapter<BEMeal>(context, 0, meals)
{
    private val colours = intArrayOf(
            Color.parseColor("#AAAAAA"),
            Color.parseColor("#CCCCCC")
    )

    override fun getView(position: Int, v: View?, parent: ViewGroup): View {
        var v1: View? = v
        if (v1 == null) {
            val li = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)
                    as LayoutInflater
            v1 = li.inflate(R.layout.listview_cell, null)
        }
        val resView: View = v1!!
        resView.setBackgroundColor(colours[position % colours.size])
        val m = meals[position]
        val nameView = resView.findViewById<TextView>(R.id.tvMealName)
        val descriptionView = resView.findViewById<TextView>(R.id.tvMealDescription)
        val foodImageView = resView.findViewById<ImageView>(R.id.imgMeal)
        nameView.text = m.name
        descriptionView.text = m.description
        foodImageView.setImageResource(R.drawable.ok)
        return resView
        }
    }



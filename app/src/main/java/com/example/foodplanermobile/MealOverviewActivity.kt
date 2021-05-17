package com.example.foodplanermobile

import android.app.ListActivity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.foodplanermobile.model.BEMeal
import com.example.foodplanermobile.model.Meal
import com.example.foodplanermobile.model.WeekDto
import com.example.foodplanermobile.services.FoodplanerService
import com.example.foodplanermobile.services.adapters.WeekOverviewAdapter
import com.google.gson.Gson
import io.socket.client.Socket
import org.json.JSONArray
import java.util.ArrayList

class MealOverviewActivity : ListActivity() {

    var selectedMeal: BEMeal? = null
    var allMeals: ArrayList<BEMeal>? = null

    var mSocket: Socket? = null
    val gson: Gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val foodplanerService: FoodplanerService = application as FoodplanerService
        mSocket = foodplanerService.getMSocket()
        mSocket?.connect()

        mSocket?.on("allMeals") { args ->
            if (args[0] != null){
                var data = args[0] as JSONArray
                var mealsDB: ArrayList<BEMeal> = ArrayList()
                mealsDB.clear()
                for (i in 0 until data.length()) {
                    val meal = data[i]
                    val mealdto = gson.fromJson(meal.toString(), BEMeal::class.java)
                    mealsDB.add(mealdto)
                }
                allMeals = mealsDB
            }
            runOnUiThread{
                listAdapter = MealAdapter(this, allMeals!!)
            }
        }
        mSocket?.emit("getMeals")
    }

    override fun onListItemClick(parent: ListView?,
                                 v: View?, position: Int, id: Long) {
        val intent = Intent(this, MealDetailsActivity::class.java)
        selectedMeal = allMeals!![position]
        if (selectedMeal != null) {
            intent.putExtra("meal", selectedMeal)
            Toast.makeText(this, "You have clicked ${selectedMeal!!.name}", Toast.LENGTH_LONG).show()
            startActivity(intent)
        }
    }
}

internal class MealAdapter(context: Context, private val meals: ArrayList<BEMeal>) : ArrayAdapter<BEMeal>(context, 0, meals)
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



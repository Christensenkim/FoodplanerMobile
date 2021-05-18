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
import androidx.appcompat.app.AppCompatActivity
import com.example.foodplanermobile.model.BEMeal
import com.example.foodplanermobile.model.Meal
import com.example.foodplanermobile.model.WeekDto
import com.example.foodplanermobile.services.FoodplanerService
import com.example.foodplanermobile.services.adapters.MealOverviewAdapter
import com.example.foodplanermobile.services.adapters.WeekOverviewAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
import io.socket.client.Socket
import org.json.JSONArray
import java.util.ArrayList

class MealOverviewActivity : AppCompatActivity() {
    private lateinit var mealOverviewList: ListView
    private var listMealAdapter: MealOverviewAdapter? = null
    var selectedMeal: BEMeal? = null
    var allMeals: ArrayList<BEMeal> = ArrayList()

    var mSocket: Socket? = null
    val gson: Gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mealoverview)

        val foodplanerService: FoodplanerService = application as FoodplanerService
        mSocket = foodplanerService.getMSocket()
        mSocket?.connect()

        mSocket?.on("allMeals") { args ->
            if (args[0] != null) {
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
            runOnUiThread {
                listMealAdapter = MealOverviewAdapter(this, allMeals)
                mealOverviewList.adapter = listMealAdapter
                //listAdapter = MealAdapter(this, allMeals!!)
            }
        }
        mSocket?.emit("getMeals")

        mealOverviewList = findViewById(R.id.mealOverviewList)

        mealOverviewList.setOnItemClickListener { parent, view, position, id ->
            val intent = Intent(this, WeekDetailedActivity::class.java)
            val mealSelect = listMealAdapter!!.getItem(position)
            if (mealSelect != null) {
                intent.putExtra("week", mealSelect)
                startActivity(intent)
                //Toast.makeText(this,"Du har valgt uge: ${weekSelect.weekNumber}", Toast.LENGTH_LONG ).show()
            }
        }

        val bottomNavigationView = findViewById<View>(R.id.bottomNav) as BottomNavigationView
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }

                R.id.recipes -> {
                    val intent = Intent(this, MealOverviewActivity::class.java)
                    startActivity(intent)
                }
                R.id.addRecipe -> {
                    val intent = Intent(this, CreateMealActivity::class.java)
                    startActivity(intent)
                }
            }
            true
        }
    }
}



//internal class MealAdapter(context: Context, private val meals: ArrayList<BEMeal>) : ArrayAdapter<BEMeal>(context, 0, meals)
//{
//    private val colours = intArrayOf(
//            Color.parseColor("#AAAAAA"),
//            Color.parseColor("#CCCCCC")
//    )
//
//    override fun getView(position: Int, v: View?, parent: ViewGroup): View {
//        var v1: View? = v
//        if (v1 == null) {
//            val li = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)
//                    as LayoutInflater
//            v1 = li.inflate(R.layout.listview_cell, null)
//        }
//        val resView: View = v1!!
//        resView.setBackgroundColor(colours[position % colours.size])
//        val m = meals[position]
//        val nameView = resView.findViewById<TextView>(R.id.tvMealName)
//        val descriptionView = resView.findViewById<TextView>(R.id.tvMealDescription)
//        val foodImageView = resView.findViewById<ImageView>(R.id.imgMeal)
//        nameView.text = m.name
//        descriptionView.text = m.description
//        foodImageView.setImageResource(R.drawable.ok)
//        return resView
//        }
//    }
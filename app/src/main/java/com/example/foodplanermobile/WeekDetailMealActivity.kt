package com.example.foodplanermobile

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.foodplanermobile.model.*
import com.example.foodplanermobile.services.FoodplanerService
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
import io.socket.client.Socket
import org.json.JSONArray
import org.json.JSONObject

class WeekDetailMealActivity : AppCompatActivity() {
    private val weekDaysDanish = arrayOf("mandag", "tirsdag", "onsdag", "Torsdag", "fredag", "lørdag", "søndag")
    var mSocket: Socket? = null
    var meal: BEMeal? = null
    val gson: Gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weekdetailmeal)

        val weekView = findViewById<TextView>(R.id.weekDetailMealNumber)
        val weekDayView = findViewById<TextView>(R.id.weekDetailMealDay)
        val mealNameView = findViewById<TextView>(R.id.weekDetailMealName)
        val mealDescView = findViewById<TextView>(R.id.weekDetailMealDesc)
        val mealIngredientView = findViewById<TextView>(R.id.weekDetailMealIngredients)
        val mealDirectionsView = findViewById<TextView>(R.id.weekDetailMealDirections)

        val week = SelectedWeek.getWeek()
        val weekDay = intent.getIntExtra("weekday", 0)
        val mealID = intent.getIntExtra("mealID", 0)

        if (week != null) {
            weekView.text = week.weekNumber.toString()
        }
        weekDayView.text = weekDaysDanish[weekDay].capitalize()

        val foodplanerService: FoodplanerService = application as FoodplanerService
        mSocket = foodplanerService.getMSocket()
        mSocket?.connect()

        mSocket?.on("mealByID") { args ->
            var data = args[0] as JSONObject
            meal = gson.fromJson(data.toString(), BEMeal::class.java)
            println("backend says:" + meal?.name)
            runOnUiThread {
                mealNameView.text = meal?.name
                mealDescView.text = meal?.description
                mealIngredientView.text = meal?.ingredients
                mealDirectionsView.text = meal?.directions
            }
        }

        mSocket?.emit("getMealByID", mealID)





        val bottomNavigationView = findViewById<View>(R.id.bottomNav) as BottomNavigationView
        bottomNavigationView.setSelectedItemId(R.id.home)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(0,0)
                    true
                }

                R.id.recipes -> {
                    val intent = Intent(this, MealOverviewActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(0,0)
                    true
                }
            }
            true
        }

    }

}
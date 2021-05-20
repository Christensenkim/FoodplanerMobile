package com.example.foodplanermobile

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ListView
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.foodplanermobile.model.BEMeal
import com.example.foodplanermobile.model.BEWeek
import com.example.foodplanermobile.model.MealDto
import com.example.foodplanermobile.model.SelectedWeek
import com.example.foodplanermobile.services.FoodplanerService
import com.example.foodplanermobile.services.adapters.MealOverviewAdapter
import com.google.gson.Gson
import io.socket.client.Socket
import org.json.JSONArray
import java.util.ArrayList

class WeekDetailChangeMealActivity : AppCompatActivity() {
    private lateinit var mealListView: ListView
    private var listMealAdapter: MealOverviewAdapter? = null
    var allMeals: ArrayList<BEMeal> = ArrayList()
    lateinit var selectedMealDto: MealDto
    var mSocket: Socket? = null
    val gson: Gson = Gson()
    private val weekDaysDanish = arrayOf("mandag", "tirsdag", "onsdag", "Torsdag", "fredag", "lørdag", "søndag")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weekdetailchangemeal)

        val weekView = findViewById<TextView>(R.id.weekDetailChangeMealNumber)
        val weekDayView = findViewById<TextView>(R.id.weekDetailChangeMealDay)
        val mealNameView = findViewById<TextView>(R.id.weekDetailChangeMealName)
        val btnChangeMealToSelected = findViewById<Button>(R.id.btnChangetoSelectedMeal)

        val week = SelectedWeek.getWeek()
        val weekDay = intent.getIntExtra("weekday", 0)
        val mealName = intent.getStringExtra("mealName")

        if (week != null) {
            weekView.text = week.weekNumber.toString()
        }
        weekDayView.text = weekDaysDanish[weekDay].capitalize()
        if (mealName != null){
            mealNameView.text = mealName
        } else
        {
            mealNameView.text = "Ikke noget planlagt for idag"
        }

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
                mealListView.adapter = listMealAdapter
            }
        }
        mSocket?.emit("getMeals")

        mealListView = findViewById(R.id.weekDetailChangeMealOverviewList)

        mealListView.setOnItemClickListener { parent, view, position, id ->
            val meal = listMealAdapter!!.getItem(position)
            selectedMealDto = MealDto(meal!!.id, meal!!.name)

        }

        btnChangeMealToSelected.setOnClickListener {
            val intent = Intent(this, WeekDetailedActivity::class.java)
            when (weekDay) {
                0 -> week?.monday = selectedMealDto
                1 -> week?.tuesday = selectedMealDto
                2 -> week?.wednesday = selectedMealDto
                3 -> week?.thursday = selectedMealDto
                4 -> week?.friday = selectedMealDto
                5 -> week?.saturday = selectedMealDto
                6 -> week?.sunday = selectedMealDto
            }
            SelectedWeek.setWeek(week)
            val weekUpdate = BEWeek(
                week!!.id,
                week!!.weekNumber,
                week!!.monday?.id,
                week!!.tuesday?.id,
                week!!.wednesday?.id,
                week!!.thursday?.id,
                week!!.friday?.id,
                week!!.saturday?.id,
                week!!.sunday?.id)
            val weekJson = gson.toJson(weekUpdate)
            mSocket?.emit("update-week-mobile", weekJson)
            startActivity(intent)
        }
    }
}
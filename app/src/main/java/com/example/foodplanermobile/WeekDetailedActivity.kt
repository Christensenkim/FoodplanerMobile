package com.example.foodplanermobile

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.foodplanermobile.model.*
import com.example.foodplanermobile.services.adapters.WeekDetailAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView

class WeekDetailedActivity : AppCompatActivity(){
    private lateinit var weekDetailList: ListView
    private var weekDetailMealAdapter: WeekDetailAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weekdetailed)
        weekDetailList = findViewById(R.id.weekdetailList)

        val week = SelectedWeek.getWeek()

        val weekNumber = findViewById<TextView>(R.id.weekNumber)
        val mealArray = arrayOfNulls<MealDto>(7)
        if (week != null) {
            mealArray[0] = week.monday
            mealArray[1] = week.tuesday
            mealArray[2] = week.wednesday
            mealArray[3] = week.thursday
            mealArray[4] = week.friday
            mealArray[5] = week.saturday
            mealArray[6] = week.sunday
            weekNumber.text = week.weekNumber.toString()
        }

        weekDetailMealAdapter = WeekDetailAdapter(this, mealArray)
        weekDetailList.adapter = weekDetailMealAdapter

        weekDetailList.setOnItemClickListener {parent, view, position, id ->
            val weekDayMealIDSelect = mealArray[position]?.id?.toInt()
            if (weekDayMealIDSelect != null) {
                val intent = Intent(this, WeekDetailMealActivity::class.java)
                intent.putExtra("weekday", position)
                intent.putExtra("mealID", weekDayMealIDSelect)
                startActivity(intent)
            }
        }

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
                R.id.addRecipe -> {
                    val intent = Intent(this, CreateMealActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(0,0)
                    true
                }
            }
            true
        }
    }
}
package com.example.foodplanermobile

import android.os.Bundle
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.foodplanermobile.model.BEMealOverview
import com.example.foodplanermobile.model.BEWeek
import com.example.foodplanermobile.model.MealDto
import com.example.foodplanermobile.model.WeekDto
import com.example.foodplanermobile.services.adapters.WeekDetailAdapter

class WeekDetailedActivity : AppCompatActivity(){
    private lateinit var weekDetailList: ListView
    private var weekDetailMealAdapter: WeekDetailAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weekdetailed)
        weekDetailList = findViewById(R.id.weekdetailList)

        val week = intent.getSerializableExtra("week") as? WeekDto

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
    }
}
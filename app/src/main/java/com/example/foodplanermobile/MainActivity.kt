package com.example.foodplanermobile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.foodplanermobile.model.Week
import com.example.foodplanermobile.model.WeekDto
import com.example.foodplanermobile.services.FoodplanerService
import com.example.foodplanermobile.services.adapters.WeekOverviewAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
import io.socket.client.Socket
import org.json.JSONArray
import java.util.*


class MainActivity : AppCompatActivity() {
    private lateinit var weekOverviewList: ListView
    private var listWeekAdapter: WeekOverviewAdapter? = null
    var mSocket: Socket? = null
    val gson: Gson = Gson()
    var weeks: ArrayList<WeekDto> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigationView = findViewById<View>(R.id.bottomNav) as BottomNavigationView
        bottomNavigationView.setSelectedItemId(R.id.home)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
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

        val foodplanerService: FoodplanerService = application as FoodplanerService
        mSocket = foodplanerService.getMSocket()
        mSocket?.connect()

        mSocket?.on("return-all-weeks") { args ->
            if (args[0] != null){
                var data = args[0] as JSONArray
                var weeksDB: ArrayList<WeekDto> = ArrayList()
                weeksDB.clear()
                for (i in 0 until data.length()) {
                    val week = data[i]
                    val weekdto = gson.fromJson(week.toString(), WeekDto::class.java)
                    weeksDB.add(weekdto)
                }
                weeks = weeksDB
            }
            runOnUiThread{
                listWeekAdapter = WeekOverviewAdapter(this, weeks)
                weekOverviewList.adapter = listWeekAdapter
            }
        }
        mSocket?.emit("get-weeks")

        weekOverviewList = findViewById(R.id.weekOverviewList)

        Log.d("TAG", "antal = " + weeks.size)

        weekOverviewList.setOnItemClickListener { parent, view, position, id ->
            val intent = Intent(this, WeekDetailedActivity::class.java)
            val weekSelect = listWeekAdapter!!.getItem(position)
            if (weekSelect != null) {
                intent.putExtra("week", weekSelect)
                startActivity(intent)
                //Toast.makeText(this,"Du har valgt uge: ${weekSelect.weekNumber}", Toast.LENGTH_LONG ).show()
            }
        }
    }

    fun createNewWeek(view: View) {
        mSocket?.emit("create-new-week")
    }

    fun updateWeek(view: View) {
        val week = Week(
            1,
            1,
            1,
            1,
            2,
            1,
            1,
            1,
        )
        val weekJson = gson.toJson(week)
        mSocket?.emit("update-week-mobile", weekJson)
    }

    fun deleteWeek(view: View) {
        val weekID = 1
        mSocket?.emit("delete-week", weekID)
    }

}
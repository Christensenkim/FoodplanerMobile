package com.example.foodplanermobile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.example.foodplanermobile.model.SelectedWeek
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
            SelectedWeek.setWeek(listWeekAdapter!!.getItem(position))
            val intent = Intent(this, WeekDetailedActivity::class.java)
            startActivity(intent)
        }

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
            }
            true
        }
    }

    fun createNewWeek() {
        mSocket?.emit("create-new-week")
    }

    fun deleteWeek(view: View) {
        val weekID = 1
        mSocket?.emit("delete-week", weekID)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.app_bar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.addNew -> {
                createNewWeek()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
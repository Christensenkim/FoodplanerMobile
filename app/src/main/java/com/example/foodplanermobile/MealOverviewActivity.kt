package com.example.foodplanermobile

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.foodplanermobile.model.BEMeal
import com.example.foodplanermobile.services.FoodplanerService
import com.example.foodplanermobile.services.adapters.MealOverviewAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
import io.socket.client.Socket
import org.json.JSONArray
import java.util.ArrayList

class MealOverviewActivity : AppCompatActivity() {
    private lateinit var mealOverviewList: ListView
    private var listMealAdapter: MealOverviewAdapter? = null
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
            }
        }
        mSocket?.emit("getMeals")

        mealOverviewList = findViewById(R.id.mealOverviewList)

        mealOverviewList.setOnItemClickListener { parent, view, position, id ->
            val intent = Intent(this, MealDetailsActivity::class.java)
            val mealSelect = listMealAdapter!!.getItem(position)
            if (mealSelect != null) {
                intent.putExtra("meal", mealSelect)
                startActivity(intent)
            }
        }

        val bottomNavigationView = findViewById<View>(R.id.bottomNav) as BottomNavigationView
        bottomNavigationView.setSelectedItemId(R.id.recipes)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(0,0)
                    true
                }
                R.id.recipes -> {
                    true
                }
            }
            true
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.app_bar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.addNew -> {
                val intent = Intent(this, CreateMealActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
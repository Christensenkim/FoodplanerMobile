package com.example.foodplanermobile

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.foodplanermobile.model.BEMeal
import com.google.android.material.bottomnavigation.BottomNavigationView

class MealDetailsActivity : AppCompatActivity()  {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mealdetails)

        val bottomNavigationView = findViewById<View>(R.id.bottomNav) as BottomNavigationView
        bottomNavigationView.setSelectedItemId(R.id.home)
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

        val mealName: TextView = findViewById(R.id.MealDetailName)
        val mealDescription: TextView = findViewById(R.id.MealDetailDescription)
        val mealIngredients: TextView = findViewById(R.id.MealDetailIngredients)
        val mealDirections: TextView = findViewById(R.id.MealDetailDirections)

        if (intent.extras != null) {
            val meal = intent.getSerializableExtra("meal") as BEMeal

            mealName.text = meal.name
            mealDescription.text = meal.description
            mealIngredients.text = meal.ingredients
            mealDirections.text = meal.directions
        }
    }
}
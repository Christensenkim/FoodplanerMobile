package com.example.foodplanermobile

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.foodplanermobile.model.BEMeal

class MealDetailsActivity : AppCompatActivity()  {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mealdetails)

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
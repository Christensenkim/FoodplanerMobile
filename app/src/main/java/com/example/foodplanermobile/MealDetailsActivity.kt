package com.example.foodplanermobile

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import com.example.foodplanermobile.model.BEMeal
import com.example.foodplanermobile.services.FoodplanerService
import com.google.gson.Gson
import io.socket.client.Socket
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage

class MealDetailsActivity : AppCompatActivity()  {
    var mSocket: Socket? = null
    val gson: Gson = Gson()

    var meal: BEMeal? = null
    var mealName: TextView? = null
    var mealDescription: TextView? = null
    var mealIngredients: TextView? = null
    var mealDirections: TextView? = null
    var edit: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mealdetails)

        val foodplanerService: FoodplanerService = application as FoodplanerService
        mSocket = foodplanerService.getMSocket()
        mSocket?.connect()

        mealName = findViewById(R.id.MealDetailName)
        mealDescription = findViewById(R.id.MealDetailDescription)
        mealIngredients = findViewById(R.id.MealDetailIngredients)
        mealDirections = findViewById(R.id.MealDetailDirections)
        edit = findViewById(R.id.editButton)

        if (intent.extras != null) {
            meal = intent.getSerializableExtra("meal") as BEMeal

            downloadImage(meal?.picName)

            mealName?.text = meal?.name
            mealDescription?.text = meal?.description
            mealIngredients?.text = meal?.ingredients
            mealDirections?.text = meal?.directions
        }

    }

    fun editMeal(view: View) {
        val intent = Intent(this, CreateMealActivity::class.java)
        intent.putExtra("meal", meal)
        startActivity(intent)
    }
}
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
import android.widget.*
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
    val storage = Firebase.storage

    var meal: BEMeal? = null
    var mealName: TextView? = null
    var mealDescription: TextView? = null
    var mealIngredients: TextView? = null
    var mealDirections: TextView? = null
    var mealPicture: ImageView? = null
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
        mealPicture = findViewById(R.id.MealPicture)
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

    fun downloadImage(picName: String?) {
        val storageRef = storage.reference
        val pathReference = storageRef.child("uploads/$picName")

        val ONE_MEGABYTE: Long = 1024 * 1024
        pathReference.getBytes(ONE_MEGABYTE)
            .addOnSuccessListener { bytes ->
                val bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                mealPicture?.setImageBitmap(bmp)
            }.addOnFailureListener {
                Log.d("TAG", "Fail")
            }
    }

    fun editMeal(view: View) {
        val intent = Intent(this, CreateMealActivity::class.java)
        intent.putExtra("meal", meal)
        startActivity(intent)
    }
}
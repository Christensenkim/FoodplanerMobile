package com.example.foodplanermobile

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
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
import com.example.foodplanermobile.model.Meal
import com.example.foodplanermobile.services.FoodplanerService
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.google.gson.Gson
import io.socket.client.Socket
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class CreateMealActivity : AppCompatActivity() {
    var mSocket: Socket? = null
    val gson: Gson = Gson()

    var newMeal = true;
    var mFile: File? = null
    private val PERMISSION_REQUEST_CODE = 1
    val CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE_BY_FILE = 101

    var meal: Meal? = null
    var mealName: TextView? = null
    var mealDescription: TextView? = null
    var mealIngredients: TextView? = null
    var mealDirections: TextView? = null
    var mealPicture: ImageButton? = null
    var save: Button? = null
    var delete: Button? = null
    val storage = Firebase.storage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_createmeal)

        val foodplanerService: FoodplanerService = application as FoodplanerService
        mSocket = foodplanerService.getMSocket()
        mSocket?.connect()

        mealName = findViewById(R.id.MealDetailName)
        mealDescription = findViewById(R.id.MealDetailDescription)
        mealIngredients = findViewById(R.id.MealDetailIngredients)
        mealDirections = findViewById(R.id.MealDetailDirections)
        mealPicture = findViewById(R.id.MealPicture)
        save = findViewById(R.id.saveButton)
        delete = findViewById(R.id.deleteButton)
        delete?.isVisible = false


        if (intent.extras != null) {
            save?.text = "Opdater"
            newMeal = false
            delete?.isVisible = true
            meal = intent.getSerializableExtra("meal") as Meal

            if (meal!!.picName != null)
            {
                downloadImage(meal?.picName)
            }
            mealName?.text = meal?.name
            mealDescription?.text = meal?.description
            mealIngredients?.text = meal?.ingredients
            mealDirections?.text = meal?.directions
        }
        checkPermissions()
    }

    fun createNewMeal(view: View) {
        val mealToSave = Meal(
            id = 0,
            name = mealName?.text.toString(),
            ingredients = mealIngredients?.text.toString(),
            directions = mealDirections?.text.toString(),
            description = mealDescription?.text.toString(),
            picName = UUID.randomUUID().toString()
        )

        uploadImage(mealToSave.picName)

        if (newMeal == true){
            val mealJson = gson.toJson(mealToSave)
            mSocket?.emit("createMeal-mobile", mealJson)
            val intent = Intent(this, MealOverviewActivity::class.java)
            startActivity(intent)
        } else {
            mealToSave.id = meal?.id!!
            val mealJson = gson.toJson(mealToSave)
            mSocket?.emit("updateMeal-mobile", mealJson)
            val intent = Intent(this, MealOverviewActivity::class.java)
            startActivity(intent)
        }
    }

    fun deleteMeal(view: View){
        mSocket?.emit("deleteMeal", meal?.id)
    }

    private fun uploadImage(picName: String) {
        var storageRef = storage.reference
        var imagesRef: StorageReference? = storageRef.child("uploads/$picName")
        var uploadTask = imagesRef?.putFile(Uri.fromFile(mFile))
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

    fun takeAPicture(view: View) {
        mFile = getOutputMediaFile("Camera01") // create a file to save the image

        if (mFile == null) {
            Toast.makeText(this, "Could not create file...", Toast.LENGTH_LONG).show()
            return
        }

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        val applicationId = "com.example.foodplanermobile"
        intent.putExtra(
            MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(
                this,
                "${applicationId}.provider",
                mFile!!))

        if (intent.resolveActivity(packageManager) != null) {
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE_BY_FILE)
        } else Log.d("TAG", "camera app could NOT be started")
    }

    private fun checkPermissions() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return
        val permissions = mutableListOf<String>()
        if ( ! isGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE) ) permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if ( ! isGranted(Manifest.permission.CAMERA) ) permissions.add(Manifest.permission.CAMERA)
        if (permissions.size > 0)
            ActivityCompat.requestPermissions(this, permissions.toTypedArray(), PERMISSION_REQUEST_CODE)
    }

    private fun isGranted(permission: String): Boolean =
        ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED

    private fun getOutputMediaFile(folder: String): File? {
        val mediaStorageDir = File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), folder)
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("TAG", "failed to create directory")
                return null
            }
        }

        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val postfix = "jpg"
        val prefix = "IMG"
        return File(mediaStorageDir.path +
                File.separator + prefix +
                "_" + timeStamp + "." + postfix)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val mImage = findViewById<ImageButton>(R.id.MealPicture)
        when (requestCode) {

            CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE_BY_FILE ->
                if (resultCode == RESULT_OK)
                    showImageFromFile(mImage, mFile!!)
                else handleOther(resultCode)
        }
    }

    private fun handleOther(resultCode: Int) {
        if (resultCode == RESULT_CANCELED)
            Toast.makeText(this, "Canceled...", Toast.LENGTH_LONG).show()
        else Toast.makeText(this, "Picture NOT taken - unknown error...", Toast.LENGTH_LONG).show()
    }

    private fun showImageFromFile(img: ImageButton, f: File) {
        img.setImageURI(Uri.fromFile(f))
    }
}
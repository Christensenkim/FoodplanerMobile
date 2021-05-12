package com.example.foodplanermobile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListView
import android.view.View
import android.widget.Toast
import com.example.foodplanermobile.model.WeekDTO
import com.example.foodplanermobile.services.FoodplanerService
import io.socket.client.IO
import io.socket.client.Socket

class MainActivity : AppCompatActivity() {

    var mSocket: Socket? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val listOfWeeks: ListView = findViewById(R.id.listOfWeeks)
        val app: FoodplanerService = application as FoodplanerService
        mSocket = app.getMSocket()
        mSocket?.connect()

        Toast.makeText(this, mSocket?.id() + "",Toast.LENGTH_SHORT).show()


        mSocket?.emit("test")
    }

    fun Tester(view: View) {
        mSocket?.emit("test")
        Toast.makeText(this, mSocket?.id() + "",Toast.LENGTH_SHORT).show()
    }

    fun MealOverview(view: View) {
        val intent = Intent(this, MealOverviewActivity::class.java)
        startActivity(intent)
    }
}
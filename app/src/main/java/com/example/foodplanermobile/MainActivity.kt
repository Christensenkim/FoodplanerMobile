package com.example.foodplanermobile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.foodplanermobile.model.WeekDTO
import com.example.foodplanermobile.services.FoodplanerService
import io.socket.client.IO

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val app: FoodplanerService = application as FoodplanerService
        val mSocket = app.getMSocket()
        mSocket?.connect()

        Toast.makeText(this, mSocket?.id() + "",Toast.LENGTH_SHORT).show()
    }
}
package com.example.foodplanermobile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.foodplanermobile.model.WeekDto
import com.example.foodplanermobile.services.FoodplanerService
import com.google.gson.Gson
import io.socket.client.Socket
import org.json.JSONArray
import java.util.ArrayList

class MainActivity : AppCompatActivity() {

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
                for (i in 0 until data.length()) {
                    val week = data[i]
                    val weekdto = gson.fromJson(week.toString(), WeekDto::class.java)
                    weeksDB.add(weekdto)
                }
                weeks = weeksDB
                weeksDB.clear()
            }
        }
    }

    fun Tester(view: View) {
        mSocket?.emit("get-weeks")
    }
}


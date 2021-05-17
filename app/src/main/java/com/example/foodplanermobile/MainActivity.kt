package com.example.foodplanermobile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ListView
import com.example.foodplanermobile.model.Week
import com.example.foodplanermobile.services.adapters.WeekOverviewAdapter
import android.view.View
import com.example.foodplanermobile.model.WeekDto
import com.example.foodplanermobile.services.FoodplanerService
import com.google.gson.Gson
import io.socket.client.Socket
import org.json.JSONArray
import java.util.ArrayList

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
        }
        mSocket?.emit("get-weeks")

        weekOverviewList = findViewById(R.id.weekOverviewList)
        while (weeks.size == 0) {
            Thread.sleep(100)
            Log.d("TAG", "venter")
        }
        listWeekAdapter = WeekOverviewAdapter(this, weeks)
        weekOverviewList.adapter = listWeekAdapter

        Log.d("TAG", "antal = " + weeks.size)

        weekOverviewList.setOnItemClickListener { parent, view, position, id ->
            val intent = Intent(this, WeekDetailedActivity::class.java)
            val weekSelect = listWeekAdapter!!.getItem(position)
            if (weekSelect != null) {
                intent.putExtra("week", weekSelect)
                startActivity(intent)
                //Toast.makeText(this,"Du har valgt uge: ${weekSelect.weekNumber}", Toast.LENGTH_LONG ).show()
            }
        }
    }

    fun createNewWeek(view: View) {
        mSocket?.emit("create-new-week")
    }

    fun updateWeek(view: View) {
        val week = Week(
            1,
            1,
            1,
            1,
            2,
            1,
            1,
            1,
        )
        val weekJson = gson.toJson(week)
        mSocket?.emit("update-week-mobile", weekJson)
    }

    fun deleteWeek(view: View) {
        val weekID = 1
        mSocket?.emit("delete-week", weekID)
    }
}
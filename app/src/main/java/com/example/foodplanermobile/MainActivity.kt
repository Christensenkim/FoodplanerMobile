package com.example.foodplanermobile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListView
import com.example.foodplanermobile.model.Week
import com.example.foodplanermobile.services.adapters.WeekOverviewAdapter


class MainActivity : AppCompatActivity() {
    lateinit var weekList: ListView
    var listWeekAdapter: WeekOverviewAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        weekList = findViewById(R.id.weekOverviewList)
        listWeekAdapter = WeekOverviewAdapter(this, Week().getAll())
        weekList.adapter = listWeekAdapter
    }

}
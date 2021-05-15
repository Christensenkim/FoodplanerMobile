package com.example.foodplanermobile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListView
import android.widget.Toast
import com.example.foodplanermobile.model.Week
import com.example.foodplanermobile.services.adapters.WeekOverviewAdapter

class MainActivity : AppCompatActivity() {
    private lateinit var weekOverviewList: ListView
    private var listWeekAdapter: WeekOverviewAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        weekOverviewList = findViewById(R.id.weekOverviewList)
        listWeekAdapter = WeekOverviewAdapter(this, Week().getAll())
        weekOverviewList.adapter = listWeekAdapter

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


}
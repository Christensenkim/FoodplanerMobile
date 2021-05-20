package com.example.foodplanermobile.model

object SelectedWeek {

    var selectedWeek: WeekDto? = null

    fun getWeek(): WeekDto? = selectedWeek

    fun setWeek(week: WeekDto?) {
        this.selectedWeek = week
    }
}
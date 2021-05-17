package com.example.foodplanermobile.model

import java.io.Serializable

class WeekDto(
    var id: Int,
    var weekNumber: Int,
    var monday: MealDto?,
    var tuesday: MealDto?,
    var wednesday: MealDto?,
    var thursday: MealDto?,
    var friday: MealDto?,
    var saturday: MealDto?,
    var sunday: MealDto?,
    var daysPlanned: Int
): Serializable
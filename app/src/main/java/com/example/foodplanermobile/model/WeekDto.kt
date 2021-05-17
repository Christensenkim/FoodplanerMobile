package com.example.foodplanermobile.model

import java.io.Serializable

class WeekDto(
    var id: Number,
    var weekNumber: Number,
    var monday: MealDto?,
    var tuesday: MealDto?,
    var wednesday: MealDto?,
    var thursday: MealDto?,
    var friday: MealDto?,
    var saturday: MealDto?,
    var sunday: MealDto?,
    var daysPlanned: Number
): Serializable
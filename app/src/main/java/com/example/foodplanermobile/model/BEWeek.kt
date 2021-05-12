package com.example.foodplanermobile.model

class BEWeek(var id: Number,
             var weekNumber: Number,
             var UserID: Number,
             var monday: BEMealOverview?,
             var tuesday: BEMealOverview?,
             var wednesday: BEMealOverview?,
             var thursday: BEMealOverview?,
             var friday: BEMealOverview?,
             var saturday: BEMealOverview?,
             var sunday: BEMealOverview?,
             var daysPlanned: Number) {
}
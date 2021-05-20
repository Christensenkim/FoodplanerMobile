package com.example.foodplanermobile.model

import java.io.Serializable

class BEWeek (var id: Number,
             var weekNumber: Number,
             var monday: Number?,
             var tuesday: Number?,
             var wednesday: Number?,
             var thursday: Number?,
             var friday: Number?,
             var saturday: Number?,
             var sunday: Number?
             ): Serializable{
}
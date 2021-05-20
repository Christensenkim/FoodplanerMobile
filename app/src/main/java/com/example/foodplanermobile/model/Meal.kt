package com.example.foodplanermobile.model

import java.io.Serializable

class Meal(var id: Number,
           var name: String,
           var ingredients: String,
           var directions: String,
           var description: String,
           var picName: String): Serializable {
}
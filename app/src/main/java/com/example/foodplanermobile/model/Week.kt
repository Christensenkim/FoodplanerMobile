package com.example.foodplanermobile.model

class Week {
    val mockWeeks = arrayOf<BEWeek>(
            BEWeek(1, 15, 1,
                    BEMealOverview(1, "Spaghetti & Kødsovs"),
                    BEMealOverview(2, "Pizza"),
                    BEMealOverview(3, "Røde Bøffer"),
                    BEMealOverview(4, "Sushi"),
                    BEMealOverview(1, "Spaghetti & Kødsovs"),
                    BEMealOverview(1, "Spaghetti & Kødsovs"),
                    BEMealOverview(1, "Spaghetti & Kødsovs"),
                    7),
            BEWeek(2, 16, 1,
                    null,
                    BEMealOverview(2, "Pizza"),
                    null,
                    BEMealOverview(4, "Sushi"),
                    BEMealOverview(1, "Spaghetti & Kødsovs"),
                    null,
                    null,
                    3)
    )

    fun getAll(): Array<BEWeek> = mockWeeks
}
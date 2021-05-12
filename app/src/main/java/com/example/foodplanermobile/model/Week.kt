package com.example.foodplanermobile.model

class Week {
    val mockWeeks = arrayOf<BEWeek>(
            BEWeek(1, 15, 1,
                    BEMealOverview(1, "mad 1"),
                    BEMealOverview(1, "mad 1"),
                    BEMealOverview(1, "mad 1"),
                    BEMealOverview(1, "mad 1"),
                    BEMealOverview(1, "mad 1"),
                    BEMealOverview(1, "mad 1"),
                    BEMealOverview(1, "mad 1"),
                    7),
            BEWeek(2, 16, 1,
                    null,
                    BEMealOverview(1, "mad 1"),
                    null,
                    BEMealOverview(1, "mad 1"),
                    BEMealOverview(1, "mad 1"),
                    null,
                    null,
                    3)
    )

    fun getAll(): Array<BEWeek> = mockWeeks
}
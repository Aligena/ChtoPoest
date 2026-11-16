package com.chtohotovit.data

/**
 * Блюдо с полным рецептом.
 *
 * @param calories калорийность на 100 г готового блюда
 * @param portionGrams вес одной порции в граммах
 */
data class Dish(
    val id: Int,
    val name: String,
    val emoji: String,
    val category: String,
    val calories: Int,
    val timeMinutes: Int,
    val difficulty: String,
    val portionGrams: Int,
    val description: String,
    val ingredients: List<String>,
    val steps: List<String>,
    val tip: String
) {
    /** Калорийность одной порции. */
    val portionCalories: Int
        get() = calories * portionGrams / 100
}

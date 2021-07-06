package com.baro.constants

import com.baro.models.Category
import org.json.JSONArray
import java.util.*
import kotlin.collections.ArrayList

enum class CategoryEnum(val unicode: String) {

    SYRINGE("\uD83D\uDC89"),
    BANDAGE("\uD83E\uDE79"),
    SOAP("\uD83E\uDDFC"),
    WATER("\uD83D\uDEB0"),
    HOSPITAL("\uD83C\uDFE5"),
    // House
    FAMILY("\uD83D\uDC6A"),
    HOUSE("\uD83C\uDFD8"),
    BRICK("\uD83E\uDDF1"),
    WHEAT("\uD83C\uDF3E"),
    COW("\uD83D\uDC04"),
    HERB("\uD83C\uDF3F"),
    // Mechanics
    TOOL("\uD83D\uDEE0"),
    MOTORBIKE("\uD83D\uDEF5"),
    CAR("\uD83D\uDE97"),
    // Other
    ART("\uD83D\uDD8C"),
    PRAY("\uD83D\uDED0");





    companion object {
        private val map = CategoryEnum.values().associateBy(CategoryEnum::unicode)
        private fun fromUnicode(type: String) = map[type]

        fun getCategoriesFromJSONArray(categoriesJSON: JSONArray): ArrayList<Category> {
            var categories = ArrayList<Category>()

            for (i in 0 until categoriesJSON.length()) {
                var categoryEnum = fromUnicode(categoriesJSON.getString(i))
                var category = Category(categoryEnum?.name, categoryEnum?.unicode)
                categories.add(category)
            }
            return categories
        }

    }

}
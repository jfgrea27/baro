package com.baro.models

class Category(var name: String, val emoji: String, var isSelected: Boolean = false) {
    override fun toString(): String {
        return emoji
    }
}

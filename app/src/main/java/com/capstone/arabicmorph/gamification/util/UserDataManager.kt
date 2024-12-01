package com.capstone.arabicmorph.gamification.util


import android.content.Context

class UserDataManager(context: Context) {

    private val sharedPreferences = context.getSharedPreferences("user_data", Context.MODE_PRIVATE)

    fun addXp(amount: Int) {
        val editor = sharedPreferences.edit()
        val currentXp = sharedPreferences.getInt("xp", 0)
        editor.putInt("xp", currentXp + amount)
        editor.apply()
    }

    fun getXp(): Int {
        return sharedPreferences.getInt("xp", 0)
    }
}
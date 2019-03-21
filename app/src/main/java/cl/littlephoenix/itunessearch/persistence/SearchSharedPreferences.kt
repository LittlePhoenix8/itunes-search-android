package cl.littlephoenix.itunessearch.persistence

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

class SearchSharedPreferences(context: Context)
{
    private val sharedPreferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)!!

    companion object
    {
        const val LAST_SEARCH = "login"
    }

    fun setLastSearch(email: String)
    {
        sharedPreferences.edit().putString(LAST_SEARCH, email).apply()
    }

    fun getLastSearch(): String?
    {
        return sharedPreferences.getString(LAST_SEARCH, null)
    }
}
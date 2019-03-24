package cl.littlephoenix.itunessearch.persistence

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

class SearchSharedPreferences(context: Context)
{
    private val sharedPreferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)!!

    companion object
    {
        const val LAST_SEARCH = "last_search"
    }

    fun setLastSearch(search: String)
    {
        sharedPreferences.edit().putString(LAST_SEARCH, search).apply()
    }

    fun getLastSearch(): String?
    {
        return sharedPreferences.getString(LAST_SEARCH, null)
    }
}
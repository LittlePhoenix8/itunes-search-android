package cl.littlephoenix.itunessearch.customviews

import android.support.v7.widget.SearchView
import cl.littlephoenix.itunessearch.interfaces.OnQueryTextListenerInstance

class QueryText
{
    private var callback: OnQueryTextListenerInstance? = null

    fun setCallBack(callback: OnQueryTextListenerInstance): QueryText
    {
        this.callback = callback
        return this
    }

    fun registerSearchView(searchView: SearchView): QueryText
    {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener
        {
            override fun onQueryTextSubmit(query: String?): Boolean
            {
                callback?.onQueryTextSubmit(query)
                return false
            }

            override fun onQueryTextChange(query: String?): Boolean
            {
                return false
            }
        })
        return this
    }
}
package cl.littlephoenix.itunessearch.interfaces

interface OnQueryTextListenerInstance
{
    fun onQueryTextSubmit(query: String?): Boolean
}
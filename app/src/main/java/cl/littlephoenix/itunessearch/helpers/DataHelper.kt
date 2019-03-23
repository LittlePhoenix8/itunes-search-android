package cl.littlephoenix.itunessearch.helpers

import cl.littlephoenix.itunessearch.models.response.DetailResponse
import cl.littlephoenix.itunessearch.models.response.SongsResponse

class DataHelper
{
    fun parseDetailData(details: Array<DetailResponse>): Array<DetailResponse>
    {
        val list = details.filter { it.wrapperType.equals("collection", true) }
        return list.toTypedArray()
    }

    fun parseSongsData(details: Array<SongsResponse>): Array<SongsResponse>
    {
        val list = details.filter { it.wrapperType.equals("track", true) }
        return list.toTypedArray()
    }

    fun parseSearchString(query: String): String
    {
        return query.replace(" ", "+", true)
    }
}
package cl.littlephoenix.itunessearch.helpers

import cl.littlephoenix.itunessearch.models.response.DetailResponse

class DataHelper
{
    fun parseDetailData(details: Array<DetailResponse>): Array<DetailResponse>
    {
        val list = details.filter { it.wrapperType.equals("collection", true) }
        return list.toTypedArray()
    }
}
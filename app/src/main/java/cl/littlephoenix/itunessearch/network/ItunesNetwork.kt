package cl.littlephoenix.itunessearch.network

import cl.littlephoenix.itunessearch.models.BaseResponse
import cl.littlephoenix.itunessearch.models.response.ArtistResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface ItunesNetwork
{
    companion object
    {
        const val URL_BASE  = "https://itunes.apple.com/"
    }

    @Headers("Content-Type:application/json")
    @GET("search")
    fun getArtistByName(@Query("term") id:String, @Query("entity") entity: String): Call<BaseResponse<ArtistResponse>>
}
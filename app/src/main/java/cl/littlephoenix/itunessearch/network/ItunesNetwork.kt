package cl.littlephoenix.itunessearch.network

import cl.littlephoenix.itunessearch.models.BaseResponse
import cl.littlephoenix.itunessearch.models.response.ArtistResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface ItunesNetwork
{
    companion object
    {
        const val URL_BASE  = "https://itunes.apple.com/"
    }

    @Headers("Content-Type:application/json")
    @GET("search?term={artist}&entity=musicArtist")
    fun getArtistByName(@Path("artist") id:String): Call<BaseResponse<ArtistResponse>>

}
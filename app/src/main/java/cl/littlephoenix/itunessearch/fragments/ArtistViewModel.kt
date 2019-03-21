package cl.littlephoenix.itunessearch.fragments

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import cl.littlephoenix.itunessearch.base.BaseViewModel
import cl.littlephoenix.itunessearch.models.BaseResponse
import cl.littlephoenix.itunessearch.models.response.ArtistResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ArtistViewModel: BaseViewModel()
{
    private val artistResponse = MutableLiveData<BaseResponse<ArtistResponse>>()
    private val errorResponse = MutableLiveData<String>()

    fun getArtists(): LiveData<BaseResponse<ArtistResponse>>
    {
        return artistResponse
    }

    fun getError(): LiveData<String>
    {
        return errorResponse
    }

    fun searchArtist(query: String)
    {
        getRetrofit().getArtistByName(query, "musicArtist").enqueue(ArtistData())
    }

    private inner class ArtistData: Callback<BaseResponse<ArtistResponse>>
    {
        override fun onResponse(call: Call<BaseResponse<ArtistResponse>>,
                                response: Response<BaseResponse<ArtistResponse>>)
        {
            if(response.isSuccessful && response.body() != null)
            {
                artistResponse.postValue(response.body())
            }
            else
            {
                errorResponse.postValue(response.message())
            }
        }

        override fun onFailure(call: Call<BaseResponse<ArtistResponse>>, t: Throwable)
        {
            errorResponse.postValue(t.message)
        }
    }
}
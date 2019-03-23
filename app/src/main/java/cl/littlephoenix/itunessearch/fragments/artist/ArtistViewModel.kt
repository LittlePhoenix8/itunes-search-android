package cl.littlephoenix.itunessearch.fragments.artist

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import cl.littlephoenix.itunessearch.base.BaseViewModel
import cl.littlephoenix.itunessearch.models.BaseResponse
import cl.littlephoenix.itunessearch.models.response.ArtistResponse
import cl.littlephoenix.itunessearch.persistence.SearchSharedPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ArtistViewModel: BaseViewModel()
{
    private val artistResponse = MutableLiveData<BaseResponse<ArtistResponse>>()
    private val errorResponse = MutableLiveData<String>()
    private val queryResponse = MutableLiveData<String>()
    private val progressResponse = MutableLiveData<Boolean>()

    fun getArtists(): LiveData<BaseResponse<ArtistResponse>>
    {
        return artistResponse
    }

    fun getError(): LiveData<String>
    {
        return errorResponse
    }

    fun savedQuery(): LiveData<String>
    {
        return queryResponse
    }

    fun showProgress(): LiveData<Boolean>
    {
        return progressResponse
    }

    fun searchArtist(query: String)
    {
        progressResponse.postValue(true)
        queryResponse.postValue(query)
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
package cl.littlephoenix.itunessearch.fragments.detail

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import cl.littlephoenix.itunessearch.base.BaseViewModel
import cl.littlephoenix.itunessearch.models.BaseResponse
import cl.littlephoenix.itunessearch.models.response.DetailResponse
import cl.littlephoenix.itunessearch.models.response.SongsResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel: BaseViewModel()
{
    private val artistResponse = MutableLiveData<BaseResponse<DetailResponse>>()
    private val songsResponse = MutableLiveData<BaseResponse<SongsResponse>>()
    private val errorResponse = MutableLiveData<String>()

    fun getArtistDetail(): LiveData<BaseResponse<DetailResponse>>
    {
        return artistResponse
    }

    fun getArtistSongs(): LiveData<BaseResponse<SongsResponse>>
    {
        return songsResponse
    }

    fun getError(): LiveData<String>
    {
        return errorResponse
    }

    fun getArtistDetail(idArtist: String)
    {
        getRetrofit().getArtistDetail(idArtist, "album", "5").enqueue(ArtistDetailResponse())
    }

    fun getArtistSongs(idArtist: String)
    {
        getRetrofit().getArtistSongs(idArtist, "song", "10").enqueue(ArtistSongsResponse())
    }

    inner class ArtistDetailResponse: Callback<BaseResponse<DetailResponse>>
    {
        override fun onResponse(call: Call<BaseResponse<DetailResponse>>, response: Response<BaseResponse<DetailResponse>>)
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

        override fun onFailure(call: Call<BaseResponse<DetailResponse>>, t: Throwable)
        {
            errorResponse.postValue(t.message)
        }
    }

    inner class ArtistSongsResponse: Callback<BaseResponse<SongsResponse>>
    {
        override fun onResponse(call: Call<BaseResponse<SongsResponse>>, response: Response<BaseResponse<SongsResponse>>)
        {
            if(response.isSuccessful && response.body() != null)
            {
                songsResponse.postValue(response.body())
            }
            else
            {
                errorResponse.postValue(response.message())
            }
        }

        override fun onFailure(call: Call<BaseResponse<SongsResponse>>, t: Throwable)
        {
            errorResponse.postValue(t.message)
        }
    }
}
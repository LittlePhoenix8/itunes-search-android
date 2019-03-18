package cl.littlephoenix.itunessearch.base

import android.arch.lifecycle.ViewModel
import cl.littlephoenix.itunessearch.network.ItunesNetwork
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

open class BaseViewModel: ViewModel()
{
    fun getRetrofit(): ItunesNetwork
    {
        val retrofit = Retrofit.Builder()
            .baseUrl(ItunesNetwork.URL_BASE)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(ItunesNetwork::class.java)
    }
}
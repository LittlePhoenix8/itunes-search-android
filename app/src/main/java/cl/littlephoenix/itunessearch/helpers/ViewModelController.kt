package cl.littlephoenix.itunessearch.helpers

import cl.littlephoenix.itunessearch.activities.MainViewModel
import cl.littlephoenix.itunessearch.fragments.artist.ArtistViewModel

class ViewModelController
{
    var mainViewModel: MainViewModel? = null
    var artistViewModel: ArtistViewModel? = null

    fun changeData(query: String)
    {
        artistViewModel?.searchArtist(query)
    }
}
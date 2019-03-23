package cl.littlephoenix.itunessearch.activities

import cl.littlephoenix.itunessearch.base.BaseViewModel
import cl.littlephoenix.itunessearch.helpers.ViewModelController

class MainViewModel: BaseViewModel()
{
    val controller = ViewModelController()

    init
    {
        controller.mainViewModel = this
    }

    fun onQuerySearch(query: String)
    {
        controller.changeData(query)
    }
}
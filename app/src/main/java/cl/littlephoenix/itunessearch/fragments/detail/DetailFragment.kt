package cl.littlephoenix.itunessearch.fragments.detail

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.arch.lifecycle.ViewModelStores
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation

import cl.littlephoenix.itunessearch.R
import cl.littlephoenix.itunessearch.adapters.DetailAdapter
import cl.littlephoenix.itunessearch.helpers.DataHelper
import cl.littlephoenix.itunessearch.models.BaseResponse
import cl.littlephoenix.itunessearch.models.response.DetailResponse
import kotlinx.android.synthetic.main.fragment_detail.*

class DetailFragment : Fragment()
{
    private lateinit var viewModel: DetailViewModel
    private val details = ArrayList<DetailResponse>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        initComponents()
    }

    private fun initComponents()
    {
        viewModel = ViewModelProviders.of(this).get(DetailViewModel::class.java)
        viewModel.getArtistDetail().observe(this, ArtistObserverResponse())
        viewModel.getError().observe(this, ErrorObserverResponse())

        recyclerDetails.layoutManager = LinearLayoutManager(context)
        recyclerDetails.adapter = DetailAdapter(details)

        showProgressBar()

        val idArtist = arguments?.getString("id_artist")
        idArtist?.let {
            viewModel.getArtistDetail(it)
        }?: run {
            showToastMessage(getString(R.string.error_id))
            navigateUp()
        }
    }

    private fun showProgressBar()
    {
        progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar()
    {
        progressBar.visibility = View.GONE
    }

    private fun showToastMessage(message: String)
    {
        Toast.makeText(context!!, message, Toast.LENGTH_SHORT).show()
    }

    private fun navigateUp()
    {
        view?.let {
            val navController = Navigation.findNavController(it)
            navController.navigateUp()
        }
    }

    inner class ArtistObserverResponse: Observer<BaseResponse<DetailResponse>>
    {
        override fun onChanged(t: BaseResponse<DetailResponse>?)
        {
            ViewModelStores.of(this@DetailFragment).clear()
            hideProgressBar()
            t?.results?.let {
                details.clear()
                details.addAll(DataHelper().parseDetailData(it))
                recyclerDetails.adapter?.notifyDataSetChanged()
            }
        }
    }

    inner class ErrorObserverResponse: Observer<String>
    {
        override fun onChanged(t: String?)
        {
            ViewModelStores.of(this@DetailFragment).clear()
            hideProgressBar()
            t?.let {
                showToastMessage(it)
            }
        }
    }
}

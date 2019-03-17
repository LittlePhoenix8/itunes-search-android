package cl.littlephoenix.itunessearch.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import cl.littlephoenix.itunessearch.R
import cl.littlephoenix.itunessearch.models.response.ArtistResponse

class ArtistAdapter(private val artists: Array<ArtistResponse>): RecyclerView.Adapter<ArtistAdapter.ArtistHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtistHolder
    {
        return ArtistHolder(LayoutInflater.from(parent.context).inflate(R.layout.artist_row, parent, false))
    }

    override fun getItemCount(): Int = artists.size

    override fun onBindViewHolder(holder: ArtistHolder, position: Int)
    {
        holder.txtType.text = artists[position].artistType
        holder.txtName.text = artists[position].artistName
        holder.txtGenre.text = artists[position].primaryGenreName
        //TODO on click listener
    }

    inner class ArtistHolder(view: View): RecyclerView.ViewHolder(view)
    {
        val txtType = view.findViewById<TextView>(R.id.txtType)!!
        val txtName = view.findViewById<TextView>(R.id.txtName)!!
        val txtGenre = view.findViewById<TextView>(R.id.txtGenre)!!
    }
}
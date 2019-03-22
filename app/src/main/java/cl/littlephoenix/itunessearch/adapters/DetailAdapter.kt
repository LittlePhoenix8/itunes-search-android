package cl.littlephoenix.itunessearch.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import cl.littlephoenix.itunessearch.R
import cl.littlephoenix.itunessearch.models.response.DetailResponse

class DetailAdapter(private val details: ArrayList<DetailResponse>): RecyclerView.Adapter<DetailAdapter.DetailHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailHolder
    {
        return DetailHolder(LayoutInflater.from(parent.context).inflate(R.layout.detail_row, parent, false))
    }

    override fun getItemCount(): Int = details.size

    override fun onBindViewHolder(holder: DetailHolder, position: Int)
    {
        //TODO glide images
        holder.txtName.text = details[position].collectionName
        holder.txtCopyRight.text = details[position].copyright
        holder.txtGenre.text = details[position].primaryGenreName
    }

    inner class DetailHolder(view: View): RecyclerView.ViewHolder(view)
    {
        val imgCover = view.findViewById<ImageView>(R.id.imgCover)!!
        val txtName = view.findViewById<TextView>(R.id.txtName)!!
        val txtCopyRight = view.findViewById<TextView>(R.id.txtCopyRight)!!
        val txtGenre = view.findViewById<TextView>(R.id.txtGenre)!!
    }
}
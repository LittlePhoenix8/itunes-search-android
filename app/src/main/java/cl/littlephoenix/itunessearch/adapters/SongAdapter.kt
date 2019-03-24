package cl.littlephoenix.itunessearch.adapters

import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import cl.littlephoenix.itunessearch.R
import cl.littlephoenix.itunessearch.helpers.DataHelper
import cl.littlephoenix.itunessearch.interfaces.OnSongSelectListener
import cl.littlephoenix.itunessearch.models.response.SongsResponse
import com.bumptech.glide.Glide

class SongAdapter(private val songs: ArrayList<SongsResponse>,
                  private val onSongSelectListener: OnSongSelectListener): RecyclerView.Adapter<SongAdapter.SongHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongHolder
    {
        return SongHolder(LayoutInflater.from(parent.context).inflate(R.layout.song_row, parent, false))
    }

    override fun getItemCount(): Int = songs.size

    override fun onBindViewHolder(holder: SongHolder, position: Int)
    {
        Glide.with(holder.imgCover.context).load(songs[position].artworkUrl60).centerCrop().into(holder.imgCover)
        holder.txtSongName.text = songs[position].trackName
        holder.txtAlbumName.text = songs[position].collectionName
        holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.context,
            DataHelper().getSongBackgroundColor(songs[position].isPlaying)))
        holder.itemView.setOnClickListener {
            onSongSelectListener.playSongAt(position)
        }
    }

    inner class SongHolder(view: View): RecyclerView.ViewHolder(view)
    {
        val imgCover = view.findViewById<ImageView>(R.id.imgCover)!!
        val txtSongName = view.findViewById<TextView>(R.id.txtSongName)!!
        val txtAlbumName = view.findViewById<TextView>(R.id.txtAlbumName)!!
    }
}
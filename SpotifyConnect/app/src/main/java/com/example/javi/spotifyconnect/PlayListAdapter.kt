package com.example.javi.spotifyconnect

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import kaaes.spotify.webapi.android.models.Image

class PlayListAdapter(val ctx : Context, val data : List<String>, val images : List<Image>) : BaseAdapter() {

    private val inflater = ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = inflater.inflate(R.layout.entry, parent, false)

        val tv = view.findViewById<TextView>(R.id.playlistName)

        tv.text = data[position]

        val imageView = view.findViewById<ImageView>(R.id.trackImage)

        Picasso.get().load(images[position].url).into(imageView);

        Log.d("SPOTIFY", images[position].url )

        return view
    }

    override fun getItem(position: Int): Any {
        return data[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return data.size
    }


}
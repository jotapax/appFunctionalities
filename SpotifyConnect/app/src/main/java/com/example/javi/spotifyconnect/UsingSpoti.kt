package com.example.javi.spotifyconnect

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.TextView
import kaaes.spotify.webapi.android.SpotifyApi
import kaaes.spotify.webapi.android.SpotifyCallback
import kaaes.spotify.webapi.android.SpotifyError
import kaaes.spotify.webapi.android.SpotifyService
import kaaes.spotify.webapi.android.models.Image
import kaaes.spotify.webapi.android.models.Pager
import kaaes.spotify.webapi.android.models.PlaylistSimple
import kotlinx.android.synthetic.main.player.*
import kotlinx.android.synthetic.main.using_spoty.*
import org.jetbrains.anko.toast
import retrofit.client.Response

/*
    MY FIRST EXAMPLE

    spotify.getMe(object : SpotifyCallback<UserPrivate>() {
        override fun success(user: UserPrivate, response: Response) {
            toast("${user.birthdate}")
        }

        override fun failure(error: SpotifyError) {
            toast("${error.errorDetails.message}")
        }
    })
 */

class UsingSpoti() : AppCompatActivity() {

    lateinit var spotify : SpotifyService
    lateinit var playlist : MutableList<PlaylistSimple>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.using_spoty)
        val authtoken = intent.getStringExtra("com.example.javi.spotifyconnect.authtoken")
        setServiceAPI(authtoken)

        spotify.getMyPlaylists(object : SpotifyCallback<Pager<PlaylistSimple>>() {
            override fun success(pager: Pager<PlaylistSimple>?, response: Response?) {
                playlist = pager?.items!!

                val playlistNames = playlist.map {it.name}

                val playlistDesc = playlist.map{it.images[0]}

                showList(playlistNames, playlistDesc)
            }

            override fun failure(error: SpotifyError?) {
                toast("${error?.errorDetails?.message}")
            }
        })

        listview.setOnItemClickListener{ _, _, position, _ ->
            itemSelected(position)
        }

    }

    private fun setServiceAPI(auth_token : String) {
        Log.d("SPOTIFY", "Setting Spotify API Service")
        val api = SpotifyApi()
        api.setAccessToken(auth_token)
        spotify = api.service
    }

    private fun showList(playlistNames : List<String>, playlistImages : List<Image>) {
        val adapter = PlayListAdapter(this, playlistNames, playlistImages)
        listview.adapter = adapter
    }

    private fun itemSelected(position : Int) {
        val intent = Intent(this, Player::class.java)
        intent.putExtra("com.example.javi.spotifyconnect.uriPlaylist", playlist[position].uri)
        startActivity(intent)
    }

}
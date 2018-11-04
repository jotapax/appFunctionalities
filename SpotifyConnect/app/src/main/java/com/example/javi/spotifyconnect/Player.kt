package com.example.javi.spotifyconnect

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector
import com.spotify.android.appremote.api.SpotifyAppRemote
import kotlinx.android.synthetic.main.player.*
import org.jetbrains.anko.toast

class Player : AppCompatActivity(), Connector.ConnectionListener {

    private val CLIENT_ID = "e6fd3a91c16341bfbc94966a8251d4e6"
    private val REDIRECT_URI = "com.example.javi.spotifyconnect://callback"

    lateinit var remote : SpotifyAppRemote
    lateinit var uri : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.player)

        uri = intent.getStringExtra("com.example.javi.spotifyconnect.uriPlaylist")

        getAppRemote()

        play.setOnClickListener{
            remote.playerApi.resume()
        }

        stop.setOnClickListener{
            remote.playerApi.pause()
        }

        prev.setOnClickListener{
            remote.playerApi.skipPrevious()
            setInfo()
        }

        next.setOnClickListener{
            remote.playerApi.skipNext()
            setInfo()
        }
    }

    private fun getAppRemote() {
        val connectionParams = ConnectionParams.Builder(CLIENT_ID)
                .setRedirectUri(REDIRECT_URI).build()

        SpotifyAppRemote.connect(this, connectionParams, this)
    }

    override fun onConnected(remote : SpotifyAppRemote?) {
        this.remote = remote!!
        remote.playerApi.play(uri)

        play.isEnabled = true
        stop.isEnabled = true
        prev.isEnabled = true
        next.isEnabled = true

        setInfo()
    }

    private fun setInfo() {
        Thread.sleep(500)
        remote.playerApi.playerState.setResultCallback {
            remote.imagesApi.getImage(it.track.imageUri).setResultCallback {
                trackImage.setImageBitmap(it)
            }
            trackTitle.text = it.track.name
            trackArtist.text = it.track.artist.name
        }
    }

    override fun onFailure(error : Throwable?) {
        toast(error?.message.toString())
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        SpotifyAppRemote.disconnect(remote)
    }
}
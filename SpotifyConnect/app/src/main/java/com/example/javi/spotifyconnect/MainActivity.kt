package com.example.javi.spotifyconnect

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.spotify.sdk.android.authentication.AuthenticationClient
import com.spotify.sdk.android.authentication.AuthenticationRequest
import com.spotify.sdk.android.authentication.AuthenticationResponse
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.toast

/*
    com.example.javi.spotifyconnect://callback

    Android packages
    com.example.javi.spotifyconnect
    96:AD:10:D0:11:38:10:26:39:D1:77:17:04:71:17:B2:8C:E2:78:95
 */

/*
    IMPORTANT STEPS BEFORE CODING

    IMPORT .AAR FILES
    REGISTER APP IN SPOTIFY DEVELOPERS DASHBOARD
    SET FINGERPRINTS
    ADD LOGINACTIVITY TO MANIFEST
    INCLUDE REDIRECT_URI IN WHITELIST
 */

/*
     MAIN ACTIITY ONLY FUNCTIONALITY : GET ACCESS TOKEN

     improvement -> store it in database

     in case it doesnt work, then you open a login activity
 */

class MainActivity : AppCompatActivity() {

    private val CLIENT_ID = "e6fd3a91c16341bfbc94966a8251d4e6"
    private val REDIRECT_URI = "com.example.javi.spotifyconnect://callback"
    private val REQUEST_CODE_SPOTIFY = 133

    private val scopes = arrayOf("streaming", "app-remote-control", "user-read-birthdate",
            "user-read-private", "playlist-read-private")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        logIn.setOnClickListener{
            logIn()
        }
    }

    private fun logIn() {

        val builder = AuthenticationRequest.Builder(CLIENT_ID,
                AuthenticationResponse.Type.TOKEN, REDIRECT_URI)

        builder.setScopes(scopes)

        val request = builder.build()

        AuthenticationClient.openLoginActivity(this, REQUEST_CODE_SPOTIFY, request)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if ( requestCode == REQUEST_CODE_SPOTIFY ) {

            val response = AuthenticationClient.getResponse(resultCode, data)

            when (response.type) {

                AuthenticationResponse.Type.TOKEN -> nextStep(response.accessToken)

                AuthenticationResponse.Type.ERROR -> toast("Error on authentication: ${response.error}")

                else -> toast("Authentication not completed")
            }
        }
    }

    private fun nextStep(authtoken : String) {
        val intent = Intent(this, UsingSpoti::class.java)
        intent.putExtra("com.example.javi.spotifyconnect.authtoken", authtoken)
        startActivity(intent)
        finish()
    }

}

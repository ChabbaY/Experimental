package cloud.englert.experimental.ui.spotify

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.fragment.app.Fragment

import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector
import com.spotify.android.appremote.api.SpotifyAppRemote
import com.spotify.protocol.types.Track
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationRequest
import com.spotify.sdk.android.auth.AuthorizationResponse

import cloud.englert.experimental.databinding.FragmentSpotifyBinding

class SpotifyFragment : Fragment() {
    private var _binding: FragmentSpotifyBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    private val clientId = "94d87ade19dc4506a8659158aba38f98"
    private val redirectUri = "https://chabbay.de"
    private var spotifyAppRemote: SpotifyAppRemote? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSpotifyBinding.inflate(inflater, container, false)
        val root: View = binding.root

        login()

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()

        connect()
    }

    private fun connect() {
        val connectionParams = ConnectionParams.Builder(clientId)
            .setRedirectUri(redirectUri)
            .showAuthView(true)
            .build()

        SpotifyAppRemote.connect(requireContext(), connectionParams,
            object : Connector.ConnectionListener {
                override fun onConnected(appRemote: SpotifyAppRemote) {
                    spotifyAppRemote = appRemote
                    Log.d(TAG, "Connected! Yay!")
                    // Now you can start interacting with App Remote
                    connected()
                }

                override fun onFailure(throwable: Throwable) {
                    Log.e(TAG, throwable.message, throwable)
                    // Something went wrong when attempting to connect! Handle errors here
                }
            })
    }

    private fun connected() {
        play()
    }

    override fun onPause() {
        super.onPause()
        spotifyAppRemote?.let {
            SpotifyAppRemote.disconnect(it)
        }
    }

    private fun login() {
        // Request code will be used to verify if result comes from the login activity.
        // Can be set to any integer.
        val authRequestCode = 1337

        val builder: AuthorizationRequest.Builder =
            AuthorizationRequest.Builder(clientId, AuthorizationResponse.Type.CODE,
                redirectUri)

        builder.setScopes(arrayOf("streaming"))
        val request: AuthorizationRequest = builder.build()

        AuthorizationClient.openLoginActivity(requireActivity(), authRequestCode,
            request)
    }

    private fun play() {
        spotifyAppRemote?.let { it ->
            // Play a playlist
            val playlistURI = "spotify:playlist:37i9dQZF1DX2sUQwD7tbmL"
            it.playerApi.play(playlistURI)
            // Subscribe to PlayerState
            it.playerApi.subscribeToPlayerState().setEventCallback {
                val track: Track = it.track
                val trackString = track.name + " by " + track.artist.name
                Log.d(TAG, trackString)
                binding.textSongName.text = trackString
            }
        }
    }

    companion object {
        private const val TAG = "SpotifyFragment"
    }
}
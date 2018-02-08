package foo.bar.musicplayer

import android.app.Application

import foo.bar.musicplayer.network.SpotifyApiClient

class MusicPlayerApplication : Application() {
  val apiClient = SpotifyApiClient()
}

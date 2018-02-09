package foo.bar.musicplayer.data

import foo.bar.musicplayer.data.network.SpotifyApiClient
import foo.bar.musicplayer.data.network.SpotifyAuthClient
import foo.bar.musicplayer.data.network.responses.ArtistSearchResponse
import foo.bar.musicplayer.util.StringUtilities
import io.reactivex.Single
import io.reactivex.functions.Function
import java.io.UnsupportedEncodingException
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by evin on 2/8/18.
 */

@Singleton
class SpotifyRepository @Inject constructor(private val spotifyApiClient: SpotifyApiClient,
                                            private val spotifyAuthClient: SpotifyAuthClient) {

  fun retrieveArtists(searchTerm: String): Single<ArtistSearchResponse> {
    return spotifyApiClient.doArtistSearch(searchTerm)
  }
}

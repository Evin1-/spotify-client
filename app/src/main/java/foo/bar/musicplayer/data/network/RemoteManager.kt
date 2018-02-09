package foo.bar.musicplayer.data.network

import foo.bar.musicplayer.data.network.responses.ArtistSearchResponse
import foo.bar.musicplayer.data.network.responses.TokenResponse
import foo.bar.musicplayer.util.Constants
import io.reactivex.Single
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by evin on 2/9/18.
 */
@Singleton
class RemoteManager @Inject constructor(private val spotifyApiService: SpotifyService.SpotifyApiService,
                                        private val spotifyAuthService: SpotifyService.SpotifyAuthService) {

  fun retrieveArtists(searchTerm: String, headersMap: Map<String, String>): Single<ArtistSearchResponse> {
    return spotifyApiService.search(searchTerm, Constants.TYPE_ARTISTS, headersMap)
        .flatMap {
          when (it.code()) {
            200 -> Single.just(it.body())
            else -> Single.error(Exception("Unauthorized"))
          }
        }
  }

  fun refreshToken(): Single<Response<TokenResponse>> {
    return spotifyAuthService.refreshToken("client_credentials")
  }
}
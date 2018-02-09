package foo.bar.musicplayer.data.network

import android.util.Base64
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
            in 400..499 -> Single.error(UNAUTHORIZED_EXCEPTION)
            else -> Single.error(UNEXPECTED_EXCEPTION)
          }
        }
  }

  fun refreshToken(): Single<Response<TokenResponse>> {
    return spotifyAuthService.refreshToken("client_credentials", buildBase64Key())
  }

  private fun buildBase64Key(): String {
    val stringToEncode = "$CLIENT_ID:$CLIENT_SECRET"
    val stringEncoded = Base64.encodeToString(stringToEncode.toByteArray(), Base64.NO_WRAP)
    return "Basic $stringEncoded"
  }

  companion object {
    const val UNAUTHORIZED_MESSAGE = "Unauthorized, please try refreshing token!"
    const val UNEXPECTED_MESSAGE = "Unexpected error happened, please try again later!"

    const val CLIENT_ID = "104c48bc7eaa456f9377222a4731abd7"
    const val CLIENT_SECRET = "3ef577ee13024d67adc56539a9bf1f5c"

    val UNAUTHORIZED_EXCEPTION = Exception(UNAUTHORIZED_MESSAGE)
    val UNEXPECTED_EXCEPTION = Exception(UNEXPECTED_MESSAGE)
  }
}
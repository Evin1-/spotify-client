package foo.bar.musicplayer.data.network

import java.io.UnsupportedEncodingException

import foo.bar.musicplayer.model.Artists
import foo.bar.musicplayer.data.network.responses.ArtistSearchResponse
import foo.bar.musicplayer.data.network.responses.TopTracksResponse
import foo.bar.musicplayer.util.Constants
import foo.bar.musicplayer.util.StringUtilities
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import javax.inject.Inject

class SpotifyApiClient @Inject constructor() {

  interface SpotifyWebServices {
    @Headers("Authorization: Bearer BQDte0VSenjIjZWsqt7XLBIS5cV3UUsQ_TEpV-Zc8T4aPohCczUG7rAf3nz3VEQc_QlaQV0RtHJtF-HJmTk", "Accept: application/json")
    @GET("/v1/search")
    fun search(@Query("q") query: String?, @Query("type") type: String): Single<Response<ArtistSearchResponse>>

    @GET("/v1/artists/{id}/top-tracks")
    fun topTracks(@Path("id") id: String, @Query("country") country: String): Single<Response<TopTracksResponse>>
  }

  fun doArtistSearch(searchTerm: String): Single<ArtistSearchResponse> {
    return SpotifyApiClient.get()!!.search(searchTerm, Constants.TYPE_ARTISTS)
        .flatMap {
          when (it.code()) {
            200 -> Single.just(it.body())
            else -> Single.error(Exception("Unauthorized"))
          }
        }
  }

  fun doTopTracks(artistId: String) {
    SpotifyApiClient.get()!!.topTracks(artistId, Constants.COUNTRY_CODE)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(Consumer { })
  }

  companion object {
    private const val API_BASE_URL = "https://api.spotify.com/"
    private var SPOTIFY_WEB_SERVICES: SpotifyWebServices? = null

    init {
      setupServiceHelper()
    }

    fun get(): SpotifyWebServices? {
      return SPOTIFY_WEB_SERVICES
    }

    private fun setupServiceHelper() {
      val logging = HttpLoggingInterceptor()
      logging.level = HttpLoggingInterceptor.Level.BODY

      val httpClient = OkHttpClient.Builder()
      httpClient.addInterceptor(logging)

      val retrofit = Retrofit.Builder()
          .baseUrl(API_BASE_URL)
          .addConverterFactory(GsonConverterFactory.create())
          .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
          .client(httpClient.build())
          .build()

      SPOTIFY_WEB_SERVICES = retrofit.create(SpotifyWebServices::class.java)
    }
  }
}

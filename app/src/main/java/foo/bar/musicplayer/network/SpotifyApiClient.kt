package foo.bar.musicplayer.network

import java.io.UnsupportedEncodingException

import foo.bar.musicplayer.model.Artists
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
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

class SpotifyApiClient {

  interface SpotifyWebServices {
    @Headers("Authorization: Bearer BQAc2Sv5zaHhUKiW0DMV8DlgNNPc_Puau6jyiOTpSaCjw8wCkZ_QgM3IB9pli2QokRVAIRhY0oxaJ5YC_YU", "Accept: application/json")
    @GET("v1/search")
    fun search(@Query("q") query: String?, @Query("type") type: String): Single<Response<ArtistSearchResponse>>

    @GET("v1/artists/{id}/top-tracks")
    fun topTracks(@Path("id") id: String, @Query("country") country: String): Single<Response<TopTracksResponse>>
  }

  fun doArtistSearch(searchTerm: String, consumer: Consumer<Artists>) {
    var encodedSearch: String? = null
    try {
      encodedSearch = StringUtilities.makeUrlEncoded(searchTerm)
    } catch (e: UnsupportedEncodingException) {
      e.printStackTrace()
    }

    SpotifyApiClient.get()!!.search(encodedSearch, Constants.TYPE_ARTISTS)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe({ artistSearchResponseResponse ->
          if (artistSearchResponseResponse.isSuccessful && artistSearchResponseResponse.code() == 200) {
            Single.just(artistSearchResponseResponse.body()!!.artists)
                .subscribe(consumer)
          }
        }) { throwable ->

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

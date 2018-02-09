package foo.bar.musicplayer.data.network

import foo.bar.musicplayer.data.network.responses.ArtistSearchResponse
import foo.bar.musicplayer.data.network.responses.TokenResponse
import foo.bar.musicplayer.data.network.responses.TopTracksResponse
import io.reactivex.Single
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

/**
 * Created by evin on 2/9/18.
 */
interface SpotifyService {

  interface SpotifyApiService {
    @GET("/v1/search")
    fun search(@Query("q") query: String?, @Query("type") type: String,
               @HeaderMap headersMap: Map<String, String>): Single<Response<ArtistSearchResponse>>

    @GET("/v1/artists/{id}/top-tracks")
    fun topTracks(@Path("id") id: String, @Query("country") country: String,
                  @HeaderMap headersMap: Map<String, String>): Single<Response<TopTracksResponse>>
  }

  interface SpotifyAuthService {
    @POST("/api/token")
    @FormUrlEncoded
    fun refreshToken(@Field("grant_type") grantType: String,
                     @Header("Authorization") authorization: String): Single<Response<TokenResponse>>
  }

  companion object Factory {
    const val BASE_API_URL = "https://api.spotify.com/"
    const val BASE_AUTH_URL = "https://accounts.spotify.com/"

    fun createApiInterceptor(): Interceptor {
      val interceptor = HttpLoggingInterceptor()
      interceptor.level = HttpLoggingInterceptor.Level.BODY
      return interceptor
    }

    fun createOkHttp(interceptor: Interceptor): OkHttpClient {
      return OkHttpClient.Builder()
          .addInterceptor(interceptor)
          .build()
    }

    fun createRetrofit(okHttpClient: OkHttpClient, baseUrl: String): Retrofit {
      return Retrofit.Builder()
          .baseUrl(baseUrl)
          .client(okHttpClient)
          .addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync())
          .addConverterFactory(GsonConverterFactory.create())
          .build()
    }

    fun createApiService(retrofit: Retrofit): SpotifyApiService = retrofit.create(SpotifyApiService::class.java)

    fun createAuthService(retrofit: Retrofit): SpotifyAuthService = retrofit.create(SpotifyAuthService::class.java)
  }
}
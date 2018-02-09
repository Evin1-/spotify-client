package foo.bar.musicplayer.di

import dagger.Module
import dagger.Provides
import foo.bar.musicplayer.data.network.SpotifyService
import foo.bar.musicplayer.di.qualifier.Api
import foo.bar.musicplayer.di.qualifier.Auth
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit

/**
 * Created by evin on 2/8/18.
 */
@Module
class DataModule {

  @Provides
  fun provideApiInterceptor(): Interceptor = SpotifyService.Factory.createApiInterceptor()

  @Provides
  fun provideOkHttpClient(interceptor: Interceptor): OkHttpClient = SpotifyService.Factory.createOkHttp(interceptor)

  @Provides
  @Api
  fun provideApiRetrofit(okHttpClient: OkHttpClient): Retrofit = SpotifyService.Factory.createRetrofit(okHttpClient, SpotifyService.BASE_API_URL)

  @Provides
  @Auth
  fun provideAuthRetrofit(okHttpClient: OkHttpClient): Retrofit = SpotifyService.Factory.createRetrofit(okHttpClient, SpotifyService.BASE_AUTH_URL)

  @Provides
  fun provideApiService(@Api retrofit: Retrofit): SpotifyService.SpotifyApiService = SpotifyService.createApiService(retrofit)

  @Provides
  fun provideAuthService(@Auth retrofit: Retrofit): SpotifyService.SpotifyAuthService = SpotifyService.createAuthService(retrofit)
}
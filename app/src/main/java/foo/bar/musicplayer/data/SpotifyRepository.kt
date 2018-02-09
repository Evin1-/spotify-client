package foo.bar.musicplayer.data

import foo.bar.musicplayer.data.local.CacheManager
import foo.bar.musicplayer.data.network.RemoteManager
import foo.bar.musicplayer.model.Artist
import foo.bar.musicplayer.util.rx.SchedulerProvider
import foo.bar.musicplayer.util.ExtensionUtils.swapThreadJumpBack
import io.reactivex.Single
import io.reactivex.functions.Consumer
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by evin on 2/8/18.
 */

@Singleton
class SpotifyRepository @Inject constructor(private val cacheManager: CacheManager,
                                            private val remoteManager: RemoteManager,
                                            private val schedulerProvider: SchedulerProvider) {

  private var tokenAuth: String? = null

  fun addConsumer(consumer: Consumer<List<Artist>>) {
    cacheManager.addConsumer(consumer)
  }

  fun removeConsumer(consumer: Consumer<List<Artist>>) {
    cacheManager.removeConsumer(consumer)
  }

  fun retrieveArtists(searchTerm: String): Single<List<Artist>> {
    if (tokenAuth == null) {
      refreshToken(searchTerm, Exception(RemoteManager.UNAUTHORIZED_MESSAGE))
    } else {
      val headersMap = buildHeadersMap()
      remoteArtistSingle(searchTerm, headersMap)
          ?.swapThreadJumpBack(schedulerProvider)
          ?.subscribe({ cacheManager.updateCacheData(searchTerm, it) }, { refreshToken(searchTerm, it) })
    }

    return Single.just(cacheManager.retrieveDataInCache(searchTerm))
  }

  private fun remoteArtistSingle(searchTerm: String, headersMap: Map<String, String>): Single<MutableList<Artist>>? {
    return when (tokenAuth) {
      null -> Single.error(RemoteManager.UNAUTHORIZED_EXCEPTION)
      else -> remoteManager.retrieveArtists(searchTerm, headersMap)
          .flatMap { Single.just(it.artists.items) }
          .subscribeOn(schedulerProvider.io())
          .observeOn(schedulerProvider.ui())
    }
  }

  private fun refreshToken(searchTerm: String, it: Throwable) {
    if (it.message != RemoteManager.UNAUTHORIZED_MESSAGE) {
      return
    }
    remoteManager.refreshToken()
        .doOnEvent({ response, throwable ->
          if (throwable == null && response.isSuccessful) {
            tokenAuth = response.body()?.accessToken ?: ""
          }
        })
        .flatMap { remoteArtistSingle(searchTerm, buildHeadersMap()) }
        .swapThreadJumpBack(schedulerProvider)
        .subscribe({ cacheManager.updateCacheData(searchTerm, it) }, { it.printStackTrace() })
  }

  private fun buildHeadersMap(): Map<String, String> {
    val map = mutableMapOf<String, String>()
    map["Authorization"] = "Bearer " + tokenAuth
    map["Accept"] = "application/json"
    return map
  }
}

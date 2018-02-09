package foo.bar.musicplayer.data

import foo.bar.musicplayer.data.local.CacheManager
import foo.bar.musicplayer.data.network.RemoteManager
import foo.bar.musicplayer.model.Artist
import foo.bar.musicplayer.util.rx.SchedulerProvider
import io.reactivex.Single
import io.reactivex.functions.Consumer
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by evin on 2/8/18.
 */

@Singleton
class SpotifyRepository @Inject constructor(private val cacheManager: CacheManager,
                                            private val remoteManager: RemoteManager,
                                            private val schedulerProvider: SchedulerProvider) {

  private var tokenAuth = ""

  fun addConsumer(consumer: Consumer<List<Artist>>) {
    cacheManager.addConsumer(consumer)
  }

  fun removeConsumer(consumer: Consumer<List<Artist>>) {
    cacheManager.removeConsumer(consumer)
  }

  fun retrieveArtists(searchTerm: String): Single<List<Artist>> {

    singleRemote(searchTerm, buildHeadersMap()).subscribeOn(schedulerProvider.io())
        .observeOn(schedulerProvider.ui())
        .subscribe({ cacheManager.updateCacheData(searchTerm, it) }, { refreshToken(searchTerm) })

    return Single.just(cacheManager.retrieveDataInCache(searchTerm))
  }

  private fun singleRemote(searchTerm: String, headersMap: Map<String, String>) = remoteManager.retrieveArtists(searchTerm, headersMap)
      .flatMap { Single.just(it.artists.items) }
      .subscribeOn(schedulerProvider.io())
      .observeOn(schedulerProvider.ui())

  private fun refreshToken(searchTerm: String) {
    remoteManager.refreshToken()
        .doOnEvent({ response, throwable ->
          if (throwable == null && response.isSuccessful) {
            tokenAuth = response.body()?.accessToken ?: ""
          }
        })
        .flatMap { singleRemote(searchTerm, buildHeadersMap()) }
        .subscribeOn(schedulerProvider.io())
        .observeOn(schedulerProvider.ui())
        .subscribe({ cacheManager.updateCacheData(searchTerm, it) }, { it.printStackTrace() })
  }

  private fun buildHeadersMap(): Map<String, String> {
    val map = mutableMapOf<String, String>()
    map["Authorization"] = "Bearer " + tokenAuth
    map["Accept"] = "application/json"
    return map
  }
}

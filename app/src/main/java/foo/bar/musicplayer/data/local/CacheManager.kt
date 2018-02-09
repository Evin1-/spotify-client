package foo.bar.musicplayer.data.local

import android.util.LruCache
import foo.bar.musicplayer.model.Artist
import io.reactivex.Single
import io.reactivex.functions.Consumer
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by evin on 2/8/18.
 */

@Singleton
class CacheManager @Inject constructor() {
  private val consumers = mutableSetOf<Consumer<List<Artist>>>()
  private val savedArtists = LruCache<String, MutableList<Artist>>(cacheSize)

  companion object {
    private const val cacheSize: Int = 1 * 1024 * 1024 // 1MB
  }

  fun addConsumer(consumer: Consumer<List<Artist>>) {
    consumers.add(consumer)
  }

  fun removeConsumer(consumer: Consumer<List<Artist>>) {
    consumers.remove(consumer)
  }

  fun updateCacheData(searchTerm: String, artists: List<Artist>) {
    val mutableList = savedArtists[searchTerm] ?: mutableListOf<Artist>()
    if (!artists.isEmpty()) {
      mutableList.clear()
      mutableList.addAll(artists)
      savedArtists.put(searchTerm, mutableList)
    }

    updateObservers(searchTerm)
  }

  fun retrieveDataInCache(searchTerm: String) = savedArtists[searchTerm] ?: emptyList<Artist>()

  private fun updateObservers(searchTerm: String) {
    val dataInCache = retrieveDataInCache(searchTerm)
    consumers.forEach { Single.just(dataInCache).subscribe(it) }
  }
}
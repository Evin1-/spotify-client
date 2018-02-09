package foo.bar.musicplayer.util

import foo.bar.musicplayer.model.Artist
import foo.bar.musicplayer.ui.search.SearchListContract
import foo.bar.musicplayer.ui.search.SearchListPresenter
import foo.bar.musicplayer.util.rx.SchedulerProvider
import io.reactivex.Single

/**
 * Created by evin on 2/9/18.
 */
object ExtensionUtils {
  fun <T> Single<T>.swapThreadJumpBack(schedulerProvider: SchedulerProvider): Single<T> {
    this.subscribeOn(schedulerProvider.io())
    this.observeOn(schedulerProvider.ui())
    return this
  }

  fun List<Artist>.sortedByPopularity(order: Int): List<Artist>? {
    if (order == SearchListPresenter.ORDER_ASC) {
      return this.sortedBy { it.popularity }
    }
    return this.sortedByDescending { it.popularity }
  }

  fun Int.toggleOrder(): Int {
    if (this == SearchListPresenter.ORDER_DESC) {
      return SearchListPresenter.ORDER_ASC
    }
    return SearchListPresenter.ORDER_DESC
  }
}
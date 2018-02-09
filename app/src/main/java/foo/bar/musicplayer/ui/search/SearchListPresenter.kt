package foo.bar.musicplayer.ui.search

import android.arch.lifecycle.MutableLiveData
import foo.bar.musicplayer.data.SpotifyRepository
import foo.bar.musicplayer.di.scopes.PerView
import foo.bar.musicplayer.model.Artist
import foo.bar.musicplayer.util.StringUtils
import foo.bar.musicplayer.util.rx.SchedulerProvider
import foo.bar.musicplayer.util.ExtensionUtils.swapThreadJumpBack
import foo.bar.musicplayer.util.ExtensionUtils.sortedByPopularity
import foo.bar.musicplayer.util.ExtensionUtils.toggleOrder
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import io.reactivex.functions.Function
import java.util.*
import javax.inject.Inject

/**
 * Created by evin on 2/8/18.
 */
@PerView
class SearchListPresenter @Inject constructor(private val spotifyRepository: SpotifyRepository,
                                              private val schedulerProvider: SchedulerProvider) : SearchListContract.Presenter {

  private var view: SearchListContract.View? = null
  private val compositeDisposable = CompositeDisposable()
  private val consumer = Consumer<List<Artist>> { refreshView(it) }
  private val liveData = MutableLiveData<List<Artist>>()
  private var order = ORDER_DESC

  override fun attachView(view: SearchListContract.View) {
    this.view = view
    spotifyRepository.addConsumer(consumer)
  }

  override fun detachView() {
    this.view = null
    spotifyRepository.removeConsumer(consumer)
    compositeDisposable.dispose()
  }

  override fun loadData(searchTerm: String) {
    view?.showProgress()
    StringUtils.makeUrlEncoded(searchTerm)
        .doOnError { handleError("Couldn't encode that string!") }
        .flatMap { spotifyRepository.retrieveArtists(searchTerm) }
        .swapThreadJumpBack(schedulerProvider)
        .subscribe({ refreshView(it) }, {
          it.printStackTrace()
          handleError("Couldn't retrieve items from cache!")
        })
  }

  private fun handleError(error: String) {
    view?.showError(error)
    view?.hideProgress()
  }

  private fun refreshView(results: List<Artist>) {
    liveData.value = results.sortedByPopularity(order)
    view?.hideProgress()
  }

  fun getLiveData(): MutableLiveData<List<Artist>> {
    return liveData
  }

  override fun toggleSortData() {
    order = order.toggleOrder()

    if (order == ORDER_DESC) {
      view?.showDescendingOrderIcon()
    } else {
      view?.showAscendingOrderIcon()
    }

    val artists = liveData.value ?: emptyList()
    refreshView(artists)
  }

  companion object {
    const val ORDER_ASC = 1
    const val ORDER_DESC = 2
  }
}
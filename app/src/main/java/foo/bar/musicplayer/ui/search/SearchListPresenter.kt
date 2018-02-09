package foo.bar.musicplayer.ui.search

import android.arch.lifecycle.MutableLiveData
import foo.bar.musicplayer.data.SpotifyRepository
import foo.bar.musicplayer.di.scopes.PerView
import foo.bar.musicplayer.model.Artist
import foo.bar.musicplayer.util.AppLogger
import foo.bar.musicplayer.util.ExtensionUtils.sortedByPopularity
import foo.bar.musicplayer.util.ExtensionUtils.swapThreadJumpBack
import foo.bar.musicplayer.util.ExtensionUtils.toggleOrder
import foo.bar.musicplayer.util.StringUtils
import foo.bar.musicplayer.util.rx.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import javax.inject.Inject

/**
 * Created by evin on 2/8/18.
 */
@PerView
class SearchListPresenter @Inject constructor(private val spotifyRepository: SpotifyRepository,
                                              private val schedulerProvider: SchedulerProvider) : SearchListContract.Presenter {
  private var view: SearchListContract.View? = null
  private val compositeDisposable = CompositeDisposable()
  private val consumer = Consumer<List<Artist>> { refreshData(it) }

  private val liveData = MutableLiveData<List<Artist>>()
  private var order = ORDER_DESC
  private var searchTerm: String? = null
  private var max: Int? = null
  private var min: Int? = null

  override fun attachView(view: SearchListContract.View) {
    this.view = view
    spotifyRepository.addConsumer(consumer)
  }

  override fun detachView() {
    this.view = null
    spotifyRepository.removeConsumer(consumer)
    compositeDisposable.dispose()
  }

  override fun loadDataRemotely(searchTerm: String) {
    this.searchTerm = searchTerm
    view?.showProgress()
    StringUtils.makeUrlEncoded(searchTerm)
        .doOnError { handleError("Couldn't encode that string!") }
        .flatMap { spotifyRepository.retrieveArtists(searchTerm) }
        .swapThreadJumpBack(schedulerProvider)
        .subscribe({
          refreshData(it)
        }, {
          it.printStackTrace()
          handleError("Couldn't retrieve items from cache!")
        })
  }

  override fun toggleSortData(min: Int?, max: Int?) {
    val minValue = min ?: 0
    val maxValue = max ?: 0
    order = order.toggleOrder()

    if (order == ORDER_DESC) {
      view?.showDescendingOrderIcon()
    } else {
      view?.showAscendingOrderIcon()
    }

    loadDataFiltered(minValue, maxValue)
  }

  override fun loadDataFiltered(min: Int, max: Int) {
    spotifyRepository.retrieveArtistsFromCache(searchTerm)
        .swapThreadJumpBack(schedulerProvider)
        .subscribe { artists ->
          getFilterRanges(artists)
          val filteredList = artists.filter {
            val popularity = it.popularity?.toInt() ?: -1
            popularity in min..max
          }
          refreshView(filteredList)
        }
  }

  override fun triggerFilterView(currentMin: Int?, currentMax: Int?) {
    val minValue = min ?: 0
    val maxValue = max ?: 0
    view?.showFilterFragment(minValue, maxValue,
        currentMin ?: minValue, currentMax ?: maxValue)
  }

  override fun loadDataFromCache(searchTerm: String, currentMin: Int?, currentMax: Int?) {
    this.searchTerm = searchTerm
    loadDataFiltered(currentMin ?: min ?: Int.MIN_VALUE, currentMax ?: max ?: Int.MAX_VALUE)
  }

  override fun setCurrentOrder(currentOrder: Int) {
    this.order = currentOrder

    if (order == ORDER_DESC) {
      view?.showDescendingOrderIcon()
    } else {
      view?.showAscendingOrderIcon()
    }
  }

  private fun getFilterRanges(artists: List<Artist>) {
    val sortedByPopularity = artists.sortedByPopularity(ORDER_DESC)
    max = sortedByPopularity?.firstOrNull()?.popularity?.toInt() ?: 0
    min = sortedByPopularity?.lastOrNull()?.popularity?.toInt() ?: 0

    if (min == null || max == null) {
      view?.showError("Couldn't get ranges in this set!")
    }
  }

  private fun refreshData(artists: List<Artist>) {
    getFilterRanges(artists)
    refreshView(artists)
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

  companion object {
    const val ORDER_ASC = 1
    const val ORDER_DESC = 2
  }
}
package foo.bar.musicplayer.ui.search

import android.arch.lifecycle.MutableLiveData
import foo.bar.musicplayer.data.SpotifyRepository
import foo.bar.musicplayer.di.scopes.PerView
import foo.bar.musicplayer.model.Artist
import foo.bar.musicplayer.util.StringUtilities
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
  private val consumer = Consumer<List<Artist>> { refreshView(it) }
  private val liveData = MutableLiveData<List<Artist>>()

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
    StringUtilities.makeUrlEncoded(searchTerm)
        .doOnError { handleError("Couldn't encode that string!") }
        .flatMap { spotifyRepository.retrieveArtists(searchTerm) }
        .subscribeOn(schedulerProvider.io())
        .observeOn(schedulerProvider.ui())
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
    liveData.value = results
    view?.hideProgress()
  }

  fun getLiveData(): MutableLiveData<List<Artist>> {
    return liveData
  }
}
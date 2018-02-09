package foo.bar.musicplayer.ui.search

import foo.bar.musicplayer.data.SpotifyRepository
import foo.bar.musicplayer.di.scopes.PerView
import foo.bar.musicplayer.model.Artist
import foo.bar.musicplayer.util.StringUtilities
import foo.bar.musicplayer.util.rx.SchedulerProvider
import io.reactivex.Single
import javax.inject.Inject

/**
 * Created by evin on 2/8/18.
 */
@PerView
class SearchListPresenter @Inject constructor(private val spotifyRepository: SpotifyRepository,
                                              private val schedulerProvider: SchedulerProvider) : SearchListContract.Presenter {
  var view: SearchListContract.View? = null

  override fun attachView(view: SearchListContract.View) {
    this.view = view
  }

  override fun detachView() {
    this.view = null
  }

  override fun loadData(searchTerm: String) {
    view?.showProgress()
    StringUtilities.makeUrlEncoded(searchTerm)
        .doOnError { handleError("Couldn't encode that string!") }
        .flatMap { spotifyRepository.retrieveArtists(it) }
        .flatMap { Single.just(it.artists.items) }
        .subscribeOn(schedulerProvider.io())
        .observeOn(schedulerProvider.ui())
        .subscribe({ refreshView(it) }, {
          it.printStackTrace()
          handleError("Couldn't download the information!")
        })
  }

  private fun handleError(error: String) {
    view?.showError(error)
    view?.hideProgress()
  }

  private fun refreshView(results: List<Artist>) {
    view?.showData(results)
    view?.hideProgress()
  }
}
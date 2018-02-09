package foo.bar.musicplayer.ui.search

import foo.bar.musicplayer.model.Artist

/**
 * Created by evin on 2/8/18.
 */
interface SearchListContract {
  interface View {
    fun showProgress()
    fun hideProgress()
    fun showError(error: String)
    fun showData(artists: List<Artist>)
    fun showDescendingOrderIcon()
    fun showAscendingOrderIcon()
  }

  interface Presenter {
    fun attachView(view: View)
    fun detachView()
    fun loadData(searchTerm: String)
    fun toggleSortData()
  }
}
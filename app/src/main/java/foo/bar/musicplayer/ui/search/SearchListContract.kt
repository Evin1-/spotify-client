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
    fun showFilterFragment(min: Int, max: Int, currentMin: Int, currentMax: Int)
  }

  interface Presenter {
    fun attachView(view: View)
    fun detachView()
    fun loadDataRemotely(searchTerm: String)
    fun loadDataFromCache(searchTerm: String, currentMin: Int?, currentMax: Int?)
    fun loadDataFiltered(min: Int, max: Int)
    fun toggleSortData()
    fun triggerFilterView(currentMin: Int?, currentMax: Int?)
    fun setCurrentOrder(currentOrder: Int)
  }
}
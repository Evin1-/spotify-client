package foo.bar.musicplayer.ui.search

import android.view.View
import foo.bar.musicplayer.model.Artist
import foo.bar.musicplayer.model.Artists

/**
 * Created by evin on 2/8/18.
 */
interface SearchListContract {
  interface View {
    fun showProgress()
    fun hideProgress()
    fun showError(error: String)
    fun showData(artists: List<Artist>)
  }

  interface Presenter {
    fun attachView(view: View)
    fun detachView()
    fun loadData(searchTerm: String)
  }
}
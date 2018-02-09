package foo.bar.musicplayer.ui.search

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import foo.bar.musicplayer.MusicPlayerApplication
import foo.bar.musicplayer.R
import foo.bar.musicplayer.model.Artist
import foo.bar.musicplayer.ui.search.di.DaggerSearchListComponent
import kotlinx.android.synthetic.main.fragment_search.*
import javax.inject.Inject

class SearchListFragment : Fragment(), SearchListContract.View {

  private var searchListAdapter: SearchListAdapter? = null

  @Inject
  lateinit var searchListPresenter: SearchListPresenter

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    injectDependencies()
    searchListPresenter.attachView(this)
  }

  override fun onDestroy() {
    super.onDestroy()
    searchListPresenter.detachView()
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.fragment_search, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    initViews()
    searchListPresenter.loadData("Michael")
  }

  override fun showData(artists: List<Artist>) {
    searchListAdapter?.setSearchResults(artists)
  }

  override fun showProgress() {
    f_search_progress.visibility = View.VISIBLE
  }

  override fun hideProgress() {
    f_search_progress.visibility = View.INVISIBLE
  }

  override fun showError(error: String) {
    Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
  }

  private fun initViews() {
    searchListAdapter = SearchListAdapter()
    f_search_recycler.adapter = searchListAdapter
    f_search_recycler.layoutManager = LinearLayoutManager(context)
  }

  private fun injectDependencies() {
    val mainComponent = (activity?.application as MusicPlayerApplication).mainComponent
    if (mainComponent != null) {
      DaggerSearchListComponent.builder()
          .mainComponent(mainComponent)
          .build()
          .inject(this)
    }
  }

  companion object {
    private val TAG = SearchListFragment::class.java.simpleName
  }
}

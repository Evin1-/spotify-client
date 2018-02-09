package foo.bar.musicplayer.ui.search

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.content.ContextCompat.getDrawable
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import android.widget.Toast

import foo.bar.musicplayer.MusicPlayerApplication
import foo.bar.musicplayer.R
import foo.bar.musicplayer.model.Artist
import foo.bar.musicplayer.ui.search.di.DaggerSearchListComponent
import kotlinx.android.synthetic.main.fragment_search.*
import javax.inject.Inject

class SearchListFragment : Fragment(), SearchListContract.View {

  @Inject
  lateinit var searchListPresenter: SearchListPresenter

  private var searchListAdapter: SearchListAdapter? = null
  private var menu: Menu? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setHasOptionsMenu(true);
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
    subscribeLiveData()
    searchListPresenter.loadData("Michael")
  }

  override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
    this.menu = menu
    super.onCreateOptionsMenu(menu, inflater)
  }

  override fun onOptionsItemSelected(item: MenuItem?): Boolean {
    when (item?.itemId) {
      R.id.m_main_filter -> TODO()
      R.id.m_main_order -> searchListPresenter.toggleSortData()
      else -> return super.onOptionsItemSelected(item)
    }
    return true
  }

  override fun showData(artists: List<Artist>) {
    refreshAdapter(artists)
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

  override fun showAscendingOrderIcon() {
    menu?.findItem(R.id.m_main_order)
        ?.icon = context?.let { getDrawable(it, R.drawable.ic_trending_down_white_24dp) }
  }

  override fun showDescendingOrderIcon() {
    menu?.findItem(R.id.m_main_order)
        ?.icon = context?.let { getDrawable(it, R.drawable.ic_trending_up_white_24dp) }
  }

  private fun subscribeLiveData() {
    searchListPresenter.getLiveData()
        .observe(this, Observer { refreshAdapter(it) })
  }

  private fun refreshAdapter(artists: List<Artist>?) {
    searchListAdapter?.setSearchResults(artists ?: emptyList())
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

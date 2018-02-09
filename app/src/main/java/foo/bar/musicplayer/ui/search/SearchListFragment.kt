package foo.bar.musicplayer.ui.search

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat.getDrawable
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import android.widget.Toast

import foo.bar.musicplayer.MusicPlayerApplication
import foo.bar.musicplayer.R
import foo.bar.musicplayer.model.Artist
import foo.bar.musicplayer.ui.filter.FilterFragment
import foo.bar.musicplayer.ui.search.di.DaggerSearchListComponent
import kotlinx.android.synthetic.main.fragment_search.*
import javax.inject.Inject

class SearchListFragment : Fragment(), SearchListContract.View, FilterFragment.FilterCallback {

  @Inject
  lateinit var searchListPresenter: SearchListPresenter

  private var searchListAdapter: SearchListAdapter? = null
  private var menu: Menu? = null

  private var currentMin: Int? = null
  private var currentMax: Int? = null
  private var currentOrder = SearchListPresenter.ORDER_DESC

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setHasOptionsMenu(true);
    injectDependencies()
    searchListPresenter.attachView(this)
    savedInstanceState?.let { loadSavedState(it) }
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
    searchListPresenter.setCurrentOrder(currentOrder)
    if (savedInstanceState == null) {
      searchListPresenter.loadDataRemotely("Michael")
    } else {
      searchListPresenter.loadDataFromCache("Michael", currentMin, currentMax)
    }
  }

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    currentMin?.let { outState.putInt(KEY_MIN_BUNDLE, it) }
    currentMax?.let { outState.putInt(KEY_MAX_BUNDLE, it) }
    outState.putInt(KEY_ORDER_BUNDLE, currentOrder)
  }

  override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
    this.menu = menu
    super.onCreateOptionsMenu(menu, inflater)
  }

  override fun onOptionsItemSelected(item: MenuItem?): Boolean {
    when (item?.itemId) {
      R.id.m_main_filter -> searchListPresenter.triggerFilterView(currentMin, currentMax)
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
    currentOrder = SearchListPresenter.ORDER_ASC
  }

  override fun showDescendingOrderIcon() {
    menu?.findItem(R.id.m_main_order)
        ?.icon = context?.let { getDrawable(it, R.drawable.ic_trending_up_white_24dp) }
    currentOrder = SearchListPresenter.ORDER_DESC
  }

  override fun showFilterFragment(min: Int, max: Int, currentMin: Int, currentMax: Int) {
    val filterFragment = FilterFragment.newInstance(min, max, currentMin, currentMax)
    filterFragment.show(childFragmentManager, FILTER_FRAGMENT_TAG)
  }

  override fun onRangesSelected(min: Int, max: Int) {
    currentMin = min
    currentMax = max
    searchListPresenter.loadDataFiltered(min, max)
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

  private fun loadSavedState(savedInstanceState: Bundle) {
    if (savedInstanceState.containsKey(KEY_MIN_BUNDLE)) {
      currentMin = savedInstanceState.getInt(KEY_MIN_BUNDLE)
    }
    if (savedInstanceState.containsKey(KEY_MAX_BUNDLE)) {
      currentMax = savedInstanceState.getInt(KEY_MAX_BUNDLE)
    }
    currentOrder = savedInstanceState.getInt(KEY_ORDER_BUNDLE, SearchListPresenter.ORDER_DESC)
  }

  companion object {
    private val TAG = SearchListFragment::class.java.simpleName
    private const val FILTER_FRAGMENT_TAG = "FILTER_FRAGMENT_TAG";
    private const val KEY_MIN_BUNDLE = "KEY_MIN_BUNDLE";
    private const val KEY_MAX_BUNDLE = "KEY_MAX_BUNDLE";
    private const val KEY_ORDER_BUNDLE = "KEY_ORDER_BUNDLE";
  }
}

package foo.bar.musicplayer.ui.search

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import foo.bar.musicplayer.MusicPlayerApplication
import foo.bar.musicplayer.R
import foo.bar.musicplayer.model.Artists
import io.reactivex.functions.Consumer

class SearchListFragment : Fragment() {

  private var searchListAdapter: SearchListAdapter? = null

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    val v = inflater.inflate(R.layout.fragment_searchlist, container, false)
    val recyclerView = v.findViewById<RecyclerView>(R.id.recycler_view)
    searchListAdapter = SearchListAdapter()
    recyclerView.adapter = searchListAdapter
    fetchArtistSearch("Michael")
    return v
  }

  private fun fetchArtistSearch(query: String) {
    val application = activity!!.application as MusicPlayerApplication
    application.apiClient.doArtistSearch(query, Consumer { searchListAdapter?.setSearchResults(it) })
  }

  companion object {
    private val TAG = SearchListFragment::class.java.simpleName
  }
}

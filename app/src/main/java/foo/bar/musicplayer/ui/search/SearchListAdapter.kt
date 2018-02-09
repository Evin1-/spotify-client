package foo.bar.musicplayer.ui.search

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import foo.bar.musicplayer.R
import foo.bar.musicplayer.model.Artist

class SearchListAdapter : RecyclerView.Adapter<SearchListAdapter.ListingViewHolder>() {

  private var artists = mutableListOf<Artist>()

  fun setSearchResults(artists: List<Artist>) {
    this.artists.clear()
    this.artists.addAll(artists)
    notifyDataSetChanged()
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchListAdapter.ListingViewHolder {
    val layoutInflater = LayoutInflater.from(parent.context)
    val view = layoutInflater.inflate(R.layout.artist_search_result_item, parent, false)
    return ListingViewHolder(view)
  }

  override fun onBindViewHolder(holder: SearchListAdapter.ListingViewHolder, position: Int) {
    holder.bind(artists[position])
  }

  override fun getItemCount() = artists.size


  class ListingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val artistName: TextView = itemView.findViewById<View>(R.id.artist_name) as TextView

    fun bind(artist: Artist) {
      artistName.text = artist.name
    }
  }

  companion object {
    private val TAG = SearchListAdapter::class.java.simpleName
  }
}

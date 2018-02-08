package foo.bar.musicplayer.ui.home

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

import foo.bar.musicplayer.R
import foo.bar.musicplayer.ui.search.SearchListFragment

class HomeActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_home)

    showSearchListFragment()
  }

  private fun showSearchListFragment() {
    if (supportFragmentManager.findFragmentByTag(SEARCHLIST_FRAGMENT_TAG) == null) {
      supportFragmentManager
          .beginTransaction()
          .replace(R.id.container, SearchListFragment(), SEARCHLIST_FRAGMENT_TAG)
          .commit()
    }
  }

  companion object {
    private val SEARCHLIST_FRAGMENT_TAG = "fragment_searchlist"
  }
}

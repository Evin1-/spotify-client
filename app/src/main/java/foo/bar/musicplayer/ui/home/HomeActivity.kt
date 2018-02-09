package foo.bar.musicplayer.ui.home

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import foo.bar.musicplayer.R


class HomeActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_home)
  }

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    val inflater = menuInflater
    inflater.inflate(R.menu.main_menu, menu)
    return true
  }
}

package foo.bar.musicplayer

import android.app.Application

import foo.bar.musicplayer.di.DaggerMainComponent
import foo.bar.musicplayer.di.DataModule
import foo.bar.musicplayer.di.MainComponent
import foo.bar.musicplayer.di.RxModule
import foo.bar.musicplayer.util.AppLogger

class MusicPlayerApplication : Application() {

  var mainComponent: MainComponent? = null
    private set

  override fun onCreate() {
    super.onCreate()

    AppLogger.init()

    mainComponent = DaggerMainComponent.builder()
        .dataModule(DataModule())
        .rxModule(RxModule())
        .build()
  }
}

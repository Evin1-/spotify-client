package foo.bar.musicplayer.util

import android.annotation.SuppressLint
import foo.bar.musicplayer.BuildConfig
import timber.log.Timber

/**
 * Created by evin on 2/8/18.
 */
@SuppressLint("TimberExceptionLogging")
object AppLogger {

  fun init() {
    if (BuildConfig.DEBUG) {
      Timber.plant(Timber.DebugTree())
    }
  }

  fun d(s: String, vararg objects: Any) {
    Timber.d(s, *objects)
  }

  fun d(throwable: Throwable, s: String, vararg objects: Any) {
    Timber.d(throwable, s, *objects)
  }

  fun i(s: String, vararg objects: Any) {
    Timber.i(s, *objects)
  }

  fun i(throwable: Throwable, s: String, vararg objects: Any) {
    Timber.i(throwable, s, *objects)
  }

  fun w(s: String, vararg objects: Any) {
    Timber.w(s, *objects)
  }

  fun w(throwable: Throwable, s: String, vararg objects: Any) {
    Timber.w(throwable, s, *objects)
  }

  fun e(s: String, vararg objects: Any) {
    Timber.e(s, *objects)
  }

  fun e(throwable: Throwable, s: String, vararg objects: Any) {
    Timber.e(throwable, s, *objects)
  }
}
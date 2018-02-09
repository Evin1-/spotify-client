package foo.bar.musicplayer.util.rx

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by evin on 2/8/18.
 */
object AppSchedulerProvider : SchedulerProvider {
  override fun ui(): Scheduler = AndroidSchedulers.mainThread()
  override fun computation(): Scheduler = Schedulers.computation()
  override fun io(): Scheduler = Schedulers.io()
}
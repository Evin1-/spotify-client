package foo.bar.musicplayer.di

import dagger.Component
import foo.bar.musicplayer.data.SpotifyRepository
import foo.bar.musicplayer.util.rx.SchedulerProvider
import javax.inject.Singleton

/**
 * Created by evin on 2/8/18.
 */
@Singleton
@Component(modules = [(DataModule::class), (RxModule::class)])
interface MainComponent {
  fun spotifyRepository(): SpotifyRepository
  fun schedulerProvider(): SchedulerProvider
}
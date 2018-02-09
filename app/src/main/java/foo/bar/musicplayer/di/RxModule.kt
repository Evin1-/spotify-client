package foo.bar.musicplayer.di;

import dagger.Module
import dagger.Provides
import foo.bar.musicplayer.util.rx.AppSchedulerProvider
import foo.bar.musicplayer.util.rx.SchedulerProvider
import javax.inject.Singleton

@Module
class RxModule {

  @Singleton
  @Provides
  fun createSchedulerProvider(): SchedulerProvider {
    return AppSchedulerProvider
  }
}
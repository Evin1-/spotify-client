package foo.bar.musicplayer.ui.search.di

import dagger.Component
import foo.bar.musicplayer.di.MainComponent
import foo.bar.musicplayer.di.scopes.PerView
import foo.bar.musicplayer.ui.search.SearchListFragment

/**
 * Created by evin on 2/8/18.
 */
@PerView
@Component(dependencies = [MainComponent::class])
interface SearchListComponent {
  fun inject(searchListFragment: SearchListFragment)
}
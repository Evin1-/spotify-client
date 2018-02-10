package foo.bar.musicplayer.ui

import com.nhaarman.mockito_kotlin.*
import foo.bar.musicplayer.data.SpotifyRepository
import foo.bar.musicplayer.model.Artist
import foo.bar.musicplayer.ui.search.SearchListContract
import foo.bar.musicplayer.ui.search.SearchListPresenter
import foo.bar.musicplayer.util.rx.SchedulerProvider
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.util.*
import android.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.Rule
import org.junit.rules.TestRule


/**
 * Created by evin on 2/9/18.
 */

class SearchListPresenterUnitTest {

  @get:Rule
  val rule: TestRule = InstantTaskExecutorRule()

  private var searchListPresenter: SearchListPresenter? = null
  private val searchTerm: String = "Michael"

  private val spotifyRepository = mock<SpotifyRepository> {
    on { retrieveArtistsFromCache(searchTerm) } doReturn (Single.just(Arrays.asList()))
    on { retrieveArtists(searchTerm) } doReturn (Single.just(Arrays.asList()))
  }

  private val view = mock<SearchListContract.View> {}

  private val schedulerProvider = mock<SchedulerProvider> {
    on { ui() } doReturn (Schedulers.trampoline())
    on { io() } doReturn (Schedulers.trampoline())
  }

  @Before
  fun setUp() {
    searchListPresenter = SearchListPresenter(spotifyRepository, schedulerProvider)
  }

  @After
  fun tearDown() {
    searchListPresenter = null
  }

  @Test
  fun `onLoadData_shouldShowProgress`() {
    searchListPresenter?.attachView(view)
    searchListPresenter?.loadDataRemotely(searchTerm)

    verify(spotifyRepository).retrieveArtists(searchTerm)
    verify(view).showProgress()
    verify(view).hideProgress()
  }
}
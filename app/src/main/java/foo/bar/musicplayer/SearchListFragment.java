package foo.bar.musicplayer;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.otto.Subscribe;

import foo.bar.musicplayer.event.ArtistSearchEvent;
import foo.bar.musicplayer.event.BusProvider;
import foo.bar.musicplayer.network.ArtistSearchResponse;

public class SearchListFragment extends Fragment {
    private static final String TAG = SearchListFragment.class.getSimpleName();

    private SearchListAdapter searchListAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_searchlist, container, false);
        RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        searchListAdapter = new SearchListAdapter(this.getContext());
        recyclerView.setAdapter(searchListAdapter);
        fetchArtistSearch("Michael");
        BusProvider.getInstance().register(this);
        return v;
    }

    @Override
    public void onStop() {
        super.onStop();
        BusProvider.getInstance().unregister(this);
    }

    @Subscribe
    public void onArtistSearchEvent(ArtistSearchEvent event) {
        ArtistSearchResponse searchResponse = event.response;
        searchListAdapter.setSearchResults(searchResponse.getArtists());
    }

    private void fetchArtistSearch(String query) {
        MusicPlayerApplication application = (MusicPlayerApplication) getActivity().getApplication();
        application.service().doArtistSearch(query);
    }
}

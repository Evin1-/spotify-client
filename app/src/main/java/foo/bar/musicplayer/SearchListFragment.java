package foo.bar.musicplayer;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import foo.bar.musicplayer.model.Artists;
import io.reactivex.functions.Consumer;

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
        return v;
    }

    private void fetchArtistSearch(String query) {
        MusicPlayerApplication application = (MusicPlayerApplication) getActivity().getApplication();
        application.service().doArtistSearch(query, new Consumer<Artists>() {
            @Override
            public void accept(Artists artists) throws Exception {
                searchListAdapter.setSearchResults(artists);
            }
        });
    }
}

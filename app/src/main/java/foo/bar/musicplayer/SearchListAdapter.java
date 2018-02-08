package foo.bar.musicplayer;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import foo.bar.musicplayer.model.Artist;
import foo.bar.musicplayer.model.Artists;

public class SearchListAdapter extends RecyclerView.Adapter<SearchListAdapter.ListingViewHolder> {

    private static final String TAG = SearchListAdapter.class.getSimpleName();

    private Artists artists = new Artists();

    private Context context;

    public SearchListAdapter(Context context) {
        this.context = context;
    }

    public void setSearchResults(Artists artists) {
        this.artists = artists;
        notifyDataSetChanged();
    }

    @Override
    public SearchListAdapter.ListingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ListingViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.artist_search_result_item, parent, false));
    }

    @Override
    public void onBindViewHolder(SearchListAdapter.ListingViewHolder holder, int position) {
        holder.bind(artists.getItems().get(position));
    }

    @Override
    public int getItemCount() {
        if (artists != null) {
            return artists.getItems().size();
        } else {
            return 0;
        }
    }

    static class ListingViewHolder extends RecyclerView.ViewHolder {

        private TextView artistName;
        public ListingViewHolder(View itemView) {
            super(itemView);
            artistName = (TextView) itemView.findViewById(R.id.artist_name);
        }

        public void bind(Artist artist){
            artistName.setText(artist.getName());
        }
    }
}

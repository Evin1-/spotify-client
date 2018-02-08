package foo.bar.musicplayer.model;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel
public class Tracks {
    List<Track> items;
    int total;

    public Tracks() {
        this.items = new ArrayList<Track>();
    }

    public List<Track> getItems() {
        return items;
    }

    public int getTotal() {
        return total;
    }
}

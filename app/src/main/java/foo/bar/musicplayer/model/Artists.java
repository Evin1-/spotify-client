package foo.bar.musicplayer.model;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel
public class Artists {
    List<Artist> items;

    public Artists() {
        this.items = new ArrayList<Artist>();
    }

    public List<Artist> getItems() {
        return items;
    }
}

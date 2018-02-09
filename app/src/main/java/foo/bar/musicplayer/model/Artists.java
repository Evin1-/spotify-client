package foo.bar.musicplayer.model;

import java.util.ArrayList;
import java.util.List;

public class Artists {
    List<Artist> items;

    public Artists() {
        this.items = new ArrayList<Artist>();
    }

    public List<Artist> getItems() {
        return items;
    }
}

package foo.bar.musicplayer.model;

import org.parceler.Parcel;

import java.io.Serializable;

@Parcel
public class Artist implements Serializable {
    String id;
    String name;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}

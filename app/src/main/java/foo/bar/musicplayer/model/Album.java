package foo.bar.musicplayer.model;

import org.parceler.Parcel;

import java.io.Serializable;

@Parcel
public class Album implements Serializable {
    String id;
    String href;
    String name;
    String uri;

    public String getName() {
        return name;
    }

    public String getUri() {
        return uri;
    }

    public String getId() {
        return id;
    }

    public String getHref() {
        return href;
    }
}

package foo.bar.musicplayer.model;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.io.Serializable;

@Parcel
public class Track implements Serializable {
    String name;
    String uri;
    String id;
    String href;
    int index;
    @SerializedName("preview_url")
    String previewUrl;
    Album album;

    public int getIndex() {
        return index;
    }

    public void setIndex(int idx) {
        this.index = idx;
    }

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

    public String getPreviewUrl() {
        return previewUrl;
    }

    public Album getAlbum() {
        return album;
    }
}

package foo.bar.musicplayer.network;

import org.parceler.Parcel;

import foo.bar.musicplayer.model.Artists;

@Parcel
public class ArtistSearchResponse {
    private Artists artists;

    public Artists getArtists() {
        return artists;
    }

    public void setArtists(Artists artists) {
        this.artists = artists;
    }
}

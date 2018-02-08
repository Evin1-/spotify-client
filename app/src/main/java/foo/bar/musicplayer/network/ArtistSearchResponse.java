package foo.bar.musicplayer.network;

import foo.bar.musicplayer.model.Artists;

import org.parceler.Parcel;

@Parcel
public class ArtistSearchResponse {
    Artists artists;

    public Artists getArtists() {
        return artists;
    }

    public void setArtists(Artists artists) {
        this.artists = artists;
    }
}

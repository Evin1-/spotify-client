package foo.bar.musicplayer.event;

import foo.bar.musicplayer.network.ArtistSearchResponse;

public class ArtistSearchEvent {

    public ArtistSearchResponse response;

    public ArtistSearchEvent(ArtistSearchResponse searchResponse) {
        this.response = searchResponse;
    }
}

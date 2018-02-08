package foo.bar.musicplayer.event;

import foo.bar.musicplayer.network.TopTracksResponse;

public class TopTracksEvent {

    public TopTracksResponse response;

    public TopTracksEvent(TopTracksResponse searchResponse) {
        this.response = searchResponse;
    }
}

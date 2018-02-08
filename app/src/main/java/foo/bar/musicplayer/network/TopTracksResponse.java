package foo.bar.musicplayer.network;

import org.parceler.Parcel;

import java.util.List;

import foo.bar.musicplayer.model.Track;

@Parcel
public class TopTracksResponse {
    private List<Track> tracks;

    public List<Track> getTracks() {
        return tracks;
    }

    public void setTracks(List<Track> tracks) {
        this.tracks = tracks;
    }
}

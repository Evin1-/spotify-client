package foo.bar.musicplayer.network;

import foo.bar.musicplayer.model.Track;

import org.parceler.Parcel;

import java.util.List;

@Parcel
public class TopTracksResponse {
    List<Track> tracks;

    public List<Track> getTracks() {
        return tracks;
    }

    public void setTracks(List<Track> tracks) {
        this.tracks = tracks;
    }
}

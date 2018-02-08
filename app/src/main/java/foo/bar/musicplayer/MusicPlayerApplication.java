package foo.bar.musicplayer;

import android.app.Application;

import foo.bar.musicplayer.network.SpotifyApiClient;

public class MusicPlayerApplication extends Application {

    private final SpotifyApiClient apiClient = new SpotifyApiClient();

    public SpotifyApiClient service() {
        return apiClient;
    }
}

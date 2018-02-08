package foo.bar.musicplayer.network;

import android.util.Log;

import foo.bar.musicplayer.event.BusProvider;
import foo.bar.musicplayer.event.ArtistSearchEvent;
import foo.bar.musicplayer.event.TopTracksEvent;
import foo.bar.musicplayer.util.Constants;
import foo.bar.musicplayer.util.StringUtilities;

import java.io.UnsupportedEncodingException;

import com.squareup.otto.Produce;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

public class SpotifyApiClient {
  private static final String TAG = SpotifyApiClient.class.getSimpleName();
  private static final String API_BASE_URL = "https://api.spotify.com/";
  private static SpotifyWebServices SPOTIFY_WEB_SERVICES;

  static {
    setupServiceHelper();
  }

  interface SpotifyWebServices {
    @Headers({
        "Authorization: Bearer BQCX3aQPiY5IKjI2QuFX9VAGoRPbVM4GPizyhuvJb9guwxlnDFe1e5mZ3EsiDR4nksfnj7i-twh5CbnDtkMnOafFKzyQaqdSH_s5iGPzYQiM5kk4W7N4IiW6aPFOEvA7nhBRHIHRF73y",
        "Accept: application/json"
    })
    @GET("v1/search")
    Call<ArtistSearchResponse> search(
        @Query("q") String query,
        @Query("type") String type);

    @GET("v1/artists/{id}/top-tracks")
    Call<TopTracksResponse> topTracks(
        @Path("id") String id,
        @Query("country") String country);
  }

  public static SpotifyWebServices get() {
    return SPOTIFY_WEB_SERVICES;
  }

  private static void setupServiceHelper() {
    HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
    logging.setLevel(HttpLoggingInterceptor.Level.BODY);

    OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
    httpClient.addInterceptor(logging);

    Retrofit retrofit = new Retrofit.Builder()
        .baseUrl(API_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(httpClient.build())
        .build();

    SPOTIFY_WEB_SERVICES = retrofit.create(SpotifyWebServices.class);
  }

  @Produce
  public ArtistSearchEvent produceArtistSearchEvent(ArtistSearchResponse searchResponse) {
    return new ArtistSearchEvent(searchResponse);
  }

  public void doArtistSearch(String searchTerm) {
    String encodedSearch = null;
    try {
      encodedSearch = StringUtilities.makeUrlEncoded(searchTerm);
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }

    Call<ArtistSearchResponse> call = SpotifyApiClient.get().search(encodedSearch, Constants.TYPE_ARTISTS);

    call.enqueue(new Callback<ArtistSearchResponse>() {
      @Override
      public void onResponse(Call<ArtistSearchResponse> call, Response<ArtistSearchResponse> response) {
        ArtistSearchResponse searchResponse = response.body();
        BusProvider.getInstance().post(produceArtistSearchEvent(searchResponse));
      }

      @Override
      public void onFailure(Call<ArtistSearchResponse> call, Throwable t) {
        Log.e(TAG, "onFailure: ", t.getCause());
      }
    });
  }

  @Produce
  public TopTracksEvent produceTopTracksEvent(TopTracksResponse tracksResponse) {
    return new TopTracksEvent(tracksResponse);
  }

  public void doTopTracks(String artistId) {
    Call<TopTracksResponse> call = SpotifyApiClient.get().topTracks(artistId, Constants.COUNTRY_CODE);

    call.enqueue(new Callback<TopTracksResponse>() {
      @Override
      public void onResponse(Call<TopTracksResponse> call, Response<TopTracksResponse> response) {
        TopTracksResponse tracksResponse = response.body();
        BusProvider.getInstance().post(produceTopTracksEvent(tracksResponse));
      }

      @Override
      public void onFailure(Call<TopTracksResponse> call, Throwable t) {
        Log.e(TAG, "onFailure: ", t.getCause());
      }
    });
  }
}

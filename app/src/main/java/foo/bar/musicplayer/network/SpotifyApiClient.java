package foo.bar.musicplayer.network;

import java.io.UnsupportedEncodingException;

import foo.bar.musicplayer.model.Artists;
import foo.bar.musicplayer.util.Constants;
import foo.bar.musicplayer.util.StringUtilities;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
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
        Single<Response<ArtistSearchResponse>> search(@Query("q") String query, @Query("type") String type);

        @GET("v1/artists/{id}/top-tracks")
        Single<Response<TopTracksResponse>> topTracks(@Path("id") String id, @Query("country") String country);
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
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(httpClient.build())
            .build();

        SPOTIFY_WEB_SERVICES = retrofit.create(SpotifyWebServices.class);
    }

    public void doArtistSearch(String searchTerm, final Consumer<Artists> consumer) {
        String encodedSearch = null;
        try {
            encodedSearch = StringUtilities.makeUrlEncoded(searchTerm);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        SpotifyApiClient.get().search(encodedSearch, Constants.TYPE_ARTISTS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Consumer<Response<ArtistSearchResponse>>() {
                @Override
                public void accept(Response<ArtistSearchResponse> artistSearchResponseResponse) throws Exception {
                    if (artistSearchResponseResponse.isSuccessful() && artistSearchResponseResponse.code() == 200) {
                        Single.just(artistSearchResponseResponse.body().getArtists())
                            .subscribe(consumer);
                    }
                }
            }, new Consumer<Throwable>() {
                @Override
                public void accept(Throwable throwable) throws Exception {

                }
            });
    }

    public void doTopTracks(String artistId) {
        SpotifyApiClient.get().topTracks(artistId, Constants.COUNTRY_CODE)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Consumer<Response<TopTracksResponse>>() {
                @Override
                public void accept(Response<TopTracksResponse> topTracksResponseResponse) throws Exception {

                }
            });
    }
}

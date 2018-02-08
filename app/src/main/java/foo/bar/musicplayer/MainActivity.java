package foo.bar.musicplayer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity
{
    private static final String SEARCHLIST_FRAGMENT_TAG = "fragment_searchlist";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        showSearchListFragment();
    }

    private void showSearchListFragment() {
        if (getSupportFragmentManager().findFragmentByTag(SEARCHLIST_FRAGMENT_TAG) == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, new SearchListFragment(), SEARCHLIST_FRAGMENT_TAG)
                    .commit();
        }
    }
}

package app.orion.com.popularmovies.ui;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import app.orion.com.popularmovies.R;

public class MainActivity extends AppCompatActivity {
    private MainFragment mainFragment;
    private final String MAIN_FRAGMENT_TAG = "main_fragment";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, new MainFragment(), MAIN_FRAGMENT_TAG)
                    .commit();
        }
    }
}

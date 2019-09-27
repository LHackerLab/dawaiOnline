package fusionsoftware.loop.dawaionline.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import fusionsoftware.loop.dawaionline.R;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_search);
    }
}

package au.edu.sydney.comp5216.runtracker;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import au.edu.sydney.comp5216.runtracker.fragments.PastRunsFragment;
import au.edu.sydney.comp5216.runtracker.fragments.MapFragment;
import au.edu.sydney.comp5216.runtracker.fragments.MusicFragment;
import au.edu.sydney.comp5216.runtracker.fragments.RunFragment;

public class MainActivity extends AppCompatActivity {

    private Fragment runFragment;
    private Fragment mapFragment;
    private Fragment musicFragment;
    private Fragment pastRunsFragment;
    private Fragment mCurrentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        runFragment = new RunFragment();
        mapFragment = new MapFragment();
        musicFragment = new MusicFragment();
        pastRunsFragment = new PastRunsFragment();
        mCurrentFragment = runFragment;

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.mainLayout, runFragment);
        transaction.hide(runFragment);
        transaction.add(R.id.mainLayout, mapFragment);
        transaction.hide(mapFragment);
        transaction.add(R.id.mainLayout, musicFragment);
        transaction.hide(musicFragment);
        transaction.add(R.id.mainLayout, pastRunsFragment);
        transaction.hide(pastRunsFragment);
        transaction.show(mCurrentFragment);
        transaction.commit();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            // http://www.truiton.com/2017/01/android-bottom-navigation-bar-example/
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.hide(mCurrentFragment);
            switch (item.getItemId()) {
                case R.id.navigation_run:
                    mCurrentFragment = runFragment;
                    break;
                case R.id.navigation_map:
                    mCurrentFragment = mapFragment;
                    break;
                case R.id.navigation_music:
                    mCurrentFragment = musicFragment;
                    break;
                case R.id.navigation_history:
                    mCurrentFragment = pastRunsFragment;
                    break;
            }
            transaction.show(mCurrentFragment);
            transaction.commit();
            return true;
        }
    };
}

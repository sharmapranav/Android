package au.edu.sydney.comp5216.runtracker;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    private Fragment mRunFragment;
    private Fragment mMusicFragment;
    private Fragment mHistoryFragment;
    private Fragment mCurrentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRunFragment = new RunFragment();
        mMusicFragment = new MusicFragment();
        mHistoryFragment = new HistoryFragment();
        mCurrentFragment = mRunFragment;

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.mainLayout, mCurrentFragment);
        transaction.commit();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_run:
                    mCurrentFragment = mRunFragment;
                    break;
                case R.id.navigation_music:
                    mCurrentFragment = mMusicFragment;
                    break;
                case R.id.navigation_history:
                    mCurrentFragment = mHistoryFragment;
                    break;
            }
            // http://www.truiton.com/2017/01/android-bottom-navigation-bar-example/
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.mainLayout, mCurrentFragment);
            transaction.commit();
            return true;
        }
    };
}

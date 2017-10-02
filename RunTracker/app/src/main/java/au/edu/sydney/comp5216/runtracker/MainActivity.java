package au.edu.sydney.comp5216.runtracker;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Fragment mRunFragment;
    private Fragment mMapFragment;
    private Fragment mMusicFragment;
    private Fragment mHistoryFragment;
    private Fragment mCurrentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRunFragment = new RunFragment();
        mMapFragment = new MapFragment();
        mMusicFragment = new MusicFragment();
        mHistoryFragment = new HistoryFragment();
        mCurrentFragment = mRunFragment;

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.mainLayout, mRunFragment);
        transaction.hide(mRunFragment);
        transaction.add(R.id.mainLayout, mMapFragment);
        transaction.hide(mMapFragment);
        transaction.add(R.id.mainLayout, mMusicFragment);
        transaction.hide(mMusicFragment);
        transaction.add(R.id.mainLayout, mHistoryFragment);
        transaction.hide(mHistoryFragment);
        transaction.show(mCurrentFragment);
        transaction.commit();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    public void onRunStatusClick(View view) {
        Button runStatus = (Button) findViewById(R.id.runStatus);

        if (runStatus.getText().equals(getString(R.string.start_run))) {
            runStatus.setText(getString(R.string.stop_run));
        } else if (runStatus.getText().equals(getString(R.string.stop_run))) {
            runStatus.setText(getString(R.string.start_run));
        }
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
                    mCurrentFragment = mRunFragment;
                    break;
                case R.id.navigation_map:
                    mCurrentFragment = mMapFragment;
                    break;
                case R.id.navigation_music:
                    mCurrentFragment = mMusicFragment;
                    break;
                case R.id.navigation_history:
                    mCurrentFragment = mHistoryFragment;
                    break;
            }
            transaction.show(mCurrentFragment);
            transaction.commit();
            return true;
        }
    };
}

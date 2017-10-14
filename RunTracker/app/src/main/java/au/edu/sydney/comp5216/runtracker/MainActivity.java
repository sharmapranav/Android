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
    private Fragment mMapFragment;
    private Fragment mMusicFragment;
    private Fragment mHistoryFragment;
    private Fragment mCurrentFragment;

//    private TextView mPace;
//    private TextView mSpeed;
//    private TextView mDistance;
//    private TextView mTime;
//    private long mRunStartTime;
//    private Location mLastLocation;
//
//    private Timer mTimer;
//    final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRunFragment = new RunFragment();
        mMapFragment = new MapFragment();
        mMusicFragment = new MusicFragment();
        mHistoryFragment = new HistoryFragment();
        mCurrentFragment = mRunFragment;

//        mPace = (TextView) findViewById(R.id.pace);
//        mSpeed = (TextView) findViewById(R.id.speed);
//        mDistance = (TextView) findViewById(R.id.distance);
//        mTime = (TextView) findViewById(R.id.time);

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

//        registerReceiver(locationReceiver, new IntentFilter("LocationChanged"));
    }

//    BroadcastReceiver locationReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            mLastLocation = intent.getParcelableExtra("location");
////            updateLocation(location);
//        }
//    };
//
//    public void onRunStatusClick(View view) {
//        if (mLastLocation == null) {
////            Toast.makeText(this, "Location services not working! Please check location services.", Toast.LENGTH_SHORT).show();
//            ShowToast("Location services not working! Please check location services.");
//            return;
//        }
//
//        Button runStatus = (Button) findViewById(R.id.runStatus);
//
//        if (runStatus.getText().equals(getString(R.string.start_run))) {
//            runStatus.setText(getString(R.string.stop_run));
//            startRun();
//        } else if (runStatus.getText().equals(getString(R.string.stop_run))) {
//            runStatus.setText(getString(R.string.start_run));
//            stopRun();
//        }
//    }
//
//    // https://stackoverflow.com/questions/4208401/timer-with-callback
//    private void startRun() {
//        mRunStartTime = System.currentTimeMillis();
//
//        mTimer = new Timer();
//        TimerTask timerTask = new TimerTask() {
//            public void run() {
//                handler.post(new Runnable() {
//                    public void run() {
//                        updateRunData();
//                    }
//                });
//            }
//        };
//
//        mTimer.scheduleAtFixedRate(timerTask, 0, 1000);
//    }
//
//    private void updateRunData() {
//        String currentTime = Long.toString(System.currentTimeMillis() - mRunStartTime);
////        ShowToast(currentTime);
//        try {
//            mTime.setText(currentTime);
//        }
//        catch (Exception ex){
//            ShowToast(ex.getMessage());
//        }
//    }
//
//    private void stopRun() {
//        if (mTimer != null) {
//            mTimer.cancel();
//            mTimer = null;
//        }
//    }
//
//    private void ShowToast(String message)
//    {
//        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
//    }
////
////    public void updateLocation(Location location){
////        if(location != null)
////        {
////            Toast.makeText(this, location.toString(), Toast.LENGTH_SHORT).show();
////        }
////    }
//
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

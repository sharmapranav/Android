package au.edu.sydney.comp5216.runtracker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

/**
 * Created by pranav on 30/09/2017.
 */

public class RunFragment extends Fragment {

    private View view;
    private TextView pace;
    private TextView speed;
    private TextView distance;
    private TextView time;
    private Button runStatus;

    private long mRunStartTime;
    private Location mLastLocation;
    private Timer mTimer;
    final Handler handler = new Handler();
    SimpleDateFormat simpleDateFormat;
    DecimalFormat decimalFormat;
    List<LatLng> latLngs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_run, container, false);

        pace = view.findViewById(R.id.pace);
        speed = view.findViewById(R.id.speed);
        distance = view.findViewById(R.id.distance);
        time = view.findViewById(R.id.time);
        runStatus = view.findViewById(R.id.runStatus);

        runStatus.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                onRunStatusClick(v);
            }
        });

        simpleDateFormat = new SimpleDateFormat("HH:mm:ss", Locale.UK);
        decimalFormat = new DecimalFormat();
        decimalFormat.setMaximumFractionDigits(2);

        latLngs = new ArrayList<>();

        getActivity().registerReceiver(locationReceiver, new IntentFilter("LocationChanged"));

        return view;
    }

    BroadcastReceiver locationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mLastLocation = intent.getParcelableExtra("location");
            latLngs.add(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()));
        }
    };

    public void onRunStatusClick(View view) {
        if (mLastLocation == null) {
            ShowToast("Location services not working! Please check location services.");
            return;
        }

        if (runStatus.getText().equals(getString(R.string.start_run))) {
            runStatus.setText(getString(R.string.stop_run));
            startRun();
        } else if (runStatus.getText().equals(getString(R.string.stop_run))) {
            runStatus.setText(getString(R.string.start_run));
            stopRun();
        }
    }

    // https://stackoverflow.com/questions/4208401/timer-with-callback
    private void startRun() {
        mRunStartTime = System.currentTimeMillis();

        mTimer = new Timer();
        TimerTask timerTask = new TimerTask() {
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        updateRunData();
                    }
                });
            }
        };

        mTimer.scheduleAtFixedRate(timerTask, 0, 1000);
    }

    private void updateRunData() {
        long currentTime = System.currentTimeMillis();

        long runTime = currentTime - mRunStartTime;
        String runTimeString = String.format(Locale.UK,"%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(runTime),
                TimeUnit.MILLISECONDS.toMinutes(runTime) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(runTime)),
                TimeUnit.MILLISECONDS.toSeconds(runTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(runTime)));
        time.setText(runTimeString);

        Double runDist = SphericalUtil.computeLength(latLngs);
        if(runDist < 1000) {
            distance.setText(decimalFormat.format(runDist) + " m");
            runDist /= 1000;
        }
        else
        {
            runDist /= 1000;
            distance.setText(decimalFormat.format(runDist) + " km");
        }

        Double runSpeed = runDist / ((currentTime - mRunStartTime) / 3600000.0);
        speed.setText(decimalFormat.format(runSpeed) + " km/h");

        Double runPace = 60 / runSpeed;
        pace.setText(decimalFormat.format(runPace) + " minutes/km");
    }

    private void stopRun() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
        latLngs.clear();

        RunHistory runHistory = new RunHistory(pace.getText().toString(), speed.getText().toString(), distance.getText().toString(), time.getText().toString());
        Intent runData = new Intent("RunCompleted");
        runData.putExtra("runHistory", runHistory);
        getContext().sendBroadcast(runData);
    }

    private void ShowToast(String message)
    {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }
}

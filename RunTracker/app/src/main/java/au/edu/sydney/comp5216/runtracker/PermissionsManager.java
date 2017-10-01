package au.edu.sydney.comp5216.runtracker;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

/**
 * Created by pranav on 1/10/2017.
 */

public class PermissionsManager {

    public static final int ACCESS_FINE_LOCATION = 1;

    public static boolean hasFineLocationPermission(Activity activity){
        int result = ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION);

        return result == PackageManager.PERMISSION_GRANTED ? true : false;
    }

    public static void requestFineLocationPermission(Activity activity){
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_FINE_LOCATION)){
            Toast.makeText(activity, "Location permission needed for tracking run. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},ACCESS_FINE_LOCATION);
        }
    }
}

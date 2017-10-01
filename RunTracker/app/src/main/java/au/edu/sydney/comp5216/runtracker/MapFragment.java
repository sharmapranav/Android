package au.edu.sydney.comp5216.runtracker;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by pranav on 30/09/2017.
 * https://github.com/googlemaps/android-samples/tree/master/tutorials
 */

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private View view;
    private GoogleMap googleMap;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_map, container, false);

        try {
            SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapView);
            mapFragment.getMapAsync(MapFragment.this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        try {
            this.googleMap = googleMap;

            // Add a marker in Sydney and move the camera
            LatLng location = new LatLng(-33.852, 151.211);
            this.googleMap.addMarker(new MarkerOptions().position(location).title("Field"));

            this.googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 16));

            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(location, 16);
            this.googleMap.addMarker(new MarkerOptions().position(location).title(""));
            this.googleMap.animateCamera(cameraUpdate, 2000, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onDestroyView() {
        try {
            Fragment fragment = (getChildFragmentManager().findFragmentById(R.id.mapView));
            if (fragment != null) {
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.remove(fragment);
                ft.commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroyView();
    }
}

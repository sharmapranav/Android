package au.edu.sydney.comp5216.runtracker.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import au.edu.sydney.comp5216.runtracker.adapters.RunItemsAdapter;
import au.edu.sydney.comp5216.runtracker.R;
import au.edu.sydney.comp5216.runtracker.models.RunItem;

/**
 * Created by pranav on 29/09/2017.
 */

public class PastRunsFragment extends Fragment {

    View view;
    ListView listview;
    ArrayList<RunItem> runItems;
    ArrayAdapter<RunItem> runItemsAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_history, container, false);

        runItems = new ArrayList<>();
        runItems.addAll(RunItem.listAll(RunItem.class));

        listview = view.findViewById(R.id.listView);
        runItemsAdapter = new RunItemsAdapter(getContext(), runItems);
        listview.setAdapter(runItemsAdapter);

        getActivity().registerReceiver(runCompletedReceiver, new IntentFilter("RunCompleted"));

        return view;
    }

    BroadcastReceiver runCompletedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            RunItem runItem = (RunItem) intent.getSerializableExtra("runItem");
            runItems.add(0, runItem);
            runItemsAdapter.notifyDataSetChanged();
            runItem.save();
        }
    };

}

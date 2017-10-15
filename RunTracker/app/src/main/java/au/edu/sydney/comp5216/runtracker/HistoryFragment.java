package au.edu.sydney.comp5216.runtracker;

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

/**
 * Created by pranav on 29/09/2017.
 */

public class HistoryFragment extends Fragment {

    View view;
    ListView listview;
    ArrayList<RunHistory> runHistories;
    ArrayAdapter<RunHistory> itemsAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_history, container, false);

        runHistories = new ArrayList<>();
        listview = view.findViewById(R.id.listView);
        itemsAdapter = new HistoryListAdapter(getContext(), runHistories);
        listview.setAdapter(itemsAdapter);

        getActivity().registerReceiver(runEndReceiver, new IntentFilter("RunCompleted"));

        return view;
    }

    BroadcastReceiver runEndReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            RunHistory runHistory = (RunHistory) intent.getSerializableExtra("runHistory");
            runHistories.add(0, runHistory);
            itemsAdapter.notifyDataSetChanged();
        }
    };

}

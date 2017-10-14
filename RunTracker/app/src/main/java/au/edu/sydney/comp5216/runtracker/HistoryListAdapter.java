package au.edu.sydney.comp5216.runtracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by prana on 6/10/2017.
 */

public class HistoryListAdapter extends ArrayAdapter<RunHistory> {
    public HistoryListAdapter(Context context, ArrayList<RunHistory> runHistoryItems) {
        super(context, 0, runHistoryItems);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Get the data item for this position
        RunHistory runHistory = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.layout_history_item, parent, false);
        }

        // Lookup view for data population
        TextView date = convertView.findViewById(R.id.date);
        TextView pace = convertView.findViewById(R.id.pace);
        TextView speed = convertView.findViewById(R.id.speed);
        TextView distance =convertView.findViewById(R.id.distance);
        TextView time = convertView.findViewById(R.id.time);

        // Populate the data into the template view using the data object
        date.setText(runHistory.getDate());
        pace.setText(runHistory.getPace());
        speed.setText(runHistory.getSpeed());
        distance.setText(runHistory.getDistance());
        time.setText(runHistory.getTime());

        // Return the completed view to render on screen
        return convertView;
    }
}

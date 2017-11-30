package au.edu.sydney.comp5216.runtracker.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import au.edu.sydney.comp5216.runtracker.R;
import au.edu.sydney.comp5216.runtracker.models.RunItem;

/**
 * Created by prana on 6/10/2017.
 */

public class RunItemsAdapter extends ArrayAdapter<RunItem> {
    public RunItemsAdapter(Context context, ArrayList<RunItem> runItemItems) {
        super(context, 0, runItemItems);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Get the data item for this position
        RunItem runItem = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.layout_run_item, parent, false);
        }

        // Lookup view for data population
        TextView date = convertView.findViewById(R.id.date);
        TextView pace = convertView.findViewById(R.id.pace);
        TextView speed = convertView.findViewById(R.id.speed);
        TextView distance =convertView.findViewById(R.id.distance);
        TextView time = convertView.findViewById(R.id.time);

        // Populate the data into the template view using the data object
        date.setText(runItem.getDate());
        pace.setText(runItem.getPace());
        speed.setText(runItem.getSpeed());
        distance.setText(runItem.getDistance());
        time.setText(runItem.getTime());

        // Return the completed view to render on screen
        return convertView;
    }
}

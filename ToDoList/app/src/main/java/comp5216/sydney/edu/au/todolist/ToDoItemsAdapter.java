package comp5216.sydney.edu.au.todolist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by prana on 26/08/2017.
 * reference: https://github.com/codepath/android_guides/wiki/Using-an-ArrayAdapter-with-ListView
 */

public class ToDoItemsAdapter extends ArrayAdapter<ToDoItem> {
    public ToDoItemsAdapter(Context context, ArrayList<ToDoItem> toDoItems) {
        super(context, 0, toDoItems);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Get the data item for this position
        ToDoItem toDoItem = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.todo_item, parent, false);
        }

        // Lookup view for data population
        TextView title = (TextView) convertView.findViewById(R.id.title);
        TextView date = (TextView) convertView.findViewById(R.id.date);

        // Populate the data into the template view using the data object
        title.setText(toDoItem.getTodo());
        date.setText(toDoItem.getDate());

        // Return the completed view to render on screen
        return convertView;
    }
}

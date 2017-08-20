package comp5216.sydney.edu.au.todolist;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    //define variables
    ListView listview;
    ArrayList<String> items;
    ArrayAdapter<String> itemsAdapter;
    EditText addItemEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //use "activity_main.xml" as the layout
        setContentView(R.layout.activity_main);

        //reference the "listview" variable to the id-"listview" in the layout
        listview = (ListView) findViewById(R.id.listview);
        addItemEditText = (EditText) findViewById(R.id.txtNewItem);

        //create an ArrayList of String
        items = new ArrayList<String>();
        items.add("item one");
        items.add("item two");

        //Create an adapter for the list view using Android's built-in item layout
        itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);

        //connect the listview and the adapter
        listview.setAdapter(itemsAdapter);
    }

    public void onAddItemClick(View view) {
        String toAddString = addItemEditText.getText().toString();
        toAddString += " " + new Date().toString();

        if (toAddString != null && toAddString.length() > 0) {
            itemsAdapter.add(toAddString);
            addItemEditText.setText("");
        }
    }
}

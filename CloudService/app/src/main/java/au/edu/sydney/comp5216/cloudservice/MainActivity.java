package au.edu.sydney.comp5216.cloudservice;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //define variables
    ListView listview;
    ArrayList<String> items;
    ArrayAdapter<String> itemsAdapter;

    private final String USER_AGENT = "Mozilla/5.0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //use "activity_main.xml" as the layout
        setContentView(R.layout.activity_main);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        //reference the "listview" variable to the id-"listview" in the layout
        listview = (ListView) findViewById(R.id.listview);

        //create an Array List of String
        items = new ArrayList<String>();

        //Create an adapter for the listview using Android's built-in item layout
        itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);

        //connect the listview and the adapter
        listview.setAdapter(itemsAdapter);
    }

    public void onAddItemClick(View view) throws Exception {

        itemsAdapter.clear();

        ParseFromCloud temperatures = new ParseFromCloud();
        temperatures.gettemperature();
        ArrayList<TemperatureData> tds = temperatures.tds;

        for (TemperatureData td : tds) {
            String toAddString = td.getCreatedAt() + "\ntemperatureis:" + td.getTemperature();
            if (toAddString != null && toAddString.length() > 0) {
                itemsAdapter.add(toAddString);
            }
        }
    }
}

package comp5216.sydney.edu.au.todolist;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class EditToDoItemActivity extends Activity {
    public String editItem = null;
    public int position = 0;
    EditText etItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //populate the screen using the layout
        setContentView(R.layout.activity_edit_item);

        //Get the data from the main screen
        editItem = getIntent().getStringExtra("item");
        position = getIntent().getIntExtra("position", -1);

        if(editItem != null && position != -1) {

            // show original content in the text field
            etItem = (EditText) findViewById(R.id.etEditItem);
            String temp[] = editItem.split("\n");
            editItem = temp[0];
            etItem.setText(editItem);
        }
    }

    public void onSubmit(View v) {
        etItem = (EditText) findViewById(R.id.etEditItem);

        String todoText = etItem.getText().toString();

        if(todoText == null || todoText.isEmpty())
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(EditToDoItemActivity.this);
            builder.setTitle(R.string.todo_title).setMessage(R.string.dialog_todo_empty_msg).setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                    //User Ok the dialog
                    // nothing happens
                }
            });

            builder.create().show();
        }
        else {

            // Prepare data intent for sending it back
            Intent data = new Intent();

            Date currentTime = Calendar.getInstance().getTime();
            SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd hh:mm a", Locale.getDefault());

            todoText += "\n" + format.format(currentTime);

            // Pass relevant data back as a result
            data.putExtra("item", todoText);
            if (position != -1) {
                data.putExtra("position", position);
            }

            // Activity finished ok, return the data
            setResult(RESULT_OK, data); // set result code and bundle data for response
            finish(); // closes the activity, pass data to parent
        }
    }

    public void onCancel(View v) {

        // Activity finished ok, return the data
        setResult(RESULT_CANCELED); // set result code and bundle data for response
        finish(); // closes the activity, pass data to parent
    }

}

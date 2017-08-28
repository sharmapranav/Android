package comp5216.sydney.edu.au.todolist;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class EditToDoItemActivity extends Activity {

    private enum Mode {Add, Edit};
    Mode mode;
    ToDoItem toDoItem;
    int position = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //populate the screen using the layout
        setContentView(R.layout.activity_edit_item);

        //Get the data from the main screen
        toDoItem = (ToDoItem) getIntent().getSerializableExtra("item");
        position = getIntent().getIntExtra("position", -1);

        mode = (toDoItem == null && position == -1) ? Mode.Add : Mode.Edit;

        if (mode.equals(Mode.Edit)) {
            // show original content in the text field
            EditText etItem = (EditText) findViewById(R.id.etEditItem);
            etItem.setText(toDoItem.getTodo());

        } else {
            toDoItem = new ToDoItem();
            Date currentTime = Calendar.getInstance().getTime();
            SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd hh:mm a", Locale.getDefault());
            toDoItem.setDate(currentTime);
        }

        TextView textClock = (TextView) findViewById(R.id.textClock);
        textClock.setText(toDoItem.getDate());

        TextView pageTitle = (TextView) findViewById(R.id.pageTitle);
        pageTitle.setText(mode.toString() + " Item");
    }

    public void onSubmit(View v) {

        EditText etItem = (EditText) findViewById(R.id.etEditItem);
        String todoText = etItem.getText().toString();

        if (todoText == null || todoText.isEmpty()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(EditToDoItemActivity.this);
            builder.setTitle(R.string.todo_title).setMessage(R.string.dialog_todo_empty_msg).setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                    //User Ok the dialog
                    // nothing happens
                }
            });

            builder.create().show();
        } else {

            // Prepare data intent for sending it back
            Intent data = new Intent();

            toDoItem.setTodo(todoText);

            // Pass relevant data back as a result
            data.putExtra("item", toDoItem);

            if (mode.equals(Mode.Edit)) {
                data.putExtra("position", position);
            }

            // Activity finished ok, return the data
            // set result code and bundle data for response
            setResult(RESULT_OK, data);

            // closes the activity, pass data to parent
            finish();
        }
    }

    public void onCancel(View v) {

        AlertDialog.Builder builder = new AlertDialog.Builder(EditToDoItemActivity.this);
        builder.setTitle(R.string.todo_title).setMessage(R.string.dialog_todo_cancel).setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                // Activity finished ok, return the data
                // set result code and bundle data for response
                setResult(RESULT_CANCELED);

                // closes the activity, pass data to parent
                finish();
            }
        }).setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                //User cancelled the dialog
                // nothing happens
            }
        });

        builder.create().show();
    }
}

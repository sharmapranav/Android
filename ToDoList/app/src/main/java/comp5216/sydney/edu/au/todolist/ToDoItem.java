package comp5216.sydney.edu.au.todolist;

import com.orm.SugarRecord;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ToDoItem extends SugarRecord implements Serializable {

    private String todo;
    private String date;

    static SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.getDefault());

    public ToDoItem() {
    }

    public ToDoItem(String ToDo, Date date) {

        this.todo = ToDo;
        this.date = dateFormatter.format(date);
    }

    public String getTodo() {
        return todo;
    }

    public String getDate() {
        return date;
    }

    public void setTodo(String todo) {
        this.todo = todo;
    }

    public void setDate(Date date) {
        this.date = dateFormatter.format(date);
    }
}

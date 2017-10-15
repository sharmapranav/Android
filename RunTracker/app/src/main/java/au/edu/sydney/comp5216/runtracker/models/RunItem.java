package au.edu.sydney.comp5216.runtracker.models;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by prana on 6/10/2017.
 */

public class RunItem implements Serializable {

    private String date;
    private String pace;
    private String speed;
    private String distance;
    private String time;

    static SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.getDefault());

    public RunItem(){
    }

    public RunItem(String pace, String speed, String distance, String time){
        this.date = "Date: " + dateFormatter.format(new Date());
        this.pace = "Pace: " + pace;
        this.speed = "Speed: " + speed;
        this.distance = "Distance: " + distance;
        this.time = "Time: " + time;
    }

    public String getDate(){ return date; }
    public String getPace(){ return pace; }
    public String getSpeed(){ return speed; }
    public String getDistance(){ return distance; }
    public String getTime(){ return time; }
}

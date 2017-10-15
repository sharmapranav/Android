package au.edu.sydney.comp5216.runtracker;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by prana on 6/10/2017.
 */

public class RunHistory  implements Serializable {

    private String date;
    private String pace;
    private String speed;
    private String distance;
    private String time;

    static SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.getDefault());

    public RunHistory(){
    }

    public RunHistory(String pace, String speed, String distance, String time){
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

//    public void setDate(String date){ this.date = date; }
//    public void setPace(String pace){ this.pace = pace; }
//    public void setSpeed(String speed){ this.speed = speed; }
//    public void setDistance(String distance){ this.distance = distance; }
//    public void setTime(String time){ this.time = time; }
}

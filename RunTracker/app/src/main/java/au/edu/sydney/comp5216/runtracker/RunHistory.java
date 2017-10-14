package au.edu.sydney.comp5216.runtracker;

import com.orm.SugarRecord;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by prana on 6/10/2017.
 */

public class RunHistory extends SugarRecord implements Serializable {

    private String date;
    private String pace;
    private String speed;
    private String distance;
    private String time;

    public RunHistory(){
    }

    public RunHistory(String pace, String speed, String distance, String time){
        this.date = new Date().toString();
        this.pace = pace;
        this.speed = speed;
        this.distance = distance;
        this.time = time;
    }

    public String getDate(){ return date; }
    public String getPace(){ return pace; }
    public String getSpeed(){ return speed; }
    public String getDistance(){ return distance; }
    public String getTime(){ return time; }

    public void setDate(String date){ this.date = date; }
    public void setPace(String pace){ this.pace = pace; }
    public void setSpeed(String speed){ this.speed = speed; }
    public void setDistance(String distance){ this.distance = distance; }
    public void setTime(String time){ this.time = time; }
}

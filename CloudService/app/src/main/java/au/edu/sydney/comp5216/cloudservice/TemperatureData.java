package au.edu.sydney.comp5216.cloudservice;

public class TemperatureData {
    private double temperature;
    private String createdAt;

    public TemperatureData (){

    }

    public TemperatureData (double _temperature, String _createdAt) {
        this.createdAt = _createdAt;
        this.temperature = _temperature;
    }

    public double getTemperature(){
        return this.temperature;
    }

    public String getCreatedAt() {
        return this.createdAt;
    }

}

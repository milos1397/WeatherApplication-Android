package r.rtrk.weatherforecast;

import android.content.Context;

import java.util.Date;

public class WeatherData {
    private String city,date;

    private double temperature, humidity, pressure;

    private String sunrise, sunset;

    private double windSpeed;
    private String windDirection;


    public WeatherData(String city, double temperature, double humidity, double pressure, String sunrise,
                       String sunset, double windSpeed, String windDirection, String date) {
        this.city = city;
        this.date = date;
        this.temperature = temperature;
        this.humidity = humidity;
        this.pressure = pressure;
        this.sunrise = sunrise;
        this.sunset = sunset;
        this.windSpeed = windSpeed;
        this.windDirection = windDirection;
    }

    public String getCity() {
        return city;
    }

    public String getDate() {
        return date;
    }
    public double getTemperature() {
        return temperature;
    }

    public double getHumidity() {
        return humidity;
    }

    public double getPressure() {
        return pressure;
    }

    public String getSunrise() {
        return sunrise;
    }

    public String getSunset() {
        return sunset;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public String getWindDirection() {
        return windDirection;
    }


    /*public String convertDegrees(double degrees){
        mContext = this;
        if(degrees>337.5)
            return getString(R.string.north);
        if(degrees>292.5)
            return getString(R.string.nw);
        if(degrees>247.5)
            return getString(R.string.west);
        if(degrees>202.5)
            return getString(R.string.sw);
        if(degrees>157.5)
            return getString(R.string.south);
        if(degrees>122.5)
            return getString(R.string.se);
        if(degrees>67.5)
            return getString(R.string.east);
        if(degrees>22.5)
            return getString(R.string.ne);
        else
            return getString(R.string.north);
    }*/


}


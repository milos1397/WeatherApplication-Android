package r.rtrk.weatherforecast;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.TimeZone;

public class DetailsActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView tCity,tDay,tTemp,tPress,tWindS,tWindD,tHum,tVal,tSunR,tSunS;
    private ImageView iSun;
    private Button bTemp,bSun,bWind;
    private Spinner sVal;
    private HTTPHelper httpHelper;
    String[] values;


    String showtext;
    public ArrayAdapter<String> adapter;
    public static String BASE_URL = "https://api.openweathermap.org/data/2.5/weather?q=";
    public static String GRAD ;
    public static String KEY = "&APPID=1c8772c9f12179930d9c1659860c15dc";
    public String MEASURE_IN="&units=imperial";
    public String GET_CITY;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        iSun=(ImageView)findViewById(R.id.img);
        tVal=(TextView) findViewById(R.id.value);
        tTemp=(TextView) findViewById(R.id.dataTemp);
        tPress=(TextView)findViewById(R.id.dataPress);
        tWindS=(TextView)findViewById(R.id.windStrength);
        tWindD=(TextView)findViewById(R.id.windDirection);
        tSunR=(TextView)findViewById(R.id.dataSunRise);
        tSunS=(TextView)findViewById(R.id.dataSunSet);
        sVal=(Spinner)findViewById(R.id.tempOpt);
        tHum=(TextView)findViewById(R.id.dataHum);
        iSun.setVisibility(View.INVISIBLE);
        tTemp.setVisibility(View.INVISIBLE);
        tPress.setVisibility(View.INVISIBLE);
        tWindS.setVisibility(View.INVISIBLE);
        tWindD.setVisibility(View.INVISIBLE);
        tSunR.setVisibility(View.INVISIBLE);
        tSunS.setVisibility(View.INVISIBLE);
        sVal.setVisibility(View.INVISIBLE);
        tHum.setVisibility(View.INVISIBLE);
        tVal.setVisibility(View.INVISIBLE);


        Bundle bundle = getIntent().getExtras();
        showtext = bundle.getString("City_name");

        tCity=(TextView)findViewById(R.id.city);
        tCity.setText(getString(R.string.location)+" "+showtext);

        Calendar localCalendar = Calendar.getInstance(TimeZone.getDefault());
        Date currentTime = localCalendar.getTime();
        int currentDayofWeek = localCalendar.get(Calendar.DAY_OF_WEEK);
        String day=getInSerbian(currentDayofWeek-1);
        tDay=(TextView)findViewById(R.id.day);
        tDay.setText(getString(R.string.day)+" "+day);


        values = getResources().getStringArray(R.array.value_arrays);
       adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, values);
        sVal.setAdapter(adapter);
       // sVal.setOnItemSelectedListener(this);

        httpHelper=new HTTPHelper();

        GRAD=showtext;

        GET_CITY= BASE_URL + GRAD + KEY + MEASURE_IN;


        bTemp=(Button)findViewById(R.id.temp);
        bSun=(Button)findViewById(R.id.sun);
        bWind=(Button)findViewById(R.id.wind);
        bTemp.setOnClickListener(this);
        bSun.setOnClickListener(this);
        bWind.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.sun:
                tSunR.setVisibility(View.VISIBLE);
                tSunS.setVisibility(View.VISIBLE);
                iSun.setVisibility(View.INVISIBLE);
                tTemp.setVisibility(View.INVISIBLE);
                tPress.setVisibility(View.INVISIBLE);
                tWindS.setVisibility(View.INVISIBLE);
                tWindD.setVisibility(View.INVISIBLE);
                sVal.setVisibility(View.INVISIBLE);
                tHum.setVisibility(View.INVISIBLE);
                tVal.setVisibility(View.INVISIBLE);
                new Thread(new Runnable() {
                    public void run() {
                        try {
                            Log.d("tread",GET_CITY);
                            JSONObject jsonobject = httpHelper.getJSONObjectFromURL(GET_CITY);
                            JSONObject sys = jsonobject.getJSONObject("sys");

                            long rise=Long.valueOf(sys.get("sunrise").toString())*1000;
                            Date date1=new Date(rise);
                            final String sunrise=new SimpleDateFormat("hh:mma",Locale.ENGLISH).format(date1);

                            long set=Long.valueOf(sys.get("sunset").toString())*1000;
                            Date date2=new Date(set);
                            final String sunset=new SimpleDateFormat("hh:mma",Locale.ENGLISH).format(date2);


                            runOnUiThread(new Runnable() {
                                public void run() {
                                    Log.d("tread",GET_CITY);
                                    tSunR.setText(getString(R.string.sunRise)+" "+sunrise);
                                    tSunS.setText(getString(R.string.sunSet)+" "+sunset);
                                }
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                break;
            case R.id.temp:
                sVal.setAdapter(adapter);
                tSunR.setVisibility(View.INVISIBLE);
                tSunS.setVisibility(View.INVISIBLE);
                iSun.setVisibility(View.VISIBLE);;
                tTemp.setVisibility(View.VISIBLE);
                tPress.setVisibility(View.VISIBLE);
                tWindS.setVisibility(View.INVISIBLE);
                tWindD.setVisibility(View.INVISIBLE);
                tHum.setVisibility(View.VISIBLE);
                sVal.setVisibility(View.VISIBLE);
                tVal.setVisibility(View.VISIBLE);
                new Thread(new Runnable() {
                    public void run() {
                        try {
                            JSONObject jsonobject = httpHelper.getJSONObjectFromURL(GET_CITY);
                            JSONObject main = jsonobject.getJSONObject("main");
                            final String temperature=main.get("temp").toString();
                            final String pressure=main.get("pressure").toString();
                            final String humidity=main.get("humidity").toString();

                            runOnUiThread(new Runnable() {
                                public void run() {
                                    double value = Double.parseDouble(temperature);
                                    String temperatureHelp=toFahrenheit(value);
                                    tTemp.setText(getString(R.string.tempData)+" "+temperatureHelp+getString(R.string.fahr));
                                    sVal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                       // @Override
                                        public void onItemSelected(AdapterView<?> arg0, View arg1,
                                                                   int arg2, long arg3) {
                                            String selected=arg0.getItemAtPosition(arg2).toString();
                                            if(selected.equals("°C")){
                                                double value = Double.parseDouble(temperature);
                                                String temperatureHelp=toCelsius(value);
                                                tTemp.setText(getString(R.string.tempData)+" "+temperatureHelp+getString(R.string.cels));

                                            }else{
                                                double value = Double.parseDouble(temperature);
                                                String temperatureHelp=toFahrenheit(value);
                                                tTemp.setText(getString(R.string.tempData)+" "+temperatureHelp+getString(R.string.fahr));
                                            }
                                        }

                                        //@Override
                                        public void onNothingSelected(AdapterView<?> arg0) {
                                            double value = Double.parseDouble(temperature);
                                            String temperatureHelp=toFahrenheit(value);
                                            tTemp.setText(getString(R.string.tempData)+" "+temperatureHelp);
                                        }
                                    });
                                    tPress.setText(getString(R.string.pressData)+" "+pressure+getString(R.string.bar));
                                    tHum.setText(getString(R.string.humData)+" "+humidity+getString(R.string.percent));
                                }
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                break;
            case R.id.wind:
                tSunS.setVisibility(View.INVISIBLE);
                tSunR.setVisibility(View.INVISIBLE);
                iSun.setVisibility(View.INVISIBLE);
                tTemp.setVisibility(View.INVISIBLE);
                tPress.setVisibility(View.INVISIBLE);
                tWindS.setVisibility(View.VISIBLE);
                tWindD.setVisibility(View.VISIBLE);
                sVal.setVisibility(View.INVISIBLE);
                tHum.setVisibility(View.INVISIBLE);
                tVal.setVisibility(View.INVISIBLE);

                new Thread(new Runnable() {
                    public void run() {
                        try {
                            JSONObject jsonobject = httpHelper.getJSONObjectFromURL(GET_CITY);
                            JSONObject wind = jsonobject.getJSONObject("wind");
                            final String strength=wind.get("speed").toString();
                            final String temp=wind.get("deg").toString();
                            double value = Double.parseDouble(temp);
                            final String direction=convertDegrees(value);

                            runOnUiThread(new Runnable() {
                                public void run() {
                                    tWindS.setText(getString(R.string.windStrength)+" "+strength);
                                    tWindD.setText(getString(R.string.windDirection)+" "+direction);
                                }
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();


        }
    }
    public String getInSerbian(int n){
        String day;
        switch (n){
            case 1:
                day=getString(R.string.monday);
                return day;
            case 2:
                day=getString(R.string.tuesday);
                return day;
            case 3:
                day=getString(R.string.wednesday);
                return day;
            case 4:
                day=getString(R.string.thursday);
                return day;
            case 5:
                day=getString(R.string.friday);
                return day;
            case 6:
                day=getString(R.string.saturday);
                return day;
            case 7:
                day=getString(R.string.sunday);
                return day;
            default:
                day=getString(R.string.error);
                return day;


        }
    }

    public String convertDegrees(double degrees){
        if(degrees>337.5)
            return "North";
        if(degrees>292.5)
            return "Noth West";
        if(degrees>247.5)
            return "West";
        if(degrees>202.5)
            return "South West";
        if(degrees>157.5)
            return "South";
        if(degrees>122.5)
            return "South East";
        if(degrees>67.5)
            return "East";
        if(degrees>22.5)
            return "North East";
        else
            return "North";
    }


    public String toFahrenheit(double temp) {
        int helper=(int)(temp * 5/9 + 32);
        return String.valueOf(helper);
    }


    public String toCelsius(double temp) {
        int helper=(int)(((temp - 32) * 5) / 9);
        return String.valueOf(helper);
    }

}

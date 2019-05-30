package r.rtrk.weatherforecast;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
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
import java.util.Timer;
import java.util.TimerTask;

public class DetailsActivity extends AppCompatActivity implements View.OnClickListener, ServiceConnection {

    private TextView tCity,tDay,tTemp,tPress,tWindS,tWindD,tHum,tVal,tSunR,tSunS,tLup;
    private ImageView iSun;
    private Button bTemp,bSun,bWind,bStat,bServ;
    private ImageButton ibRefr;
    private Spinner sVal;
    private HTTPHelper httpHelper;
    String[] values;
    private MyNDK myNDK;


    String showtext;//CITY NAME
    String day;
    public ArrayAdapter<String> adapter;
    public static String BASE_URL = "https://api.openweathermap.org/data/2.5/weather?q=";
    public static String GRAD ;
    public static String KEY = "&APPID=1c8772c9f12179930d9c1659860c15dc";
    public String MEASURE_IN="&units=imperial";
    public String GET_CITY;
    private DBHelper dataBase;
    private WeatherData weatherData; //object where is put the last data for some city in case already exists
    private boolean mBound=false;

    private MyService mMyService;




    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mBound == true)
            unbindService(this);
        mBound = false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        dataBase=new DBHelper(this);



        WeatherData d1=new WeatherData("Belgrade",134, 67, 456,"4.35",
                "5:56", 2.9,"south","9 May 2019");
        WeatherData d2=new WeatherData("Belgrade",79, 27, 956,"4.35",
                "5:56", 1.8,"south","10 May 2019");
        WeatherData d3=new WeatherData("Belgrade",54, 12, 291,"4.35",
                "5:56", 1.5,"south","11 May 2019");
        WeatherData d4=new WeatherData("Belgrade",145, 24, 287,"4.35",
                "5:56", 2.5,"south","12 May 2019");
        WeatherData d5=new WeatherData("Belgrade",22, 77, 342,"4.35",
                "5:56", 3.5,"south","13 May 2019");
        WeatherData d6=new WeatherData("Belgrade",21, 81, 768,"4.35",
                "5:56", 6.5,"south","14 May 2019");
        WeatherData d7=new WeatherData("Belgrade",145, 99, 823,"4.35",
                "5:56", 6.5,"south","15 May 2019");
        dataBase.insert(d1);
        dataBase.insert(d2);
        dataBase.insert(d3);
        dataBase.insert(d4);
        dataBase.insert(d5);
        dataBase.insert(d6);
        dataBase.insert(d7);
        WeatherData z1=new WeatherData("Paris",76, 67, 921,"4.35",
                "5:56", 4.5,"south","9 May 2019");
        WeatherData z2=new WeatherData("Paris",301, 91, 1002,"4.35",
                "5:56", 4.7,"south","10 May 2019");
        WeatherData z3=new WeatherData("Paris",38, 37, 994,"4.35",
                "5:56", 4.1,"south","11 May 2019");
        WeatherData z4=new WeatherData("Paris",123, 29, 922,"4.35",
                "5:56", 4.2,"south","12 May 2019");
        WeatherData z5=new WeatherData("Paris",176, 62, 878,"4.35",
                "5:56", 3.0,"south","13 May 2019");
        WeatherData z6=new WeatherData("Paris",187, 81, 234,"4.35",
                "5:56", 4.9,"south","14 May 2019");
        WeatherData z7=new WeatherData("Paris",38, 42, 225,"4.35",
                "5:56", 4.1,"south","15 May 2019");
        dataBase.insert(z1);
        dataBase.insert(z2);
        dataBase.insert(z3);
        dataBase.insert(z4);
        dataBase.insert(z5);
        dataBase.insert(z6);
        dataBase.insert(z7);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        iSun=(ImageView)findViewById(R.id.img);
        tLup=(TextView) findViewById(R.id.lup);
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
        tLup.setVisibility(View.INVISIBLE);



        Bundle bundle = getIntent().getExtras();
        showtext = bundle.getString("City_name");

        tCity=(TextView)findViewById(R.id.city);
        tCity.setText(getString(R.string.location)+" "+showtext);

        Calendar localCalendar = Calendar.getInstance(TimeZone.getDefault());
        Date currentTime = localCalendar.getTime();
        int currentDayofWeek = localCalendar.get(Calendar.DAY_OF_WEEK);
        String day=getInSerbian(currentDayofWeek-1);
        tDay=(TextView)findViewById(R.id.day);
        //tDay.setText(getString(R.string.date)+" "+day);
        Intent intent_city = new Intent(DetailsActivity.this, MyService.class);
        intent_city.putExtra("city",showtext);


        values = getResources().getStringArray(R.array.value_arrays);
       adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, values);
        sVal.setAdapter(adapter);

        httpHelper=new HTTPHelper();

        GRAD=showtext;

        GET_CITY= BASE_URL + GRAD + KEY + MEASURE_IN;

        //Intent intent_url = new Intent(DetailsActivity.this, MyService.class);
        intent_city.putExtra("url",GET_CITY);

        if(dataBase.readFromDB(showtext)==null) {//if current city hasn't been searched
            setData();
        }else{//if data for the current city exists
            weatherData=dataBase.readFromDB(showtext);
            fillFields(weatherData);
        }

        bTemp=(Button)findViewById(R.id.temp);
        bSun=(Button)findViewById(R.id.sun);
        bWind=(Button)findViewById(R.id.wind);
        bStat=(Button)findViewById(R.id.stats);
        bServ=(Button)findViewById(R.id.stop_serv);
        ibRefr=(ImageButton)findViewById(R.id.refresh);
        bTemp.setOnClickListener(this);
        bSun.setOnClickListener(this);
        bWind.setOnClickListener(this);
        ibRefr.setOnClickListener(this);
        bStat.setOnClickListener(this);
        bServ.setOnClickListener(this);

        bindService(intent_city, this, Context.BIND_AUTO_CREATE);

        myNDK=new MyNDK();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 20 seconds
                WeatherData read = dataBase.readFromDB(showtext);
                fillFields(read);
                handler.postDelayed(this, 13000);
            }
        }, 13000);
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
                break;
            case R.id.refresh:
                sVal.setAdapter(adapter);
                setData();
                tLup.setVisibility(View.INVISIBLE);
                break;
            case R.id.stats:
                Intent intent = new Intent(DetailsActivity.this, StatisticsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("City_name", showtext);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.stop_serv:
                Intent intent_service1 = new Intent(this, MyService.class);
                unbindService(this);
                stopService(intent_service1);
                mBound = false;
                break;

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
    }


    public String toFahrenheit(double temp) {
        int helper=(int)(temp * 5/9 + 32);
        return String.valueOf(helper);
    }


    public String toCelsius(double temp) {
        int helper=(int)(((temp - 32) * 5) / 9);
        return String.valueOf(helper);
    }

    public void setData(){
        WeatherData weather;
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

                    JSONObject main = jsonobject.getJSONObject("main");
                    final String temperature=main.get("temp").toString();
                    final String pressure=main.get("pressure").toString();
                    final String humidity=main.get("humidity").toString();

                    JSONObject wind = jsonobject.getJSONObject("wind");
                    final String strength=wind.get("speed").toString();
                    final String temp=wind.get("deg").toString();
                    double value = Double.parseDouble(temp);
                    final String direction=convertDegrees(value);


                    runOnUiThread(new Runnable() {
                        public void run() {
                            //Log.d("tread",GET_CITY);
                            tSunR.setText(getString(R.string.sunRise)+" "+sunrise);
                            tSunS.setText(getString(R.string.sunSet)+" "+sunset);
                            double value = Double.parseDouble(temperature);
                            String temperatureHelp=String.valueOf(myNDK.convert((int)value,0));
                            tTemp.setText(getString(R.string.tempData)+" "+temperatureHelp+getString(R.string.fahr));
                            sVal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                // @Override
                                public void onItemSelected(AdapterView<?> arg0, View arg1,
                                                           int arg2, long arg3) {
                                    String selected=arg0.getItemAtPosition(arg2).toString();
                                    if(selected.equals("°C")){
                                        double value = Double.parseDouble(temperature);
                                        //String temperatureHelp=toCelsius(value);
                                        String temperatureHelp=String.valueOf(myNDK.convert((int)value,1));
                                        tTemp.setText(getString(R.string.tempData)+" "+temperatureHelp+getString(R.string.cels));
                                        Log.d("temp",temperatureHelp);

                                    }else{
                                        double value = Double.parseDouble(temperature);
                                        //String temperatureHelp=toFahrenheit(value);
                                        String temperatureHelp=String.valueOf(myNDK.convert((int)value,0));
                                        tTemp.setText(getString(R.string.tempData)+" "+temperatureHelp+getString(R.string.fahr));
                                    }
                                }

                                //@Override
                                public void onNothingSelected(AdapterView<?> arg0) {
                                    double value = Double.parseDouble(temperature);
                                    String temperatureHelp=String.valueOf(myNDK.convert((int)value,0));
                                    tTemp.setText(getString(R.string.tempData)+" "+temperatureHelp);
                                }
                            });
                            tPress.setText(getString(R.string.pressData)+" "+pressure+getString(R.string.bar));
                            tHum.setText(getString(R.string.humData)+" "+humidity+getString(R.string.percent));

                            tWindS.setText(getString(R.string.windStrength)+" "+strength);
                            tWindD.setText(getString(R.string.windDirection)+" "+direction);
                            String data_time=getCurrentDate();
                            tDay.setText(getString(R.string.date)+" "+data_time);
                            WeatherData weather=new WeatherData(showtext,Double.parseDouble(temperature),Double.parseDouble(humidity),Double.parseDouble(pressure),
                                    sunrise,sunset,Double.parseDouble(strength),direction.toString(),data_time);
                            dataBase.insert(weather);
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

    public void fillFields(WeatherData weatherData){
        String date=getCurrentDate();
        if(date.equals(weatherData.getDate())){
            tDay.setText(getString(R.string.date)+" "+weatherData.getDate());
            tLup.setVisibility(View.INVISIBLE);
        }else
        {
            tDay.setText(getString(R.string.date)+" "+weatherData.getDate());
            tLup.setVisibility(View.VISIBLE);
        }
        tSunR.setText(getString(R.string.sunRise)+" "+weatherData.getSunrise());
        tSunS.setText(getString(R.string.sunSet)+" "+weatherData.getSunset());
        //tTemp.setText(getString(R.string.tempData)+" "+weatherData.getTemperature());
        final double temperature= weatherData.getTemperature();
        sVal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            // @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                String selected=arg0.getItemAtPosition(arg2).toString();
                if(selected.equals("°C")){
                    //String temperatureHelp=toCelsius(temperature);
                    Log.d("temp",String.valueOf((int)temperature));
                    String temperatureHelp=String.valueOf(myNDK.convert((int)temperature,1));
                    tTemp.setText(getString(R.string.tempData)+" "+temperatureHelp+getString(R.string.cels));
                    Log.d("temp",temperatureHelp);

                }else{
                    //String temperatureHelp=toFahrenheit(temperature);
                    String temperatureHelp=String.valueOf(myNDK.convert((int)temperature,0));
                    tTemp.setText(getString(R.string.tempData)+" "+temperatureHelp+getString(R.string.fahr));
                }
            }

            //@Override
            public void onNothingSelected(AdapterView<?> arg0) {
                double value = temperature;
                String temperatureHelp=String.valueOf(myNDK.convert((int)temperature,1));
                tTemp.setText(getString(R.string.tempData)+" "+temperatureHelp);
            }
        });

        tPress.setText(getString(R.string.pressData)+" "+weatherData.getPressure()+getString(R.string.bar));
        tHum.setText(getString(R.string.humData)+" "+weatherData.getHumidity()+getString(R.string.percent));
        tWindS.setText(getString(R.string.windStrength)+" "+weatherData.getWindSpeed());
        tWindD.setText(getString(R.string.windDirection)+" "+weatherData.getWindDirection());

    }

    public String getCurrentDate(){
        Calendar time=Calendar.getInstance();
        SimpleDateFormat data=new SimpleDateFormat("dd MMM yyyy");
        String data_time=data.format(time.getTime());
        return data_time;
    }


    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        MyService.MyBinder binder = (MyService.MyBinder) service;
        mMyService = binder.getService();
        mBound = true;
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        mBound=false;
    }


}

package r.rtrk.weatherforecast;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteException;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MyService extends Service {

    private static final String LOG_TAG = "MyService";
    private static final long PERIOD = 5000L; //milliseconds
    private String city,URL;
    private HTTPHelper httpHelper;
    private DBHelper dbHelper;
    private Job mJob;



    public MyService() {
    }


    @Override
    public void onCreate() {
        super.onCreate();
        //mThread = new ThreadExample();

        httpHelper = new HTTPHelper();
        dbHelper = new DBHelper(this);
        mJob = new Job();
        mJob.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //mThread.exit();
        mJob.stop();
    }


    public class MyBinder extends Binder {
        MyService getService() {
            // Return this instance of LocalService so clients can call public methods
            return MyService.this;
        }
    }
    @Override
    public IBinder onBind(Intent intent) {
        IBinder binder=new MyBinder();

        city=intent.getStringExtra("city");
        URL=intent.getStringExtra("url");

        return binder;
    }

    /*@Override
    public boolean onUnbind(Intent intent) {
        mMyBinder.stop();
        return super.onUnbind(intent);
    }*/
    private class Job implements Runnable {

        private Handler mHandler = null;
        private boolean mRun = true;


        public void start() {
            mHandler = new Handler(Looper.getMainLooper());
            mHandler.postDelayed(this, PERIOD);
        }

        public void stop() {
            mRun = false;
            mHandler.removeCallbacks(this);
        }

        @Override
        public void run() {
            if (!mRun) {
                return;
            }
            new Thread(new Runnable() {
                public void run() {
                    try {
                        //Log.d("tread",GET_CITY);
                        JSONObject jsonobject = httpHelper.getJSONObjectFromURL(URL);
                        JSONObject sys = jsonobject.getJSONObject("sys");

                        long rise=Long.valueOf(sys.get("sunrise").toString())*1000;
                        Date date1=new Date(rise);
                        final String sunrise=new SimpleDateFormat("hh:mma", Locale.ENGLISH).format(date1);

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

                        String data_time=getCurrentDate();


                        WeatherData weather=new WeatherData(city,Double.parseDouble(temperature),Double.parseDouble(humidity),Double.parseDouble(pressure),
                                sunrise,sunset,Double.parseDouble(strength),direction.toString(),data_time);
                        dbHelper.insert(weather);


                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }).start();




            WeatherData read = dbHelper.readFromDB(city);

            NotificationCompat.Builder nb = new NotificationCompat.Builder(MyService.this);
            nb.setAutoCancel(true)
                    .setDefaults(NotificationCompat.DEFAULT_ALL)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.drawable.ikona)
                    .setTicker("Vremenska prognoza")
                    .setContentTitle("Temperatura je azurirana, " + read.getTemperature())
                    .setContentText(" °C")
                    .setContentInfo("INFO");

            NotificationManager manager = (NotificationManager) MyService.this.getSystemService(Context.NOTIFICATION_SERVICE);
            manager.notify(1, nb.build());

            mHandler.postDelayed(this, PERIOD);
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

    public String getCurrentDate(){
        Calendar time=Calendar.getInstance();
        SimpleDateFormat data=new SimpleDateFormat("dd MMM yyyy");
        String data_time=data.format(time.getTime());
        return data_time;
    }



}

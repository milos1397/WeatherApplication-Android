package r.rtrk.weatherforecast;

import android.database.Cursor;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class StatisticsActivity extends AppCompatActivity implements View.OnClickListener{
    private String city;
    private ListView list;
    private TextView tCity;
    private DBHelper dataBase;
    private TextView tPon,tUto,tSre,tCet,tPet,tSub,tNed,tMin,tMax,tIzn,tIsp,tPress;
    private ImageButton ibHlad,ibTopl,ibPress;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        dataBase = new DBHelper(this);

        Log.d("grad","ovde");

        tPon = (TextView) findViewById(R.id.pon);
        tUto = (TextView) findViewById(R.id.uto);
        tSre = (TextView) findViewById(R.id.sre);
        tCet = (TextView) findViewById(R.id.cet);
        tPet = (TextView) findViewById(R.id.pet);
        tSub = (TextView) findViewById(R.id.sub);
        tNed = (TextView) findViewById(R.id.ned);
        tMin=(TextView)findViewById(R.id.mintemp);
        tMax=(TextView)findViewById(R.id.maxtemp);
        tIzn=(TextView)findViewById(R.id.iznad);
        tIsp=(TextView)findViewById(R.id.ispod);
        tPress=(TextView)findViewById(R.id.press);
        tIzn.setVisibility(View.INVISIBLE);
        tIsp.setVisibility(View.INVISIBLE);
        tPress.setVisibility(View.INVISIBLE);
        ibHlad=(ImageButton)findViewById(R.id.pahulja);
        ibTopl=(ImageButton)findViewById(R.id.sunce);
        ibPress=(ImageButton)findViewById(R.id.pritisak);
        ibHlad.setOnClickListener(this);
        ibTopl.setOnClickListener(this);
        ibPress.setOnClickListener(this);



        tCity = (TextView) findViewById(R.id.location);
        Bundle bundle = getIntent().getExtras();
        city = bundle.getString("City_name");
        Log.d("grad",city);
        tCity.setText(getString(R.string.location) + " " + city);

        Log.d("grad",city);

        Calendar time=Calendar.getInstance();
        SimpleDateFormat data=new SimpleDateFormat("dd MMM yyyy");
        String data_time=data.format(time.getTime());

        cursor = dataBase.getCity(city);//vraca mi cursor svi dana za gradove
        for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            String date=cursor.getString(1);;
            int value=getDayoFWeek(date);
            String day=dayOfWeek(value);
            //Log.d("dan",day);
            put(day,cursor);
        }

        boldTV();

        min(cursor);
        max(cursor);
    }

    public int getDayoFWeek(String date){

        int dayOfWeek;
        SimpleDateFormat format = new SimpleDateFormat("dd MMM yyyy");
        try {
            Date d = format.parse(date);
            Calendar c = Calendar.getInstance();
            c.setTime(d); // yourdate is an object of type Date

            dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
            Log.d("dan",String.valueOf(dayOfWeek));
            System.out.println(date);
            return dayOfWeek+1;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public String dayOfWeek(int d){
        if(d==2){
            return getString(R.string.sunday);
        }else if(d==3){
            return getString(R.string.monday);
        }else if(d==4){
            return getString(R.string.tuesday);
        }else if(d==5){
            return getString(R.string.wednesday);
        }else if(d==6){
            return getString(R.string.thursday);
        }else if(d==7){
            return getString(R.string.friday);
        }else
            return getString(R.string.saturday);
    }
    public void put(String day,Cursor cursor){
        if(day.equals(getString(R.string.monday))){
           // Log.d("dan",day);
            tPon.setText(getString(R.string.monday)+"   "+cursor.getDouble(2)+"         "+cursor.getDouble(3)+"          "+cursor.getDouble(4));
        }else if(day.equals(getString(R.string.tuesday))){
            tUto.setText(getString(R.string.tuesday)+"           "+cursor.getDouble(2)+"           "+cursor.getDouble(3)+"              "+cursor.getDouble(4));
        }else if(day.equals(getString(R.string.wednesday))){
            tSre.setText(getString(R.string.wednesday)+"            "+cursor.getDouble(2)+"         "+cursor.getDouble(3)+"              "+cursor.getDouble(4));
        }else if(day.equals(getString(R.string.thursday))){
            tCet.setText(getString(R.string.thursday)+"        "+cursor.getDouble(2)+"         "+cursor.getDouble(3)+"              "+cursor.getDouble(4));
        }else if(day.equals(getString(R.string.friday))){
            tPet.setText(getString(R.string.friday)+"             "+cursor.getDouble(2)+"           "+cursor.getDouble(3)+"              "+cursor.getDouble(4));
        }else if(day.equals(getString(R.string.saturday))){
            tSub.setText(getString(R.string.saturday)+"          "+cursor.getDouble(2)+"            "+cursor.getDouble(3)+"             "+cursor.getDouble(4));
        }else{
            tNed.setText(getString(R.string.sunday)+"         "+cursor.getDouble(2)+"           "+cursor.getDouble(3)+"             "+cursor.getDouble(4));
        }
    }

    public void min(Cursor cursor){
        double min=1000;
        for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            if(cursor.getDouble(2)<min){
                min=cursor.getDouble(2);
            }
        }
        for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            if(cursor.getDouble(2)==min){
                String date=cursor.getString(1);;
                int value=getDayoFWeek(date);
                String day=dayOfWeek(value);
                String temp=String.valueOf(cursor.getDouble(2));
                tMin.append(day +" "+ temp+"\n");
            }
        }

    }

    public void max(Cursor cursor){
        double max=0;
        for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            if(cursor.getDouble(2)>max){
                max=cursor.getDouble(2);
            }
        }
        for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            if(cursor.getDouble(2)==max){
                String date=cursor.getString(1);;
                int value=getDayoFWeek(date);
                String day=dayOfWeek(value);
                tMax.append(day + " "+ String.valueOf(cursor.getDouble(2))+"\n");
            }
        }
    }


    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.pahulja:
                setInvisibility();
                for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                    if(cursor.getDouble(2)<100){
                        String date=cursor.getString(1);;
                        int value=getDayoFWeek(date);
                        String day=dayOfWeek(value);
                        setDay(day);
                    }
                }
                break;
            case R.id.sunce:
                setInvisibility();
                for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                    if(cursor.getDouble(2)>100){
                        String date=cursor.getString(1);;
                        int value=getDayoFWeek(date);
                        String day=dayOfWeek(value);
                        setDay(day);
                    }
                }
                break;
            case R.id.pritisak:
                setInvisibility();
                for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                    if (cursor.getDouble(3) > 500) {
                        String date = cursor.getString(1);;
                        int value = getDayoFWeek(date);
                        String day = dayOfWeek(value);
                        setDay(day);
                    }
                }
        }
    }
    public void boldTV(){
        Calendar time=Calendar.getInstance();
        SimpleDateFormat data=new SimpleDateFormat("dd MMM yyyy");
        String data_time=data.format(time.getTime());
        int value=getDayoFWeek(data_time);
        String day=dayOfWeek((value));
        Typeface boldTypeface = Typeface.defaultFromStyle(Typeface.BOLD);


        if(day.equals(getString(R.string.monday))){
            // Log.d("dan",day);
            tPon.setTypeface(boldTypeface);
        }else if(day.equals(getString(R.string.tuesday))){
            tUto.setTypeface(boldTypeface);
        }else if(day.equals(getString(R.string.wednesday))){
            tSre.setTypeface(boldTypeface);
        }else if(day.equals(getString(R.string.thursday))){
            tCet.setTypeface(boldTypeface);
        }else if(day.equals(getString(R.string.friday))){
            tPet.setTypeface(boldTypeface);
        }else if(day.equals(getString(R.string.saturday))){
            tSub.setTypeface(boldTypeface);
        }else{
            tNed.setTypeface(boldTypeface);
        }

    }

    public void setDay(String value){
        if(value.equals(getString(R.string.monday))){
            tPon.setVisibility(View.VISIBLE);
        }else if(value.equals(getString(R.string.tuesday))){
            tUto.setVisibility(View.VISIBLE);
        }else if(value.equals(getString(R.string.wednesday))){
            tSre.setVisibility(View.VISIBLE);
        }else if(value.equals(getString(R.string.thursday))){
            tCet.setVisibility(View.VISIBLE);
        }else if(value.equals(getString(R.string.friday))){
            tPet.setVisibility(View.VISIBLE);
        }else if(value.equals(getString(R.string.saturday))){
            tSub.setVisibility(View.VISIBLE);
        }else{
            tNed.setVisibility(View.VISIBLE);
        }

    }

    public void setInvisibility(){
        tPon.setVisibility(View.INVISIBLE);
        tUto.setVisibility(View.INVISIBLE);
        tSre.setVisibility(View.INVISIBLE);
        tCet.setVisibility(View.INVISIBLE);
        tPet.setVisibility(View.INVISIBLE);
        tSub.setVisibility(View.INVISIBLE);
        tNed.setVisibility(View.INVISIBLE);
    }


}

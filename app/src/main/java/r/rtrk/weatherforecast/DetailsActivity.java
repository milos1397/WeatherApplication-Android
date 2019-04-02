package r.rtrk.weatherforecast;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DetailsActivity extends AppCompatActivity implements View.OnClickListener {

    TextView tCity,tDay,tTemp,tPress,tWind,tSun,tHum,tVal;
    ImageView iSun;
    Button bTemp,bSun,bWind;
    Spinner sVal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        iSun=(ImageView)findViewById(R.id.img);
        tVal=(TextView) findViewById(R.id.value);
        tTemp=(TextView) findViewById(R.id.dataTemp);
        tPress=(TextView)findViewById(R.id.dataPress);
        tWind=(TextView)findViewById(R.id.dataWind);
        tSun=(TextView)findViewById(R.id.dataSun);
        sVal=(Spinner)findViewById(R.id.tempOpt);
        tHum=(TextView)findViewById(R.id.dataHum);
        iSun.setVisibility(View.INVISIBLE);
        tTemp.setVisibility(View.INVISIBLE);
        tPress.setVisibility(View.INVISIBLE);
        tWind.setVisibility(View.INVISIBLE);
        tSun.setVisibility(View.INVISIBLE);
        sVal.setVisibility(View.INVISIBLE);
        tHum.setVisibility(View.INVISIBLE);
        tVal.setVisibility(View.INVISIBLE);




        Bundle bundle = getIntent().getExtras();
        String showtext = bundle.getString("City_name");

        tCity=(TextView)findViewById(R.id.city);
        tCity.setText("Lokacija: "+ showtext);

        Calendar localCalendar = Calendar.getInstance(TimeZone.getDefault());
        Date currentTime = localCalendar.getTime();
        int currentDayofWeek = localCalendar.get(Calendar.DAY_OF_WEEK);
        String day=getInSerbian(currentDayofWeek-1);
        tDay=(TextView)findViewById(R.id.day);
        tDay.setText("Dan: "+day);

        bTemp=(Button)findViewById(R.id.temp);
        bSun=(Button)findViewById(R.id.sun);
        bWind=(Button)findViewById(R.id.wind);
        bTemp.setOnClickListener(this);
        bSun.setOnClickListener(this);
        bWind.setOnClickListener(this);





    }

    @Override
    public void onClick(View v) {
        iSun=(ImageView)findViewById(R.id.img);
        tTemp=(TextView) findViewById(R.id.dataTemp);
        tPress=(TextView)findViewById(R.id.dataPress);
        tHum=(TextView)findViewById(R.id.dataHum);
        tSun=(TextView)findViewById(R.id.dataSun);
        tWind=(TextView)findViewById(R.id.dataWind);
        sVal=(Spinner)findViewById(R.id.tempOpt);
        tVal=(TextView) findViewById(R.id.value);
        switch (v.getId()){
            case R.id.sun:
                tSun.setVisibility(View.VISIBLE);
                iSun.setVisibility(View.INVISIBLE);
                tTemp.setVisibility(View.INVISIBLE);
                tPress.setVisibility(View.INVISIBLE);
                tWind.setVisibility(View.INVISIBLE);
                sVal.setVisibility(View.INVISIBLE);
                tHum.setVisibility(View.INVISIBLE);
                tVal.setVisibility(View.INVISIBLE);
                break;
            case R.id.temp:
                tSun.setVisibility(View.INVISIBLE);
                iSun.setVisibility(View.VISIBLE);;
                tTemp.setVisibility(View.VISIBLE);
                tPress.setVisibility(View.VISIBLE);;
                tWind.setVisibility(View.INVISIBLE);
                tHum.setVisibility(View.VISIBLE);
                sVal.setVisibility(View.VISIBLE);
                tVal.setVisibility(View.VISIBLE);
                break;
            case R.id.wind:
                iSun.setVisibility(View.INVISIBLE);
                tTemp.setVisibility(View.INVISIBLE);
                tPress.setVisibility(View.INVISIBLE);
                tWind.setVisibility(View.VISIBLE);
                sVal.setVisibility(View.INVISIBLE);
                tSun.setVisibility(View.INVISIBLE);
                tHum.setVisibility(View.INVISIBLE);
                tVal.setVisibility(View.INVISIBLE);
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
}

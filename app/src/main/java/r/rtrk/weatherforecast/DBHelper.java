package r.rtrk.weatherforecast;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DBHelper extends SQLiteOpenHelper implements BaseColumns {

    public static final String DATABASE_NAME = "weather.db";
    public static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "weather";
    public static final String COLUMN_CITY = "Name";
    public static final String COLUMN_DATE = "Date";
    public static final String COLUMN_WEEKDAY = "Weekday";
    public static final String COLUMN_TEMPERATURE = "Temperature";
    public static final String COLUMN_PRESSURE = "Pressure";
    public static final String COLUMN_HUMIDITY = "Humidity";
    public static final String COLUMN_SUNRISE = "Sunrise";
    public static final String COLUMN_SUNSET = "Sunset";
    public static final String COLUMN_WIND_SPEED = "WindSpeed";
    public static final String COLUMN_WIND_DIRECTION = "WindDirection";


    public DBHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_CITY + " TEXT," +
                COLUMN_DATE + " TEXT," +
                COLUMN_TEMPERATURE + " DOUBLE," +
                COLUMN_PRESSURE + " DOUBLE," +
                COLUMN_HUMIDITY + " DOUBLE," +
                COLUMN_SUNRISE + " TEXT," +
                COLUMN_SUNSET + " TEXT," +
                COLUMN_WIND_SPEED + " DOUBLE," +
                COLUMN_WIND_DIRECTION + " TEXT);";
        db.execSQL(SQL_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insert(WeatherData weatherData) {
        SQLiteDatabase dbWrite = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_CITY, weatherData.getCity());
        contentValues.put(COLUMN_DATE, weatherData.getDate());
        contentValues.put(COLUMN_TEMPERATURE, weatherData.getTemperature());
        contentValues.put(COLUMN_PRESSURE, weatherData.getPressure());
        contentValues.put(COLUMN_HUMIDITY, weatherData.getHumidity());
        contentValues.put(COLUMN_SUNRISE, weatherData.getSunrise());
        contentValues.put(COLUMN_SUNSET, weatherData.getSunset());
        contentValues.put(COLUMN_WIND_SPEED, weatherData.getWindSpeed());
        contentValues.put(COLUMN_WIND_DIRECTION, weatherData.getWindDirection());

        boolean in;
        in=inDB(weatherData.getCity(),weatherData.getDate());
        if(!in) {
            dbWrite.insert(TABLE_NAME, null, contentValues);
        }else if(weatherData.getDate().equals(getCurrentDate())){
           remove(weatherData.getCity(),weatherData.getDate());
           dbWrite.insert(TABLE_NAME, null, contentValues);
        }
        dbWrite.close();
    }

    public boolean inDB(String city,String date){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor=db.rawQuery(" SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_CITY + " = \"" + city +"\" AND " + COLUMN_DATE + " = \"" + date + "\";",null,null);

        if(cursor.getCount()<=0){
            return false;
        }
       return true;
    }

    public void remove(String city,String date){
        SQLiteDatabase db=this.getReadableDatabase();
        db.delete(TABLE_NAME, COLUMN_CITY+" = ? AND "+ COLUMN_DATE+ " = ?", new String[]{city, date});
    }

    public WeatherData readFromDB(String city){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor=db.rawQuery(" SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_CITY + " = \"" + city +"\";"
                ,null,null);
        if(cursor.getCount()<=0){
            return null;
        }
        cursor.moveToLast();
        WeatherData weatherData=new WeatherData(cursor.getString(0),cursor.getDouble(2),cursor.getDouble(4),
        cursor.getDouble(3),cursor.getString(5),cursor.getString(6),cursor.getDouble(7),
                cursor.getString(8),cursor.getString(1));
        db.close();
        return weatherData;
    }

    public Cursor getCity(String city){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor=db.rawQuery(" SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_CITY + " = \"" + city +"\";"
                ,null,null);
        return cursor;
    }


    public String getCurrentDate(){
        Calendar time=Calendar.getInstance();
        SimpleDateFormat data=new SimpleDateFormat("dd MMM yyyy");
        String data_time=data.format(time.getTime());
        return data_time;
    }

}

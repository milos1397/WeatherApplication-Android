package r.rtrk.weatherforecast;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

public class City {
    public RadioButton mCheck;
    public String mName;
    public Drawable mImage;

    public City(String name,Drawable image){
        mName=name;
        mImage=image;
    }

}

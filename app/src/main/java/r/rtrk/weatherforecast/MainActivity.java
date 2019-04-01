package r.rtrk.weatherforecast;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{


    Button show;
    EditText loc;
    String cityName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        show=(Button)findViewById(R.id.show);
        //loc=(EditText)findViewById(R.id.city);
        show.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.show:
                Bundle location = new Bundle();
                loc=(EditText)findViewById(R.id.city);
                String cityName=loc.getText().toString();
                location.putString("City_name",cityName);
                Intent onShowClick=new Intent(MainActivity.this,DetailsActivity.class);
                onShowClick.putExtras(location);
                MainActivity.this.startActivity(onShowClick);
        }
    }
}

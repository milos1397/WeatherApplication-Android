package r.rtrk.weatherforecast;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{


    private Button add;
    private EditText loc;
    private CustomAdapter adapter;
    private ListView list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        add=(Button)findViewById(R.id.add);
        add.setOnClickListener(this);



        adapter = new CustomAdapter(this);
        adapter.addCity(new City(getString(R.string.belgrade)));
        adapter.addCity(new City(getString(R.string.zagreb)));
        adapter.addCity(new City(getString(R.string.sarajevo)));

        list = (ListView) findViewById(R.id.cityList);
        list.setAdapter(adapter);
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int pos, long id) {
                adapter.removeCity(pos);
                return true;
            }
        });
    }

    @Override
    public void onClick(View v) {
        list.setAdapter(adapter);
        switch(v.getId()){
            case R.id.add:
                loc=(EditText)findViewById(R.id.loc);
                String text=loc.getText().toString();
                loc.getText().clear();
                adapter.addCity(new City(text));
        }
    }
}

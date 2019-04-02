package r.rtrk.weatherforecast;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Iterator;

public class CustomAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<City> mCity;
    private RadioButton rBttn;

    public CustomAdapter(Context context){
        mContext=context;
        mCity=new ArrayList<City>();
    }

    public void addCity(City city) {

        int i;
        boolean inList=false;
        for (City c : mCity) {
            if (c.mName.equals(city.mName) || city.mName.equals("")) {
                inList = true;
                break;
            }
        }
        if(inList==false) {
            mCity.add(city);
            //Toast.makeText()
        }
        notifyDataSetChanged();
    }

    public void removeCity(int position) {
        mCity.remove(position);
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return mCity.size();
    }

    @Override
    public Object getItem(int position) {
        Object rv = null;
        try {
            rv = mCity.get(position);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }

        return rv;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;

        if(view == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.element_row, null);
            ViewHolder holder = new ViewHolder();
            holder.check = (RadioButton) view.findViewById(R.id.checkCity);
            holder.name = (TextView) view.findViewById(R.id.cityName);
            view.setTag(holder);
        }

        final City mCity= (City) getItem(position);
        ViewHolder holder = (ViewHolder) view.getTag();
        //holder.image.setImageDrawable(character.mImage);
        holder.name.setText(mCity.mName);
        holder.check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, DetailsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("City_name", mCity.mName.toString());
                //bundle.putInt("ContactID", contactsRow.getId());
                intent.putExtras(bundle);
                mContext.startActivity(intent);
                rBttn=(RadioButton)v;
                rBttn.setChecked(false);
            }

        });


        return view;
    }
    private class ViewHolder {
        public RadioButton check = null;
        public TextView name = null;

    }
}

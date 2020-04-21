package com.example.myapplication;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class listAdapter extends ArrayAdapter<String> {
    private Context context;
    private List<String> quakeFiller;
    LinearLayout adapLayer;
    listAdapter(Context context, List<String> quakes){
        super(context, R.layout.custom_row,quakes);
        this.context = context;
        this.quakeFiller= quakes;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater krisInflater = LayoutInflater.from(getContext());
        View customView = krisInflater.inflate(R.layout.custom_row, parent, false);

        TextView titlereplace = (TextView) customView.findViewById(R.id.titleof);
        TextView timereplace = (TextView) customView.findViewById(R.id.timeof);
        TextView urlreplace = (TextView) customView.findViewById(R.id.urlof);
        TextView magreplace = (TextView) customView.findViewById(R.id.magof);


        String useThis = this.quakeFiller.get(position);
        String[] quakeInfo = useThis.split("\\$");

        Long setterTime = Long.parseLong(quakeInfo[0]);
        DateFormat edtFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");

        String setWhee = edtFormat.format(setterTime);


        titlereplace.setText(quakeInfo[1]);

        timereplace.setText(setWhee); //CONVERT THIS NUMBER

        urlreplace.setText(quakeInfo[2]);
        magreplace.setText(quakeInfo[3]);

        Double check = Double.parseDouble(quakeInfo[3]);

        adapLayer = customView.findViewById(R.id.customzz);

        if(check > 7.5 || check == 7.5){
            adapLayer.setBackgroundColor(Color.parseColor("#ff4444"));

        }
        else if(check < 7.5){
            adapLayer.setBackgroundColor(Color.parseColor("#aa66cc"));
        }

        int test = 0;

        return customView;
    }
}

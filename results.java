package com.example.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.List;

public class results extends AppCompatActivity {
    ListView listMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        Intent resultsPass = getIntent();
        final String stringURL = resultsPass.getStringExtra("urlPass");

       new QuakesSync().execute(stringURL);

    }
    private void update(List<String> quakes){

        listMain = findViewById(R.id.quakesList);
        ListAdapter listAdap = new listAdapter(this, quakes);

        listMain.setAdapter (listAdap);

        listMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String url = (String)((TextView)view.findViewById(R.id.urlof)).getText();
                String location = (String)((TextView)view.findViewById(R.id.titleof)).getText();
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);


            }
        });

    }

    private class QuakesSync extends AsyncTask<String, Void, List<String>> {
        @Override
        protected List<String> doInBackground(String... urls) {
            //if theres no URL passed
            if (urls.length < 1 || urls[0] == null) {
                return null;
            }
            return Utilities.fetchEarthquake(urls[0]);
        }

        @Override
        protected void onPostExecute(List<String> quakes) {
            if (quakes == null) {
                return;
            }
            //else call the list to set
            update(quakes);
        }

    }
}

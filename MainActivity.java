package com.example.myapplication;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity  {
    TextView dateEdit;
    Spinner orderField;
    Button dateButton, submitButton;
    EditText numberGet;
    int mYear;
    int mMonth;
    int mDay;
    static final int DATE_DIALOG_ID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dateEdit = findViewById(R.id.dateFiller);
        orderField = findViewById(R.id.spinnerOrder);
        dateButton = findViewById(R.id.dateButt);
        submitButton = findViewById(R.id.submitButt);
        numberGet = findViewById(R.id.numberField);

        final ArrayAdapter<CharSequence> adapSpin = ArrayAdapter.createFromResource(this,R.array.sorted, android.R.layout.simple_spinner_item);
        adapSpin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        orderField.setAdapter(adapSpin);
        orderField.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DATE_DIALOG_ID);

            }
        });
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        // display the current date
        updateDisplay();

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sortSelection = orderField.getSelectedItem().toString();
                String numberSelection = numberGet.getText().toString();
                String startDate = dateEdit.getText().toString();
                StringBuilder urls = new StringBuilder();
                urls.append("https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&minmagnitude=7&");
                urls.append("limit="+numberSelection);
                if(sortSelection.equals("magnitude")) {
                    urls.append("&orderby=" + sortSelection + "&");
                }
                else{
                    urls.append("&orderby=time&");

                }
                urls.append("starttime="+startDate);
               // urls.append(startDate);

                Intent resultsIntent = new Intent(getApplicationContext(), results.class);
                resultsIntent.putExtra("urlPass", urls.toString());
                startActivity(resultsIntent);
            }
        });

    }
    private void updateDisplay() {
        this.dateEdit.setText(
                new StringBuilder()
                        // Month is 0 based so add 1
                        .append(mYear).append("-")
                        .append(mMonth +1).append("-")
                        .append(mDay).append(" "));
    }

    private DatePickerDialog.OnDateSetListener mDateSetListener =
            new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker view, int year,
                                      int monthOfYear, int dayOfMonth) {
                    mYear = year;
                    mMonth = monthOfYear;
                    mDay = dayOfMonth;
                    updateDisplay();
                }
            };
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
                return new DatePickerDialog(this,
                        mDateSetListener,
                        mYear, mMonth, mDay);
        }
        return null;
    }
}

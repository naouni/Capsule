package com.example.nordineaouni.Capsule;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;

import static android.R.id.edit;

/**
 * Created by nordineaouni on 12/02/17.
 */

public class DeliveryDateTimeActivity extends AppCompatActivity implements
        DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener {

    final String TAG = getClass().toString();

    private TextView dateField;
    private TextView timeField;
    private Calendar dateAndTimeSetterCalendar;
    //Use to transform numbers representations of dates into string representation
    SimpleDateFormat dateFormat;
    SimpleDateFormat timeFormat;

    public void onCreate(Bundle savedInstancanceState){
        super.onCreate(savedInstancanceState);
        Log.d(TAG, "onCreate");
        setContentView(R.layout.activity_delivery_date_time);

        dateField = (TextView) findViewById(R.id.deliveryDateTime_activity_dateField);
        timeField = (TextView) findViewById(R.id.deliveryDateTime_activity_timeField);

        //used to initialize the textFields with current date and time
        Calendar calendar = Calendar.getInstance();
        //only used to set date and time gotten from dialogs into the textFields
        dateAndTimeSetterCalendar = Calendar.getInstance();

        //Display current date in the dateField
        Date currentDate = calendar.getTime();
        dateFormat = new SimpleDateFormat("EEE, MMM d, ''yyyy");
        String currentDateString = dateFormat.format(currentDate);
        dateField.setText(currentDateString);

        //Display current time in the timeField
        timeFormat = new SimpleDateFormat("h:mm a");
        String currentTimeString = timeFormat.format(currentDate);
        timeField.setText(currentTimeString);

        //Set listeners
        dateField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Open a date picker.
                //DeliveryDateTimeActivity.this makes sure to get the instance of this class having
                // instantiate this inner class object
                DialogFragment datePickerFragment = new DatePickerFragment(DeliveryDateTimeActivity.this);
                datePickerFragment.show(getSupportFragmentManager(), "deliveryDateTime_activity_datePicker");
            }
        });
        timeField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Open a time picker.
                //DeliveryDateTimeActivity.this makes sure to get the instance of this class having
                // instantiate this inner class object
                DialogFragment timePickerFragment =  new TimePickerFragment(DeliveryDateTimeActivity.this);
                timePickerFragment.show(getSupportFragmentManager(), "deliveryDateTime_activity_timePicker");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_delivery_date_time_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.nextToMapActivity:

                // Start next activity in the creation process and pass data along
                Intent intent = new Intent(this, MapActivity.class);

                String text = getIntent().getStringExtra("text");

                // We have to send the date as a long.
                Date openingDate = dateAndTimeSetterCalendar.getTime();
                long openingDateAsNumber = openingDate.getTime();

                intent.putExtra("text", text);
                intent.putExtra("openingDate", openingDateAsNumber);

                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        //Set date parameters of the calendar
        dateAndTimeSetterCalendar.set(year, month, day);

        //Update the displayed date
        Date openingDate = dateAndTimeSetterCalendar.getTime();
        String openingDateString = dateFormat.format(openingDate);
        dateField.setText(openingDateString);
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
        //Set time of the day parameters of the calendar
        dateAndTimeSetterCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        dateAndTimeSetterCalendar.set(Calendar.MINUTE, minute);

        //Update the displayed time
        Date openingDate = dateAndTimeSetterCalendar.getTime();
        String openingTime = timeFormat.format(openingDate);
        timeField.setText(openingTime);
    }
}
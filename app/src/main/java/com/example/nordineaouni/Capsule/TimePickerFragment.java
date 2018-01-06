package com.example.nordineaouni.Capsule;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.TimePicker;
import java.util.Calendar;

/**
 * Created by nordineaouni on 31/03/17.
 */

public class TimePickerFragment extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {

    //The callBack activity has to implement the TimePickerDialog.OnTimeSetListener that contains
    // only the method onTimeSet.
    TimePickerDialog.OnTimeSetListener callBack;

    public TimePickerFragment(TimePickerDialog.OnTimeSetListener callBack){
        //Attach callBack
        this.callBack = callBack;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        // Use the callBack corresponding function to eventually send the capsule
        callBack.onTimeSet(view, hourOfDay, minute);
    }
}
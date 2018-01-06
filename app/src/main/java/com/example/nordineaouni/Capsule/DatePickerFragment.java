package com.example.nordineaouni.Capsule;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.lang.reflect.Method;
import java.util.Calendar;

/**
 * Created by nordineaouni on 11/01/17.
 */

public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    //The callBack has to implement the DatePickerDialog.OnDateSetListener interface. This interface
    // only contains the method onDateSet.
    DatePickerDialog.OnDateSetListener callBack;


    public DatePickerFragment(DatePickerDialog.OnDateSetListener callBack){
        //Attach callBack
        this.callBack = callBack;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        //Create a new instance of DatePickerDialog and return it.
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    /**
     *  Method from the interface DatePickerDialog.OnDateSetListener. This method gets called when
     *  the user click the set button of the datePicker dialog. Then, this method pass the date
     *  information to its callBack which is an instance of ChatActivity. This instance of
     *  ChatActivity will store the date and show a timePicker in order to continue the process.
     */
    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Pass the chosen date to the callBack function
        callBack.onDateSet(view, year, month, day);
    }
}

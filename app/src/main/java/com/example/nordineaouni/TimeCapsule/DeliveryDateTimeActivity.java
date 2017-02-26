package com.example.nordineaouni.TimeCapsule;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * Created by nordineaouni on 12/02/17.
 */

public class DeliveryDateTimeActivity extends AppCompatActivity {

        final String TAG = getClass().toString();

        public void onCreate(Bundle savedInstancanceState){
            super.onCreate(savedInstancanceState);
            Log.d(TAG, "onCreate" );
            setContentView(R.layout.deliverydatetime);
        }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();

    }

}
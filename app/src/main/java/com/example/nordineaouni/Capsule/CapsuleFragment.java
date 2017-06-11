package com.example.nordineaouni.Capsule;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static android.app.Activity.RESULT_OK;

/**
 * Created by nordineaouni on 06/01/17.
 */

public class CapsuleFragment extends Fragment implements View.OnClickListener{

    final String TAG = getClass().toString();
    CapsulePageFragmentListener mCallback;

    private FirebaseAuth mAuth;
    private DatabaseReference mMessageTextRef;
    private DatabaseReference mMetadataRef;

    //Interface to comunicate with the main activity
    public interface CapsulePageFragmentListener{
        public void onPictureTaken(Bitmap imageBitmap);
    }

    public static CapsuleFragment newInstance() {
        CapsuleFragment pageFragment = new CapsuleFragment();
        return pageFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        mMessageTextRef = FirebaseDatabase.getInstance().getReference().child("messagesText").child(mAuth.getCurrentUser().getUid());
        mMetadataRef = FirebaseDatabase.getInstance().getReference().child("messagesMetadata").child(mAuth.getCurrentUser().getUid());
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.capsule_fragment, container, false);

        Button sendButton = (Button) view.findViewById(R.id.createCapsuleButton);
        sendButton.setOnClickListener(this);

        Button attachPictureButton = (Button) view.findViewById(R.id.attachPictureButton);
        attachPictureButton.setOnClickListener(this);

        Button takePictureButton = (Button) view.findViewById(R.id.takePictureButton);
        takePictureButton.setOnClickListener(this);

        Button checkInButton = (Button) view.findViewById(R.id.checkInButton);
        checkInButton.setOnClickListener(this);

        return view;
    }


    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (CapsuleFragment.CapsulePageFragmentListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement onCapsulePageFragmentListener");
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        inflater.inflate(R.menu.menu_capsule_fragment, menu);
        super.onCreateOptionsMenu(menu,inflater);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.friends_capsule_fragment:
                Intent intent = new Intent(getContext(), FriendsActivity.class);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v){
        EditText capsuleMessage = (EditText) getView().findViewById(R.id.capsuleMessage);
        switch (v.getId()) {
            case R.id.createCapsuleButton:

               /*
                //Create message metadata
                String interlocutorID = "MlvVqCcuJDeF1myoa9MW5O1E0B52";
                Calendar c = Calendar.getInstance();
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                String sentDate = dateFormat.format(c.getTime());
                //Get timePicker date as deliveryDate
                DatePicker date = (DatePicker) getView().findViewById(R.id.datePicker);
                int day = date.getDayOfMonth();
                int month = date.getMonth()+1;
                int year =  date.getYear();
                String deliveryDate = Integer.toString(day)+"/"+Integer.toString(month)+"/"+Integer.toString(year);

                MessageMetadata metadata = new MessageMetadata( (long) 1, interlocutorID, sentDate, deliveryDate);

                //Create message text
                MessageText text = new MessageText(capsuleMessage.getText().toString());

                //Create a unique key to reference both text and meta
                String key =  mMetadataRef.push().getKey();
                mMetadataRef.child(key).setValue(metadata);
                mMessageTextRef.child(key).setValue(text);
*/
                Toast.makeText(getContext(), "Capsule sent", Toast.LENGTH_SHORT).show();

                Log.d(TAG, "Before intent delivery");
                Intent intent2 = new Intent(getContext(), DeliveryDateTimeActivity.class);
                Log.d(TAG, "After intent delivery");
                startActivity(intent2);
                break;

            case  R.id.attachPictureButton:
                //TODO
                //Load and Attach picture
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 100);
                break;

            case R.id.takePictureButton:
                //TODO
                //Take picture
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, 1);
                }
                break;

            case R.id.checkInButton:
                //TODO
                //Check in a place

                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            /*ImageView mImageView = (ImageView) getView().findViewById(R.id.imageView);
            mImageView.setImageBitmap(imageBitmap);*/

            mCallback.onPictureTaken(imageBitmap);
        }
    }

}

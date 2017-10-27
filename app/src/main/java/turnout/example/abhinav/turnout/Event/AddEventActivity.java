package turnout.example.abhinav.turnout.Event;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;
import turnout.example.abhinav.turnout.R;
import turnout.example.abhinav.turnout.Timeline.MainActivity;

public class AddEventActivity extends AppCompatActivity
{

    private static final int GALLERY_REQUEST = 1;
    private static final String TAG = "";
    private String postId="";int year_x,month_x,day_x;
    static final int DIALOG_ID = 0;
    private TextView eventDate;
    String sortDate="",sortmonth="",sortday="",sortyear="";
    private Button postTime;

    private ImageButton imageSelect;
    private EditText event;
    private EditText eventContact;
    private EditText post;
    private CircleImageView image;
    private Button submit;
    private Button location;
    private Long sequence;
    private boolean isUserClickedBackButton = false;
    private Uri imageUri = null;
    private StorageReference mStorage;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabaseUsers;

    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;

    int PLACE_PICKER_REQUEST = 2;
    private EditText locationName;
    private Double latitude;
    private Double longitude;

    private ProgressDialog progressDialog;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        final String clgID = getIntent().getExtras().getString("colgId");

        mAuth = FirebaseAuth.getInstance();

        mCurrentUser = mAuth.getCurrentUser();

        mStorage = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference().child(clgID).child("Event");
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users").child(mCurrentUser.getUid());

        imageSelect = (ImageButton) findViewById(R.id.imageSelect);
        event = (EditText) findViewById(R.id.singleHomeEvent);
        eventContact = (EditText) findViewById(R.id.singleHomeEventContact);
        post = (EditText) findViewById(R.id.postWrite);
        submit = (Button) findViewById(R.id.submitPost);
        image = (CircleImageView) findViewById(R.id.user_pic);
        location = (Button) findViewById(R.id.post_location);
        locationName = (EditText) findViewById(R.id.editTextLocation);

        eventDate = (TextView) findViewById(R.id.editTextDate);
        postTime = (Button) findViewById(R.id.post_time);


        final Calendar cal = Calendar.getInstance();
        year_x =  cal.get(Calendar.YEAR);
        month_x = cal.get(Calendar.MONTH);
        day_x = cal.get(Calendar.DAY_OF_MONTH);


        progressDialog = new ProgressDialog(this);

        imageSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_REQUEST);
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Posting");
                progressDialog.show();
                progressDialog.setCanceledOnTouchOutside(false);
                getPostId();

            }
        });

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

                Intent intent;
                try {
                    intent = builder.build(AddEventActivity.this);
                    startActivityForResult(intent , PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });

        postTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DIALOG_ID);
            }
        });

    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == DIALOG_ID)
            return new DatePickerDialog(this, dpickerListner , year_x , month_x , day_x);
        else
            return null;
    }

    private DatePickerDialog.OnDateSetListener dpickerListner = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            year_x = year;
            month_x = month + 1;
            day_x = dayOfMonth;
            eventDate.setText(day_x+"/"+month_x+"/"+year_x);
            if(month_x <10)
                sortmonth="0"+Integer.toString(month_x);
            else
                sortmonth=Integer.toString(month_x);
            if(day_x <10)
                sortday="0"+Integer.toString(day_x);
            else
                sortday=Integer.toString(day_x);
            sortyear=Integer.toString(year_x);
            sortDate=sortyear+sortmonth+sortday;
            sortDate= Integer.toString(0-Integer.parseInt(sortDate));

        }
    };


    private void startPosting()
    {

        final String title_post = post.getText().toString().trim();
        final String title_event = event.getText().toString().trim();
        final String title_contact = eventContact.getText().toString().trim();
        final String title_date = eventDate.getText().toString().trim();

        if (!TextUtils.isEmpty(title_post) ) {


            final DatabaseReference newpost = mDatabase.push();

            final   StorageReference filePath = mStorage.child("Posts/"+postId);
            mDatabaseUsers.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(final DataSnapshot dataSnapshot) {


                    newpost.child("eventDate").setValue(title_date);
                    newpost.child("eventName").setValue(title_event);
                    newpost.child("eventContact").setValue(title_contact);
                    FirebaseMessaging.getInstance().subscribeToTopic(newpost.getKey());
                    newpost.child("sortDate").setValue(sortDate);
                    newpost.child("eventID").setValue(newpost.getKey());
                    newpost.child("eventDes").setValue(title_post);
                    newpost.child("latitude").setValue(latitude);
                    newpost.child("longitude").setValue(longitude);
                    if(imageUri!=null){
                        filePath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                final Uri downloadUrl = taskSnapshot.getDownloadUrl();
                                newpost.child("image").setValue(downloadUrl.toString());
                                newpost.child("With_image").setValue(1);
                            }});
                    }
                    else{
                        newpost.child("With_image").setValue(0);}
                    newpost.child("userUid").setValue(mCurrentUser.getUid());
                    newpost.child("username").setValue(dataSnapshot.child("name").getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            final String clgID = getIntent().getExtras().getString("colgId");
                            if (task.isSuccessful()) {
                                progressDialog.dismiss();
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                intent.putExtra("colgId", clgID);
                                startActivity(intent);
                            }
                        }
                    });

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });




        }
        else{
            Toast.makeText(AddEventActivity.this,"Enter All of the above data to Submit", Toast.LENGTH_LONG).show();
        }

    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 1)
        {
            this.moveTaskToBack(true);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK) {
            imageUri=data.getData();
            imageSelect.setImageURI(imageUri);
        }
        if (requestCode == PLACE_PICKER_REQUEST && resultCode == RESULT_OK){
            Place place =  PlacePicker.getPlace(this , data);
            String address = String.format("Place :", place.getAddress());
             latitude = place.getLatLng().latitude;
             longitude = place.getLatLng().longitude;
        }

    }
    private void getPostId()
    {
        final DatabaseReference postNo=FirebaseDatabase.getInstance().getReference().child("post_count");
        postNo.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {

                sequence= (Long)mutableData.getValue();
                if(sequence==null)
                    return  Transaction.success(mutableData);
                else{
                    postId=Integer.toString(sequence.intValue());
                    sequence--;
                    mutableData.setValue(sequence);
                    return  Transaction.success(mutableData);
                }}
            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                startPosting();

            }
        });

    }

}







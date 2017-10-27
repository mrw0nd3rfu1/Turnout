package turnout.example.abhinav.turnout.Timeline;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


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

import de.hdodenhof.circleimageview.CircleImageView;
import turnout.example.abhinav.turnout.R;

public class PostActivity extends AppCompatActivity
{

    private static final int GALLERY_REQUEST = 1;
    private String postId="";
    private String Event_Id ="";
    private String Event_name="";
    private String Event_date ="";
    private ImageButton imageSelect;
    private TextView event;
    private TextView eventTime;
    private EditText post;
    private CircleImageView image;
    private Button submit;
    private Long sequence;
    private boolean isUserClickedBackButton = false;
    private Uri imageUri = null;
    private StorageReference mStorage;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabaseUsers;

    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;


    private ProgressDialog progressDialog;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        final String clgID = getIntent().getExtras().getString("colgId");

        mAuth = FirebaseAuth.getInstance();

        mCurrentUser = mAuth.getCurrentUser();

        Event_Id = getIntent().getExtras().getString("EventId");
        Event_name = getIntent().getExtras().getString("EventName");
        Event_date = getIntent().getExtras().getString("EventDate");

        mStorage = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference().child(clgID).child("Post");
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users").child(mCurrentUser.getUid());

        imageSelect = (ImageButton) findViewById(R.id.imageSelect);
        event = (TextView) findViewById(R.id.singleHomeEvent);
        event.setText(Event_name);
        eventTime = (TextView)findViewById(R.id.singleHomeEventDate);
        eventTime.setText(Event_date);
        post = (EditText) findViewById(R.id.postWrite);
        submit = (Button) findViewById(R.id.submitPost);
        image = (CircleImageView) findViewById(R.id.user_pic);

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
    }


    private void startPosting()
    {

        final String title_post = post.getText().toString().trim();

        if (!TextUtils.isEmpty(title_post) ) {


            final DatabaseReference newpost = mDatabase.push();

         final   StorageReference filePath = mStorage.child("Posts/"+postId);
                    mDatabaseUsers.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(final DataSnapshot dataSnapshot) {


                            newpost.child("event").setValue(Event_name);
                            newpost.child("eventId").setValue(Event_Id);
                            FirebaseMessaging.getInstance().subscribeToTopic(Event_Id);
                            newpost.child("post").setValue(title_post);
                            newpost.child("post_key").setValue(newpost.getKey());
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
                            newpost.child("uid").setValue(mCurrentUser.getUid());
                            newpost.child("post_id").setValue(postId);
                            newpost.child("profile_pic").setValue(dataSnapshot.child("profile_pic").getValue());
                            newpost.child("thumb_profile_pic").setValue(dataSnapshot.child("thumb_profile_pic").getValue());
                            newpost.child("post_time").setValue(Event_date);
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
            Toast.makeText(PostActivity.this,"Enter Post to Submit", Toast.LENGTH_LONG).show();
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







package turnout.example.abhinav.turnout.Comment;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

import turnout.example.abhinav.turnout.R;

public class CommentListActivity extends AppCompatActivity {

    //view objects
    EditText editTextComment;
    Button buttonAddComment;
    ListView listViewComment;
    Toolbar mToolbar;


    //a list to store all the artist from firebase database
    List<CommentPosts> cName;

    DatabaseReference databaseComment;
    DatabaseReference databaseUser;
    DatabaseReference database;
    FirebaseAuth mAuth;
    private String mPost_key = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        final String clgID = getIntent().getExtras().getString("colgId");
        mPost_key = getIntent().getExtras().getString("home_id");

        database = FirebaseDatabase.getInstance().getReference().child(clgID).child("Post");
        databaseComment = FirebaseDatabase.getInstance().getReference().child(clgID).child("Post").child(mPost_key).child("Comments");
        databaseUser =FirebaseDatabase.getInstance().getReference().child("User");
        mAuth = FirebaseAuth.getInstance();

        //getting views

        editTextComment = (EditText) findViewById(R.id.comment);
        listViewComment = (ListView) findViewById(R.id.list_comment);
        buttonAddComment = (Button) findViewById(R.id.commentButton);

        mToolbar = (Toolbar)findViewById(R.id.toolbar);
        mToolbar.setTitle("Comment");

        //list to store artists
        cName = new ArrayList<>();


        buttonAddComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //calling the method addArtist()
                //the method is defined below
                //this method is actually performing the write operation
                addComment();
            }
        });

        listViewComment.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                    CommentPosts comment = cName.get(i);
                    if (mAuth.getCurrentUser().getUid().equals(comment.getComment())) {
                        showUpdateDeleteDialog(comment.getCommentID(), comment.getComment());
                    }
                        return true;

                }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        databaseComment.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                 cName.clear();

                //iterating through all the nodes
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    CommentPosts artist = postSnapshot.getValue(CommentPosts.class);
                    cName.add(artist);
                }

                //creating adapter
                CommentActivity collegeAdapter = new CommentActivity(CommentListActivity.this, cName);
                //attaching adapter to the listview
                listViewComment.setAdapter(collegeAdapter);
                listViewComment.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void addComment() {
        String name = editTextComment.getText().toString().trim();
        FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();
        String comment = mAuth.getCurrentUser().getUid();
        if (!TextUtils.isEmpty(name)) {

            //getting a unique id using push().getKey() method
            //it will create a unique id and we will use it as the Primary Key for our Artist
            String id = databaseComment.push().getKey();

            CommentPosts comment_name = new CommentPosts(id ,name,comment );

            databaseComment.child(id).setValue(comment_name);

            //setting edit text to blank again
            editTextComment.setText("");

            //displaying a success toast
            Toast.makeText(this, "Comment added", Toast.LENGTH_LONG).show();
        } else {
            //if the value is not given displaying a toast
            Toast.makeText(this, "Please enter a comment", Toast.LENGTH_LONG).show();
        }
    }

    private boolean updateCollege(String id, String name, String comment) {
        //getting the specified artist reference
        final String clgID = getIntent().getExtras().getString("colgId");
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference().child(clgID).child("Post").child(mPost_key).child("Comments").child(id);

        //updating artist
        CommentPosts clg_name = new CommentPosts(id, name, comment);
        dR.setValue(clg_name);
        Toast.makeText(getApplicationContext(), "Comment Updated", Toast.LENGTH_LONG).show();
        return true;
    }

    private void showUpdateDeleteDialog(final String commentId, final String commentName) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.comment_update, null);
        dialogBuilder.setView(dialogView);

        final EditText editTextComment = (EditText) dialogView.findViewById(R.id.editTextName);
        final Button buttonUpdate = (Button) dialogView.findViewById(R.id.buttonUpdateArtist);
        final Button buttonDelete = (Button) dialogView.findViewById(R.id.buttonDeleteArtist);

        final AlertDialog b = dialogBuilder.create();
        b.show();


        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editTextComment.getText().toString().trim();
               if (!TextUtils.isEmpty(name)) {
                    updateCollege(commentId, name, commentName);
                    b.dismiss();
                }
            }
        });




        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                deleteComment(commentId);
                b.dismiss();
            }
        });

    }

    private boolean deleteComment(String id) {
        //getting the specified artist reference
        final String clgID = getIntent().getExtras().getString("colgId");
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference().child(clgID).child("Post").child(mPost_key).child("Comments").child(id);

        //removing artist
        dR.removeValue();


         Toast.makeText(getApplicationContext(), "Comment Deleted", Toast.LENGTH_LONG).show();

        return true;
    }
}
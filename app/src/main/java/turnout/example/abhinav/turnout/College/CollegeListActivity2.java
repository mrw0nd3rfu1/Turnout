package turnout.example.abhinav.turnout.College;

import android.content.Intent;
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


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

import turnout.example.abhinav.turnout.R;

public class CollegeListActivity2 extends AppCompatActivity {

    //view objects
    EditText editTextName;
    Button buttonAddCollege;
    ListView listViewCollege;
    Toolbar mToolbar;

    //a list to store all the artist from firebase database
    List<CollegeName> cName;

    DatabaseReference databaseCollege;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_college2);

        databaseCollege = FirebaseDatabase.getInstance().getReference().child("College");

        //getting views
        editTextName = (EditText) findViewById(R.id.editTextName);
        listViewCollege = (ListView) findViewById(R.id.listViewArtists);
        buttonAddCollege = (Button) findViewById(R.id.buttonAddCollege);

        mToolbar = (Toolbar)findViewById(R.id.toolbar);
        mToolbar.setTitle("College Names");

        //list to store artists
        cName = new ArrayList<>();


        buttonAddCollege.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //calling the method addArtist()
                //the method is defined below
                //this method is actually performing the write operation
                addCollege();
            }
        });

        listViewCollege.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //getting the selected artist
                CollegeName artist = cName.get(i);
                   Intent setup=new Intent();
                   setup.putExtra("CollegeName",artist.getCollegeName());
                   setup.putExtra("CollegeId",artist.getCollegeID());

                setResult(2,setup);
                   finish();
            }
        });

        listViewCollege.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                CollegeName clg_name = cName.get(i);
                showUpdateDeleteDialog(clg_name.getCollegeID(), clg_name.getCollegeName());
                return true;
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        databaseCollege.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                 cName.clear();

                //iterating through all the nodes
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    CollegeName artist = postSnapshot.getValue(CollegeName.class);
                    cName.add(artist);
                }

                //creating adapter
                CollegeActivity collegeAdapter = new CollegeActivity(CollegeListActivity2.this, cName);
                //attaching adapter to the listview
                listViewCollege.setAdapter(collegeAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void addCollege() {
        String name = editTextName.getText().toString().trim();

        if (!TextUtils.isEmpty(name)) {

            //getting a unique id using push().getKey() method
            //it will create a unique id and we will use it as the Primary Key for our Artist
            String id = databaseCollege.push().getKey();

            CollegeName clg_name = new CollegeName(id ,name,"","");

            databaseCollege.child(id).setValue(clg_name);

            //setting edit text to blank again
            editTextName.setText("");

            //displaying a success toast
            Toast.makeText(this, "College added", Toast.LENGTH_LONG).show();
        } else {
            //if the value is not given displaying a toast
            Toast.makeText(this, "Please enter a name", Toast.LENGTH_LONG).show();
        }
    }

    private boolean updateCollege(String id, String name) {
        //getting the specified artist reference
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("College").child(id);

        //updating artist
        CollegeName clg_name = new CollegeName(id, name,"","");
        dR.setValue(clg_name);
        Toast.makeText(getApplicationContext(), "College Updated", Toast.LENGTH_LONG).show();
        return true;
    }

    private void showUpdateDeleteDialog(final String collegeId, String collegeName) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.name_update, null);
        dialogBuilder.setView(dialogView);

        final EditText editTextName = (EditText) dialogView.findViewById(R.id.editTextName);
        final Button buttonUpdate = (Button) dialogView.findViewById(R.id.buttonUpdateArtist);

        dialogBuilder.setTitle(collegeName);
        final AlertDialog b = dialogBuilder.create();
        b.show();


        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editTextName.getText().toString().trim();
                if (!TextUtils.isEmpty(name)) {
                    updateCollege(collegeId, name);
                    b.dismiss();
                }
            }
        });

    }

}
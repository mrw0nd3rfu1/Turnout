package turnout.example.abhinav.turnout.Fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;
import turnout.example.abhinav.turnout.Event.EventName;
import turnout.example.abhinav.turnout.Event.EventTimeline;
import turnout.example.abhinav.turnout.R;
import turnout.example.abhinav.turnout.Timeline.MainActivity;
import turnout.example.abhinav.turnout.Event.EventName;
import turnout.example.abhinav.turnout.Event.EventTimeline;
import turnout.example.abhinav.turnout.Timeline.MainActivity;


public class HotEventsFragment extends Fragment {

    private RecyclerView mEventList;

    private FirebaseAuth mAuth;
    FirebaseRecyclerAdapter<EventName,HotEventViewHolder> friendsRecyclerViewAdapter;
    private String mCurrent_user_id;

    public View mMainView;

    public HotEventsFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mMainView = inflater.inflate(R.layout.fragment_hoteventsfragment , container , false);
        mEventList = (RecyclerView) mMainView.findViewById(R.id.event_list);
        mAuth=FirebaseAuth.getInstance();
        mCurrent_user_id = mAuth.getCurrentUser().getUid();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("Event");
        Query query= mDatabase.orderByChild("likesCount");
        query.keepSynced(true);

        friendsRecyclerViewAdapter = new FirebaseRecyclerAdapter<EventName, HotEventViewHolder>(
                EventName.class,
                R.layout.event_list,
                HotEventViewHolder.class,
                query
        ) {
            @Override
            protected void populateViewHolder(HotEventViewHolder viewHolder,final EventName model, int position) {

                viewHolder.setName(model.getEventName());
                viewHolder.setDate(model.getEventDate());
                viewHolder.setContact(model.getEventContact());
                viewHolder.setDes(model.getEventDes());
                viewHolder.setImage(getContext() , model.getImage());

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent setup=new Intent(getContext(),EventTimeline.class);
                        setup.putExtra("EventName",model.getEventName());
                        setup.putExtra("EventId",model.getEventID());
                        setup.putExtra("EventDate", model.getEventDate());
                        setup.putExtra("colgId", MainActivity.clgID);
                        setup.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(setup);
                    }
                });

            }
        };
        mDatabase.keepSynced(true);
        mEventList.setLayoutManager(new LinearLayoutManager(getContext()));

        mEventList.setAdapter(friendsRecyclerViewAdapter);
        return mMainView;


    }



    @Override
    public void onStart() {
        super.onStart();
    }

    public static class HotEventViewHolder extends RecyclerView.ViewHolder{

        View mView;

        public HotEventViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
        }

        public void setName(String event){
            TextView eventView = (TextView) mView.findViewById(R.id.textViewName);
            eventView.setText(event);
        }

        public void setDate(String eventdate){
            TextView eventView = (TextView) mView.findViewById(R.id.eventDate);
            eventView.setText(eventdate);
        }

        public void setContact(String eventContact){
            TextView eventView = (TextView) mView.findViewById(R.id.eventContact);
            eventView.setText(eventContact);
        }
        public void setDes(String eventDes){
            TextView eventView = (TextView) mView.findViewById(R.id.eventDes);
            eventView.setText(eventDes);
        }
        public void setImage(Context ctx , String image){
            ImageView eventView = (ImageView) mView.findViewById(R.id.eventImage);
            Picasso.with(ctx).load(image).into(eventView);
        }

    }

}

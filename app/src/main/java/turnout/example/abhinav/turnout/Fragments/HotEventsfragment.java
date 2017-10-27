package turnout.example.abhinav.turnout.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import turnout.example.abhinav.turnout.R;


public class HotEventsfragment extends Fragment {



    public HotEventsfragment() {
        // Required empty public constructor
    }


    public static HotEventsfragment newInstance() {

        return new HotEventsfragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_hoteventsfragment, container, false);
    }

}

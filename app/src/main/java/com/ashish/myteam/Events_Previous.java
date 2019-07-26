package com.ashish.myteam;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import static com.ashish.myteam.SplashScreen.cardList;
import static com.ashish.myteam.SplashScreen.previousCardList;
import static com.ashish.myteam.SplashScreen.profileUser;
import static com.ashish.myteam.SplashScreen.upcomingCardList;


/**
 * A simple {@link Fragment} subclass.
 */
public class Events_Previous extends Fragment {

    ListView previouseventlist;

    public Events_Previous() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_previous_event, container, false);
        previouseventlist = (ListView) v.findViewById(R.id.previousEventCardList);
        if(cardList.size() == 0){
            Toast.makeText(getActivity(), "No Event",Toast.LENGTH_LONG).show();
        }else {
            if (previousCardList.size() == 0) {
                previouseventlist.setAdapter(new Adapter_Event_Card(getActivity(), R.layout.list_event_card, previousCardList, profileUser.team));
                Toast.makeText(getActivity(), "No Previous Event", Toast.LENGTH_LONG).show();
            } else {
                previouseventlist.setAdapter(new Adapter_Event_Card(getActivity(), R.layout.list_event_card, previousCardList, profileUser.team));
            }
        }



        return v;
    }

}

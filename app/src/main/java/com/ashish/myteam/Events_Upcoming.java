package com.ashish.myteam;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import static com.ashish.myteam.SplashScreen.cardList;
import static com.ashish.myteam.SplashScreen.profileUser;
import static com.ashish.myteam.SplashScreen.upcomingCardList;


/**
 * A simple {@link Fragment} subclass.
 */
public class Events_Upcoming extends Fragment {


    ListView eventlist;
    public Events_Upcoming() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_upcoming__event, container, false);
        eventlist = (ListView) v.findViewById(R.id.eventCardList);
        if(cardList.size() ==0){
            Toast.makeText(getActivity(), "No Event",Toast.LENGTH_LONG).show();
        }else {
            if (upcomingCardList.size() == 0) {
                eventlist.setAdapter(new Adapter_Event_Card(getActivity(), R.layout.list_event_card, upcomingCardList, profileUser.team));
                Toast.makeText(getActivity(), "No Upcoming Event", Toast.LENGTH_LONG).show();
            } else {
                eventlist.setAdapter(new Adapter_Event_Card(getActivity(), R.layout.list_event_card, upcomingCardList, profileUser.team));
            }
        }
        return v;
    }

}

package com.ashish.myteam;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import static com.ashish.myteam.SplashScreen.EventMap;
import static com.ashish.myteam.SplashScreen.UserMap;

public class Event_Members extends AppCompatActivity {

    int eId;
    ListView memList;
    ArrayList<Class_Contri_Status> Status;
    Class_Event Event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_event_members);

        eId = getIntent().getIntExtra("members", 0);
        memList = (ListView) findViewById(R.id.memberListOfEvent);
        Status = new ArrayList<>();
        Event = EventMap.get(eId);
        String[] memberIds = Event.eMembers.split(",", 0);
        for (int i = 0; i < memberIds.length; i++) {
            if (!memberIds[i].equals("")) {
                User member = UserMap.get(Integer.parseInt(memberIds[i]));
                Status.add(new Class_Contri_Status(member.id, member.name, false));
            }
        }
        if(Event.eCdtMembs.length() > 0) {
            String[] crdtMemberIds = Event.eCdtMembs.split(",", 0);
            for (int i = 0; i < crdtMemberIds.length; i++) {
                if (!crdtMemberIds[i].equals("")) {
                    for(int j =0;j<Status.size();j++){
                        if(Status.get(j).ID == Integer.parseInt(crdtMemberIds[i])){
                            Status.get(j).setStatus(true);
                        }
                    }
                }
            }
            memList.setAdapter(new Adapter_Contri_Status(Event_Members.this, R.layout.list_contri_status, Status));

        }else {
            memList.setAdapter(new Adapter_Contri_Status(Event_Members.this, R.layout.list_contri_status, Status));
        }

    }
}

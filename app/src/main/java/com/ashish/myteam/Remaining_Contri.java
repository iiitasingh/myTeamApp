package com.ashish.myteam;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import static com.ashish.myteam.SplashScreen.profileUser;
import static com.ashish.myteam.Wallet.ContriEvents;

public class Remaining_Contri extends AppCompatActivity {

    ListView contriEvents;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_remaining_contri);

        contriEvents = (ListView)findViewById(R.id.contriEvents);
        if(ContriEvents.size() == 0) {
            contriEvents.setAdapter(new Adapter_Event_Card(Remaining_Contri.this, R.layout.list_event_card, ContriEvents,profileUser.team));
            Toast.makeText(Remaining_Contri.this, "There is no event!",Toast.LENGTH_LONG).show();
        }
        else {
            contriEvents.setAdapter(new Adapter_Event_Card(Remaining_Contri.this, R.layout.list_event_card, ContriEvents,profileUser.team));
        }
    }
}

package com.ashish.myteam;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import static com.ashish.myteam.SplashScreen.UserData;
import static com.ashish.myteam.SplashScreen.getUser;
import static com.ashish.myteam.SplashScreen.getprofileUserdata;
import static com.ashish.myteam.SplashScreen.profileUser;

public class TeamMembers extends AppCompatActivity {

    ListView your_teams;
    TextView tmname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_team_members);

        your_teams = (ListView) findViewById(R.id.yourteamlist);
        tmname = (TextView) findViewById(R.id.team_name);
        tmname.setText("Team "+profileUser.team);

        if (UserData.size() == 0) {
            your_teams.setAdapter(new Adapter_Team_List(getApplicationContext(), R.layout.list_your_team, UserData));
            Toast.makeText(TeamMembers.this, "No Member in this Team!", Toast.LENGTH_LONG).show();
        } else {
            your_teams.setAdapter(new Adapter_Team_List(getApplicationContext(), R.layout.list_your_team, UserData));
        }

        your_teams.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent memberIntent = new Intent(TeamMembers.this, Resume.class);
                memberIntent.putExtra("position",position);
                startActivity(memberIntent);
            }
        });
    }
}

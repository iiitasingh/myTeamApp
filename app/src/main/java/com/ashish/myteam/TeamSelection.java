package com.ashish.myteam;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import com.google.gson.annotations.SerializedName;
import com.kosalgeek.android.json.JsonConverter;
import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.PostResponseAsyncTask;

import java.io.Serializable;
import java.util.ArrayList;

public class TeamSelection extends AppCompatActivity implements AsyncResponse {

    ImageView add_team;
    ListView teamslist;
    String[] teamstest;
    ArrayList<teamlist> teams;
    String clickedValue = "Select Team First";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_team_selection);

        add_team = (ImageView) findViewById(R.id.addTeamfab);
        teamslist = (ListView) findViewById(R.id.teamlist);

        add_team.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerTeam = new Intent(TeamSelection.this, RegisterTeam.class);
                startActivity(registerTeam);
            }
        });


        teams = new ArrayList<>();
        PostResponseAsyncTask taskRead = new PostResponseAsyncTask(TeamSelection.this, this);
        taskRead.execute("https://voteforashish.000webhostapp.com/myTeam/teamsList.php");

        teamslist.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub

                clickedValue = teams.get(position).name.toString();

                Intent intent = new Intent(TeamSelection.this, SignUp.class);
                intent.putExtra("ListViewClickedValue", clickedValue);
                startActivity(intent);

            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent setIntent = new Intent(TeamSelection.this, SignUp.class);
        startActivity(setIntent);
        finish();
    }


    @Override
    public void processFinish(String s) {
        teams = new JsonConverter<teamlist>().toArrayList(s,teamlist.class);
        if(teams.size() == 0) {
            teamslist.setAdapter(new Adapter_team_selection_list(this, teams));
            Toast.makeText(TeamSelection.this, "Register your team first",Toast.LENGTH_LONG).show();
        }
        else {
            teamslist.setAdapter(new Adapter_team_selection_list(this, teams));
        }
    }

    public class teamlist implements Serializable {

        @SerializedName("team_name")
        public String name;
    }
}
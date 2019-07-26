package com.ashish.myteam;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.ExceptionHandler;
import com.kosalgeek.genasync12.PostResponseAsyncTask;

import java.util.HashMap;

public class RegisterTeam extends AppCompatActivity {
    EditText teamN, email, pin;
    Button registerbutton;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_register_team);

        teamN = (EditText) findViewById(R.id.registerTeamName);
        email = (EditText) findViewById(R.id.registerTeamEmail);
        pin = (EditText) findViewById(R.id.registerTeamPin);
        registerbutton = (Button) findViewById(R.id.button_register);
        dialog = new Dialog(this);
    }

    public void registerTeam(View view)
    {
        if(SplashScreen.checkNetworkConnectionStatus(RegisterTeam.this)) {
            String teamname = teamN.getText().toString().trim();
            String mail = email.getText().toString().trim();
            String pn = pin.getText().toString().trim();
            String type = "register_team";

            if (teamname.length() >= 2 && mail.length() >= 5 && pn.length() >= 4) {


                HashMap<String, String> loginData = new HashMap<>();
                loginData.put("teamname", teamname.toUpperCase());
                loginData.put("email", mail);
                loginData.put("pin", pn);

                PostResponseAsyncTask loginTask = new PostResponseAsyncTask(this,
                        loginData, new AsyncResponse() {
                    @Override
                    public void processFinish(String s) {
                        if (s.contains("TeamPresent")) {
                            Toast.makeText(RegisterTeam.this, "Team already exist", Toast.LENGTH_SHORT).show();
                        } else if (s.contains("mailPresent")) {
                            Toast.makeText(RegisterTeam.this, "You already registered a Team", Toast.LENGTH_SHORT).show();
                        } else if (s.contains("ErrorInsert")) {
                            Toast.makeText(RegisterTeam.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(RegisterTeam.this, "Registration successful", Toast.LENGTH_SHORT).show();
                            Intent moveselectTeam = new Intent(RegisterTeam.this, TeamSelection.class);
                            startActivity(moveselectTeam);
                        }
                    }
                });
                loginTask.setExceptionHandler(new ExceptionHandler() {
                    @Override
                    public void handleException(Exception e) {
                        if (e != null && e.getMessage() != null) {
                            Toast.makeText(getApplicationContext(),
                                    "" + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
                loginTask.execute("https://voteforashish.000webhostapp.com/myTeam/registerTeam.php");
            } else {
                if (teamname.length() < 2) {
                    Toast.makeText(RegisterTeam.this, "Team name is too short", Toast.LENGTH_SHORT).show();
                } else if (mail.length() < 5) {
                    Toast.makeText(RegisterTeam.this, "Mail is too short", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RegisterTeam.this, "Team Pin should have at least 4 Characters", Toast.LENGTH_SHORT).show();
                }
            }
        }
        else {
            noInternet();
        }
    }

    public void noInternet(){
        dialog.setContentView(R.layout.popup_no_internet);
        int width1 = (int) (this.getResources().getDisplayMetrics().widthPixels * .4);
        int height1 = (int) (this.getResources().getDisplayMetrics().heightPixels * 0.3);
        dialog.getWindow().setLayout(width1, height1);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

}

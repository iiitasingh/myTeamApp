package com.ashish.myteam;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kosalgeek.android.json.JsonConverter;
import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.ExceptionHandler;
import com.kosalgeek.genasync12.PostResponseAsyncTask;

import java.util.Base64;
//import android.util.Base64;
import java.util.HashMap;

import static com.ashish.myteam.SplashScreen.MakePlaceholders;
import static com.ashish.myteam.SplashScreen.TransList;
import static com.ashish.myteam.SplashScreen.UserData;
import static com.ashish.myteam.SplashScreen.cardList;
import static com.ashish.myteam.SplashScreen.getUser;
import static com.ashish.myteam.SplashScreen.getprofileUserdata;
import static com.ashish.myteam.SplashScreen.parseEvents;
import static com.ashish.myteam.SplashScreen.parseTrans;
import static com.ashish.myteam.SplashScreen.pos;
import static com.ashish.myteam.SplashScreen.profileUser;

public class HomePage extends AppCompatActivity {

    ImageView yourImage, teamIcon, addEvent, birthdayImg, EventsImg, transactionImg, addTransacImg, ContriWalletImg, addEventImg2;
    TextView yourName, teamName, teamName1, birthdayTxt, addEventTxt, EventsTxt, transactionTxt, addTransacTxt, ContriWalletTxt;
    SwipeRefreshLayout swipeRefresh;
    Dialog logoutProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_home);

        logoutProfile = new Dialog(this);
        addEvent = (ImageView) findViewById(R.id.addEvent);
        yourImage = (ImageView) findViewById(R.id.userImg);
        teamIcon = (ImageView) findViewById(R.id.teamIcon);
        yourName = (TextView) findViewById(R.id.nickName);
        teamName = (TextView) findViewById(R.id.teamNm);
        teamName1 = (TextView) findViewById(R.id.teamNm1);
        birthdayImg = (ImageView) findViewById(R.id.birthdayImg);
        birthdayTxt = (TextView) findViewById(R.id.birthdayTxt);
        EventsImg = (ImageView) findViewById(R.id.EventsImg);
        EventsTxt = (TextView) findViewById(R.id.EventsTxt);
        addEventTxt = (TextView) findViewById(R.id.addEventTxt);
        transactionImg = (ImageView) findViewById(R.id.transactionImg);
        transactionTxt = (TextView) findViewById(R.id.transactionTxt);
        addTransacImg = (ImageView) findViewById(R.id.addTransacImg);
        addTransacTxt = (TextView) findViewById(R.id.addTransacTxt);
        ContriWalletImg = (ImageView) findViewById(R.id.ContriWalletImg);
        ContriWalletTxt = (TextView) findViewById(R.id.ContriWalletTxt);
        addEventImg2 = (ImageView) findViewById(R.id.addEventImg2);

        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        setdata();
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateUserData();
            }
        });

        yourImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutPopup(HomePage.this, profileUser);
            }
        });

        teamName1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent home = new Intent(HomePage.this, TeamMembers.class);
                startActivity(home);
            }
        });

        teamIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent home = new Intent(HomePage.this, TeamMembers.class);
                startActivity(home);
            }
        });

        EventsImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent MemIntent = new Intent(HomePage.this, Events.class);
                startActivity(MemIntent);
            }
        });
        EventsTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent MemIntent = new Intent(HomePage.this, Events.class);
                startActivity(MemIntent);
            }
        });
        birthdayTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent bdcalender = new Intent(HomePage.this, BirthdayCalender.class);
                startActivity(bdcalender);
            }
        });
        birthdayImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent bdcalender = new Intent(HomePage.this, BirthdayCalender.class);
                startActivity(bdcalender);
            }
        });
        addEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent MemIntent = new Intent(HomePage.this, Event_Add.class);
                startActivity(MemIntent);
            }
        });
        addEventTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent MemIntent = new Intent(HomePage.this, Event_Add.class);
                startActivity(MemIntent);
            }
        });
        transactionImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent MemIntent = new Intent(HomePage.this, Transaction.class);
                startActivity(MemIntent);
            }
        });
        transactionTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent MemIntent = new Intent(HomePage.this, Transaction.class);
                startActivity(MemIntent);
            }
        });
        addTransacImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent home = new Intent(HomePage.this, Transaction_Add.class);
                startActivity(home);
            }
        });

        addTransacTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent home = new Intent(HomePage.this, Transaction_Add.class);
                startActivity(home);
            }
        });
        ContriWalletImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent home = new Intent(HomePage.this, Wallet.class);
                startActivity(home);
            }
        });

        ContriWalletTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent home = new Intent(HomePage.this, Wallet.class);
                startActivity(home);
            }
        });

    }

    public void setdata() {
        yourName.setText("Welcome " + profileUser.nickname);
        teamName.setText(profileUser.team);
        teamName1.setText(profileUser.team);
        byte[] usrImg = Base64.getMimeDecoder().decode(profileUser.image);
        Bitmap bitmap = BitmapFactory.decodeByteArray(usrImg, 0, usrImg.length);
        yourImage.setImageBitmap(bitmap);
    }

    private void updateUserData() {
        PostResponseAsyncTask taskRead = new PostResponseAsyncTask(HomePage.this, getUser, false, new AsyncResponse() {
            @Override
            public void processFinish(String s) {

                    UserData.clear();
                    UserData = new JsonConverter<User>().toArrayList(s, User.class);
                if (UserData != null) {
                    if (UserData.size() > 0) {
                        getprofileUserdata(UserData);
                        if(profileUser.events.length() > 0) {
                            getEvents(profileUser.events);
                        }
                    } else {
                        Toast.makeText(getApplicationContext(),
                                "Check Your Internet and swipe down to refresh", Toast.LENGTH_LONG).show();
                        swipeRefresh.setRefreshing(false);
                    }

                }else {
                    Toast.makeText(getApplicationContext(),
                            "Something went wrong, Please refresh again", Toast.LENGTH_LONG).show();
                    swipeRefresh.setRefreshing(false);
                }
            }

        });
        taskRead.execute("https://voteforashish.000webhostapp.com/myTeam/getUserData.php");
    }

    public void getEvents(String events) {
        String[] names = events.split(",", 0);
        String sql = "SELECT * FROM table_event WHERE event_ID IN (" + MakePlaceholders(names.length -1) + ")  ORDER BY event_date";
        HashMap<String, String> getEvents = new HashMap<>();
        getEvents.put("events", events);
        getEvents.put("sql", sql);
        PostResponseAsyncTask taskRead1 = new PostResponseAsyncTask(HomePage.this, getEvents, false, new AsyncResponse() {
            @Override
            public void processFinish(String s1) {
                    cardList.clear();
                    cardList = new JsonConverter<Class_Event>().toArrayList(s1, Class_Event.class);
                if (cardList != null) {
                    if (cardList.size() > 0) {
                        parseEvents(cardList);
                        if(profileUser.transactions.length() > 0){
                            getTransactions(profileUser.transactions);
                        }
                        else {
                            setdata();
                            swipeRefresh.setRefreshing(false);
                        }
                    } else {
                        Toast.makeText(getApplicationContext(),
                                "Error while fetching the data! Swipe down to refresh", Toast.LENGTH_LONG).show();
                        swipeRefresh.setRefreshing(false);
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(),
                            "Something went wrong! Please swipe down to refresh again", Toast.LENGTH_LONG).show();
                    swipeRefresh.setRefreshing(false);
                }

            }

        });
        taskRead1.setExceptionHandler(new ExceptionHandler() {
            @Override
            public void handleException(Exception e) {
                if (e != null && e.getMessage() != null) {
                    Toast.makeText(getApplicationContext(),
                            "" + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
        taskRead1.execute("https://voteforashish.000webhostapp.com/myTeam/getEvents.php");
    }

    public void getTransactions(String transactions) {
        String[] names = transactions.split(",", 0);
        String sql = "SELECT * FROM table_transaction WHERE transaction_id IN (" + MakePlaceholders((names.length - 1)) + ")  ORDER BY transaction_date DESC";
        HashMap<String, String> getEvents = new HashMap<>();
        getEvents.put("events", transactions);
        getEvents.put("sql", sql);
        PostResponseAsyncTask taskRead3 = new PostResponseAsyncTask(HomePage.this, getEvents, false, new AsyncResponse() {
            @Override
            public void processFinish(String s) {
                    TransList.clear();
                    TransList = new JsonConverter<Class_Transaction>().toArrayList(s, Class_Transaction.class);
                if (TransList != null) {
                    if (TransList.size() > 0) {
                        parseTrans(TransList);
                        setdata();
                        swipeRefresh.setRefreshing(false);
                    } else {
                        Toast.makeText(getApplicationContext(),
                                "Error while fetching the data! Swipe down to refresh", Toast.LENGTH_LONG).show();
                        swipeRefresh.setRefreshing(false);
                    }
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Something went wrong! Please swipe down to refresh again", Toast.LENGTH_LONG).show();
                    swipeRefresh.setRefreshing(false);
                }
            }

        });
        taskRead3.setExceptionHandler(new ExceptionHandler() {
            @Override
            public void handleException(Exception e) {
                if (e != null && e.getMessage() != null) {
                    Toast.makeText(getApplicationContext(),
                            "" + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
        taskRead3.execute("https://voteforashish.000webhostapp.com/myTeam/getEvents.php");
    }

    Button logout, profile;
    ImageView logoutUserImg;

    private void logoutPopup(Activity popActivity, final User membMail) {

        logoutProfile.setContentView(R.layout.popup_logout);

        int width1 = (int) (popActivity.getResources().getDisplayMetrics().widthPixels * .8);
        int height1 = (int) (popActivity.getResources().getDisplayMetrics().heightPixels * .6);
        logoutProfile.getWindow().setLayout(width1, height1);
        logoutProfile.getWindow().setGravity(Gravity.CENTER);
        logoutProfile.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        logoutProfile.show();

        logout = (Button) logoutProfile.findViewById(R.id.btnLogout);
        profile = (Button) logoutProfile.findViewById(R.id.btnProfile);
        logoutUserImg = (ImageView) logoutProfile.findViewById(R.id.logoutUserImg);


        byte[] usrerImg = java.util.Base64.getMimeDecoder().decode(membMail.image);
        Bitmap bitmap = BitmapFactory.decodeByteArray(usrerImg, 0, usrerImg.length);
        logoutUserImg.setImageBitmap(bitmap);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logout();
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newIntent = new Intent(HomePage.this, Resume.class);
                newIntent.putExtra("position",pos);
                startActivity(newIntent);
                logoutProfile.dismiss();
            }
        });
    }

    private void Logout() {
        SplashScreen.session.setLoggedin(false);
        finish();
        startActivity(new Intent(HomePage.this, MainActivity.class));
    }

}

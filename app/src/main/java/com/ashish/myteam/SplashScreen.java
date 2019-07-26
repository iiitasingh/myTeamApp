package com.ashish.myteam;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kosalgeek.android.json.JsonConverter;
import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.ExceptionHandler;
import com.kosalgeek.genasync12.PostResponseAsyncTask;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class SplashScreen extends AppCompatActivity {

    public static Session session;
    static String TempHolder;
    public static int pos;
    public static User profileUser;
    public static ArrayList<User> UserData;
    public static ArrayList<Class_Event> cardList;
    public static ArrayList<Class_Event> upcomingCardList;
    public static ArrayList<Class_Event> previousCardList;
    public static ArrayList<Class_Transaction> TransList;
    public static ArrayList<Class_Transaction> DbtTransList;
    public static ArrayList<Class_Transaction> CrtTransList;
    public static HashMap<String, String> getUser = new HashMap<>();
    public static HashMap<Integer, User> UserMap = new HashMap<>();
    public static HashMap<Integer, Class_Event> EventMap = new HashMap<>();


    ImageView mConStatusIv;
    TextView mConStatusTv;
    Button mConStatusBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        mConStatusIv = findViewById(R.id.connection_img);
        mConStatusTv = findViewById(R.id.connection_text);
        mConStatusBtn = findViewById(R.id.connectionRefresh);
        upcomingCardList = new ArrayList<>();
        previousCardList = new ArrayList<>();
        cardList = new ArrayList<>();
        CrtTransList = new ArrayList<>();
        DbtTransList = new ArrayList<>();
        TransList = new ArrayList<>();

        session = new Session(this);
        if (!session.loggedin()) {
            Logout();
        } else {
            if (checkNetworkConnectionStatus(SplashScreen.this)) {
                TempHolder = getIntent().getStringExtra("UserEmail");
                UserData = new ArrayList<>();
                String sql = "select * from table_user where team = (Select team from table_user where email = '" + TempHolder + "')";
                getUser.put("sql", sql);
                getData(getUser);
            } else {
                mConStatusIv.setVisibility(View.VISIBLE);
                mConStatusTv.setVisibility(View.VISIBLE);
                mConStatusBtn.setVisibility(View.VISIBLE);
            }

        }

        mConStatusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                overridePendingTransition(0, 0);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                finish();
                overridePendingTransition(0, 0);
                startActivity(intent);

            }
        });
    }

    private void Logout() {
        session.setLoggedin(false);
        finish();
        startActivity(new Intent(SplashScreen.this, MainActivity.class));
    }

    public void getData(HashMap<String, String> getUser) {
        PostResponseAsyncTask taskRead = new PostResponseAsyncTask(SplashScreen.this, getUser, false, new AsyncResponse() {
            @Override
            public void processFinish(String s) {
                UserData.clear();
                UserData = new JsonConverter<User>().toArrayList(s, User.class);
                if (UserData != null) {
                    if (UserData.size() > 0) {
                        getprofileUserdata(UserData);
                        if (profileUser.events.length() > 0) {
                            getEvents(profileUser.events);
                        } else {
                            Intent mySuperIntent = new Intent(SplashScreen.this, HomePage.class);
                            startActivity(mySuperIntent);
                            finish();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(),
                                "Error while fetching the data", Toast.LENGTH_LONG).show();
                        mConStatusTv.setText("Please Refresh");
                        mConStatusTv.setVisibility(View.VISIBLE);
                        mConStatusBtn.setVisibility(View.VISIBLE);
                    }
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Something went wrong, please refresh", Toast.LENGTH_LONG).show();
                    mConStatusIv.setImageResource(R.drawable.connection);
                    mConStatusIv.setVisibility(View.VISIBLE);
                    mConStatusTv.setText("Please Refresh");
                    mConStatusTv.setVisibility(View.VISIBLE);
                    mConStatusBtn.setVisibility(View.VISIBLE);
                }
            }

        });
        taskRead.setExceptionHandler(new ExceptionHandler() {
            @Override
            public void handleException(Exception e) {
                if (e != null && e.getMessage() != null) {
                    Toast.makeText(getApplicationContext(),
                            "" + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
        taskRead.execute("https://voteforashish.000webhostapp.com/myTeam/getUserData.php");
    }

    public static boolean checkNetworkConnectionStatus(Context context) {
        boolean wifiConnected = false;
        boolean mobileConnected = false;
        ConnectivityManager connMgr = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeInfo = connMgr.getActiveNetworkInfo();
        if (activeInfo != null && activeInfo.isConnected()) { //connected with either mobile or wifi
            wifiConnected = activeInfo.getType() == ConnectivityManager.TYPE_WIFI;
            mobileConnected = activeInfo.getType() == ConnectivityManager.TYPE_MOBILE;
        } else { //no internet connection

        }
        return wifiConnected || mobileConnected;
    }

    public static void getprofileUserdata(ArrayList<User> list) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).email.equals(TempHolder)) {
                profileUser = list.get(i);
                pos = i;
            }
            UserMap.put(list.get(i).id, list.get(i));
        }
    }

    public void getEvents(String events) {
        String[] names = events.split(",", 0);
        String sql = "SELECT * FROM table_event WHERE event_ID IN (" + MakePlaceholders(names.length - 1) + ")  ORDER BY event_date";
        HashMap<String, String> getEvents = new HashMap<>();
        getEvents.put("events", events);
        getEvents.put("sql", sql);
        PostResponseAsyncTask taskRead1 = new PostResponseAsyncTask(SplashScreen.this, getEvents, false, new AsyncResponse() {
            @Override
            public void processFinish(String s1) {
                    cardList.clear();
                    cardList = new JsonConverter<Class_Event>().toArrayList(s1, Class_Event.class);
                if (cardList != null) {
                    if (cardList.size() > 0) {
                        parseEvents(cardList);
                        if (profileUser.transactions.length() > 0) {
                            getTransactions(profileUser.transactions);
                        } else {
                            Intent mySuperIntent = new Intent(SplashScreen.this, HomePage.class);
                            startActivity(mySuperIntent);
                            finish();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(),
                                "Error while fetching the data", Toast.LENGTH_LONG).show();
                        mConStatusTv.setText("Please Refresh");
                        mConStatusTv.setVisibility(View.VISIBLE);
                        mConStatusBtn.setVisibility(View.VISIBLE);
                    }
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Something went wrong, please refresh", Toast.LENGTH_LONG).show();
                    mConStatusIv.setImageResource(R.drawable.connection);
                    mConStatusIv.setVisibility(View.VISIBLE);
                    mConStatusTv.setText("Please Refresh");
                    mConStatusTv.setVisibility(View.VISIBLE);
                    mConStatusBtn.setVisibility(View.VISIBLE);
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

    public static void parseEvents(ArrayList<Class_Event> list) {
        upcomingCardList.clear();
        previousCardList.clear();
        for (int i = 0; i < list.size(); i++) {
            Date today = new Date();
            Date myDate = new Date();
            SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd");
            try {
                myDate = dft.parse(list.get(i).eDate);
                today = dft.parse(dft.format(new Date()));
            } catch (ParseException e) {
                e.printStackTrace(System.out);
            }
            if (today.compareTo(myDate) >= 0) {
                previousCardList.add(list.get(i));

            } else {
                upcomingCardList.add(list.get(i));
            }
            EventMap.put(list.get(i).eId, list.get(i));
        }
    }

    public void getTransactions(String transactions) {
        String[] names = transactions.split(",", 0);
        String sql = "SELECT * FROM table_transaction WHERE transaction_id IN (" + MakePlaceholders((names.length - 1)) + ")  ORDER BY transaction_date DESC";
        HashMap<String, String> getEvents = new HashMap<>();
        getEvents.put("events", transactions);
        getEvents.put("sql", sql);
        PostResponseAsyncTask taskRead3 = new PostResponseAsyncTask(SplashScreen.this, getEvents, false, new AsyncResponse() {
            @Override
            public void processFinish(String s) {
                    TransList.clear();
                    TransList = new JsonConverter<Class_Transaction>().toArrayList(s, Class_Transaction.class);
                if (TransList != null) {
                    if (TransList.size() > 0) {
                        parseTrans(TransList);
                        Intent mySuperIntent = new Intent(SplashScreen.this, HomePage.class);
                        startActivity(mySuperIntent);
                        finish();

                    } else {
                        Toast.makeText(getApplicationContext(),
                                "Error while fetching the data", Toast.LENGTH_LONG).show();
                        mConStatusTv.setText("Please Refresh");
                        mConStatusTv.setVisibility(View.VISIBLE);
                        mConStatusBtn.setVisibility(View.VISIBLE);
                    }
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Something went wrong, please refresh", Toast.LENGTH_LONG).show();
                    mConStatusIv.setImageResource(R.drawable.connection);
                    mConStatusIv.setVisibility(View.VISIBLE);
                    mConStatusTv.setText("Please Refresh");
                    mConStatusTv.setVisibility(View.VISIBLE);
                    mConStatusBtn.setVisibility(View.VISIBLE);
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

    public static void parseTrans(ArrayList<Class_Transaction> list) {
        DbtTransList.clear();
        CrtTransList.clear();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).tType.equals("credit")) {
                CrtTransList.add(list.get(i));

            } else {
                DbtTransList.add(list.get(i));
            }
        }
    }

    public static String MakePlaceholders(int len) {
        if (len < 1) {
            // It will lead to an invalid query anyway ..
            throw new RuntimeException("No placeholders");
        } else {
            StringBuilder sb = new StringBuilder(len * 2 - 1);
            sb.append("?");
            for (int i = 1; i < len; i++) {
                sb.append(",?");
            }
            return sb.toString();
        }
    }

}

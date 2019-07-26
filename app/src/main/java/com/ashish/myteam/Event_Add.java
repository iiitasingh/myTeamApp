package com.ashish.myteam;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.ExceptionHandler;
import com.kosalgeek.genasync12.PostResponseAsyncTask;

import java.util.ArrayList;
import java.util.HashMap;

import static com.ashish.myteam.SplashScreen.pos;
import static com.ashish.myteam.SplashScreen.profileUser;
import static com.ashish.myteam.SplashScreen.UserData;

public class Event_Add extends AppCompatActivity {

    EditText eventName, eventDesc, eventDate, approxContri;
    CheckBox selectallFrnd, contriButton;
    ListView eventMembers;
    Button eventAddImgBtn;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    ArrayList<Integer> mUserItems;
    String eventMembersString;
    String eventOwner;
    User owner;
    int ownerID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_event_add);

        eventName = (EditText)findViewById(R.id.eventName);
        eventDesc = (EditText)findViewById(R.id.eventDesc);
        eventDate = (EditText)findViewById(R.id.eventDate);
        selectallFrnd = (CheckBox) findViewById(R.id.selectFrnd);
        contriButton = (CheckBox) findViewById(R.id.collection_Box);
        eventMembers = (ListView) findViewById(R.id.eventMembers);
        eventAddImgBtn = (Button) findViewById(R.id.eventAddImgBtn);
        approxContri = (EditText)findViewById(R.id.approxContri);
        mUserItems = new ArrayList<>();

        eventMembers.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        owner = profileUser;
        eventOwner = owner.email;
        ownerID = owner.id;

        if (UserData.size() == 0) {
            Toast.makeText(Event_Add.this, "No Team Member", Toast.LENGTH_SHORT).show();
            eventMembers.setAdapter(new Adapter_choose_member(Event_Add.this,UserData));
        }else {
            eventMembers.setAdapter(new Adapter_choose_member(Event_Add.this,UserData));
        }

        eventDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dobPicker = new DatePickerDialog(
                        Event_Add.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year,month,day);
                dobPicker.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dobPicker.show();
            }
        });

        contriButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(contriButton.isChecked())
                {
                    approxContri.setFocusableInTouchMode(true);
                    approxContri.requestFocus();
                    approxContri.setFocusable(true);
                    approxContri.setClickable(true);
                }else{
                    approxContri.setFocusableInTouchMode(false);
                    approxContri.requestFocus();
                    approxContri.setText("");
                    approxContri.setFocusable(false);
                    approxContri.setClickable(false);
                }
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date;
                String dayS = String.valueOf(day);
                String monthS = String.valueOf(month);
                if(month < 10 )
                {
                    monthS = "0" + monthS;
                }
                if(day < 10 )
                {
                    dayS = "0" + dayS;
                }

                date = year + "-" + monthS + "-" + dayS;
                eventDate.setText(date);
            }
        };

        eventMembers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(selectallFrnd.isChecked())
                {
                    selectallFrnd.setChecked(false);
                }
                if(!mUserItems.contains(position)){
                    mUserItems.add(position);
                }else{
                    mUserItems.remove((Integer.valueOf(position)));
                }
            }
        });

        eventAddImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                eventMembersString ="";
                if(!mUserItems.contains(pos))
                {
                    mUserItems.add(pos);
                }

                for (int i = 0; i < mUserItems.size(); i++) {
                    eventMembersString = eventMembersString + String.valueOf(UserData.get(mUserItems.get(i)).id);
                    if (i != mUserItems.size() - 1) {
                        eventMembersString = eventMembersString + ",";
                    }
                }

                String name = eventName.getText().toString().trim();
                String desc = eventDesc.getText().toString().trim();
                String date = eventDate.getText().toString().trim();
                String contri = Boolean.toString(contriButton.isChecked());
                String contriAmount = approxContri.getText().toString().trim();

                if(name.length() >= 3 && desc.length() >=5 && date.length() >4)
                {
                    if(contriButton.isChecked()) {
                        if(contriAmount.length() > 0) {
                            addNewEvent(name, desc, date, contri, contriAmount );
                        }
                        else {
                            Toast.makeText(Event_Add.this, "Put Some approximate Contri Amount", Toast.LENGTH_LONG).show();
                        }
                    }
                    else {
                        contriAmount = "0";
                        addNewEvent(name, desc, date, contri, contriAmount );
                    }
                }
                else if (name.length() < 3)
                {
                    Toast.makeText(Event_Add.this, "Name length is less than 3", Toast.LENGTH_LONG).show();
                }
                else if (desc.length() <5)
                {
                    Toast.makeText(Event_Add.this, "Description Venue atleast", Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(Event_Add.this, "Date should not be null", Toast.LENGTH_LONG).show();
                }
            }
        });

        selectallFrnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectallFrnd.isChecked())
                {
                    for ( int i=0; i < eventMembers.getChildCount(); i++) {
                        eventMembers.setItemChecked(i, true);
                    }
                    mUserItems.clear();
                    for (int i = 0; i < UserData.size(); i++) {
                        mUserItems.add(i);
                    }

                }
                else {
                    for ( int i=0; i < eventMembers.getChildCount(); i++) {
                        eventMembers.setItemChecked(i, false);
                    }
                    mUserItems.clear();
                    //Toast.makeText(AddEvent.this, eventMembersString, Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    public void addNewEvent(String name, String desc, String date, String contri, String contriAmount ){
        HashMap<String, String> EventData = new HashMap<>();
        EventData.put("name", SignUp.capitailizeWord(name));
        EventData.put("owner", eventOwner);
        EventData.put("desc", desc);
        EventData.put("date", date);
        EventData.put("eventMemb", eventMembersString);
        EventData.put("contriBool", contri);
        EventData.put("ApxContri", contriAmount);

        PostResponseAsyncTask AddEvent = new PostResponseAsyncTask(Event_Add.this,
                EventData, new AsyncResponse() {
            @Override
            public void processFinish(String s) {
                if (s.contains("Error")) {
                    Toast.makeText(Event_Add.this, "Event Creation failed", Toast.LENGTH_LONG).show();
                } else if (s.contains("ErrorUpdate")){
                    Toast.makeText(Event_Add.this, "Event Update failed", Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(Event_Add.this, "Event Created :) Swipe down to refresh", Toast.LENGTH_LONG).show();
                    Intent events = new Intent(Event_Add.this, HomePage.class);
                    startActivity(events);
                    finish();
                }
            }
        });
        AddEvent.setExceptionHandler(new ExceptionHandler() {
            @Override
            public void handleException(Exception e) {
                if (e != null && e.getMessage() != null) {
                    Toast.makeText(getApplicationContext(),
                            "" + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
        AddEvent.execute("https://voteforashish.000webhostapp.com/myTeam/AddEvent.php");
    }
}

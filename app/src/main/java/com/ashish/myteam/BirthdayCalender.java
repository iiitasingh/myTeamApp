package com.ashish.myteam;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.service.autofill.UserData;
import android.text.format.DateFormat;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.ashish.myteam.SplashScreen.cardList;
import static com.ashish.myteam.SplashScreen.UserData;

public class BirthdayCalender extends AppCompatActivity {

    private static final String TAG = "BirthdayCalender";

    CompactCalendarView compactCalendar;
    TextView current;
    Calendar cal;
    ListView birthdays;
    ArrayList<String> names;
    private String[] dob_arr = {};
    User calenderOwner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_birthday_calender);

        compactCalendar = (CompactCalendarView) findViewById(R.id.compactcalendar_view);
        current = (TextView) findViewById(R.id.currentMonthYr);
        birthdays = (ListView) findViewById(R.id.birtdayList);

        cal = Calendar.getInstance();
        current.setText(DateFormat.format("MMMM yyyy", cal));
        compactCalendar.setFirstDayOfWeek(Calendar.MONDAY);

        setbdEvent(UserData);
        setEvent(cardList);

        //////////////////   Checking Events for today's Date   //////////////////
        Date today = Calendar.getInstance().getTime();
        List<Event> events = compactCalendar.getEvents(today);
        names = new ArrayList<String>();
        if (events.size() > 0) {
            names.clear();
            for (int i = 0; i < events.size(); i++) {
                Event ash = events.get(i);
                names.add(ash.getData().toString());
            }

            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                    BirthdayCalender.this,
                    android.R.layout.simple_list_item_1,
                    names);

            birthdays.setAdapter(arrayAdapter);

        } else {
            birthdays.setAdapter(null);
        }

        compactCalendar.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                List<Event> events = compactCalendar.getEvents(dateClicked);
                names = new ArrayList<String>();
                if (events.size() > 0) {

                    names.clear();
                    //Toast.makeText(BirthdayCalender.this, "Happy Birthday " + events.get(0).getData(), Toast.LENGTH_SHORT).show();
                    for (int i = 0; i < events.size(); i++) {
                        Event ash = events.get(i);
                        names.add(ash.getData().toString());
                    }

                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                            BirthdayCalender.this,
                            android.R.layout.simple_list_item_1,
                            names);

                    birthdays.setAdapter(arrayAdapter);

                } else {
                    birthdays.setAdapter(null);
                }

            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                current.setText(DateFormat.format("MMMM yyyy", firstDayOfNewMonth));
            }
        });


    }

    public void setbdEvent(ArrayList<User> dobdate) {

        int i = dobdate.size();
        int today = Calendar.getInstance().get(Calendar.YEAR);
        String currDate;

        for (int j = 0; j < i; j++) {

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date data = null;
            try {
                data = sdf.parse(dobdate.get(j).dob);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            currDate = DateFormat.format("dd/MM", data) + "/" + today + " 11:00:00";

            SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Date data1 = null;
            try {
                data1 = sdf1.parse(currDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            long millis = data1.getTime();
            Event ev1 = new Event(Color.GREEN, millis, "Happy Birthday " + dobdate.get(j).name);
            compactCalendar.addEvent(ev1);
        }

    }
    public void setEvent(ArrayList<Class_Event> evtdate) {

        int i = evtdate.size();
        int today = Calendar.getInstance().get(Calendar.YEAR);
        String currDate;

        for (int j = 0; j < i; j++) {

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date data = null;
            try {
                data = sdf.parse(evtdate.get(j).eDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            currDate = DateFormat.format("dd/MM", data) + "/" + today + " 11:00:00";

            SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Date data1 = null;
            try {
                data1 = sdf1.parse(currDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            long millis = data1.getTime();
            Event ev1 = new Event(Color.GREEN, millis, "Event: " + evtdate.get(j).eName);
            compactCalendar.addEvent(ev1);
        }

    }
}

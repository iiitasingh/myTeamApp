package com.ashish.myteam;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kosalgeek.android.json.JsonConverter;
import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.ExceptionHandler;
import com.kosalgeek.genasync12.PostResponseAsyncTask;

import static com.ashish.myteam.SplashScreen.MakePlaceholders;
import static com.ashish.myteam.SplashScreen.cardList;
import static com.ashish.myteam.SplashScreen.parseEvents;
import static com.ashish.myteam.SplashScreen.previousCardList;
import static com.ashish.myteam.SplashScreen.profileUser;

import java.util.ArrayList;
import java.util.HashMap;

public class Adapter_Event_Card extends BaseAdapter {

    private Context context;
    private int layout;
    private String team;
    private ArrayList<Class_Event> eventCard;
    Dialog dialog;

    //constructor initializing the values
    public Adapter_Event_Card(Context context, int resource, ArrayList<Class_Event> eventCardDetail, String team) {
        this.context = context;
        this.layout = resource;
        this.eventCard = eventCardDetail;
        this.team = team;
        dialog = new Dialog(this.context);
    }

    @Override
    public int getCount() {
        return eventCard.size();
    }

    @Override
    public Object getItem(int position) {
        return eventCard.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {
        ImageView contriImg;
        LinearLayout eventList;
        TextView eventName, teamName, eventDesc, eventDate, TotalContri, SpentContri, RemainingContri, eventMemList;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        Adapter_Event_Card.ViewHolder holder = new Adapter_Event_Card.ViewHolder();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(layout, null, false);
        holder.contriImg = view.findViewById(R.id.contriImg);
        holder.eventList = view.findViewById(R.id.EventListLayout);
        holder.eventName = view.findViewById(R.id.eventTextView7);
        holder.teamName = view.findViewById(R.id.eventCardTeam);
        holder.eventDesc = view.findViewById(R.id.eventCardDesc);
        holder.eventDate = view.findViewById(R.id.EventtextView6);
        holder.TotalContri = view.findViewById(R.id.TotalContri);
        holder.SpentContri = view.findViewById(R.id.SpentContri);
        holder.RemainingContri = view.findViewById(R.id.RemainingContri);
        holder.eventMemList = view.findViewById(R.id.eventMemList);

        final Class_Event card = eventCard.get(position);

        holder.eventName.setText(card.eName);
        holder.teamName.setText(team);
        holder.eventDesc.setText(card.eDesc);
        holder.eventDate.setText("Date: " + card.eDate);


        if (card.eContiBool.equals("true")) {
            holder.contriImg.setImageResource(R.drawable.tick);
        } else {
            holder.contriImg.setImageResource(R.drawable.cancel);
        }
        if (context instanceof Remaining_Contri) {
            holder.TotalContri.setText("Total: " + card.eContriTotal);
            holder.SpentContri.setText("Spent: " + card.eContriSpent);
            holder.RemainingContri.setText("Remaining: " + card.eContriRem);
        } else {
            holder.TotalContri.setText("");
            holder.SpentContri.setText("");
            holder.RemainingContri.setText("");

            holder.eventList.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    dialog.setContentView(R.layout.popup_delete);
                    TextView evntName = (TextView) dialog.findViewById(R.id.DeleteEventName);
                    Button deleteBtn = (Button) dialog.findViewById(R.id.DeleteEventBtn);
                    evntName.setText(card.eName);
                    int width1 = (int) (context.getResources().getDisplayMetrics().widthPixels * .8);
                    int height1 = (int) (context.getResources().getDisplayMetrics().heightPixels * 0.2);
                    dialog.getWindow().setLayout(width1, height1);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.show();

                    deleteBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(profileUser.email.equals(card.eOwner)){
                                deleteData(card.eId,position);
                            }else {
                                Toast.makeText(context,
                                        "You can't delete this Event", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                    return true;
                }
            });
        }

        holder.eventMemList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent memList = new Intent(context, Event_Members.class);
                memList.putExtra("members", card.eId);
                context.startActivity(memList);
            }
        });

        return view;
    }

    private void deleteData(int id, final int position){
        String sql = "DELETE FROM table_event WHERE event_ID ="+id;
        HashMap<String, String> getEvents = new HashMap<>();
        getEvents.put("sql", sql);
        PostResponseAsyncTask taskRead1 = new PostResponseAsyncTask(context, getEvents, new AsyncResponse() {
            @Override
            public void processFinish(String s) {
                if(s.contains("success")){
                    Toast.makeText(context,
                            "Event Deleted", Toast.LENGTH_LONG).show();
                    updateEventData(position);
                    eventCard.remove(position);
                    notifyDataSetChanged();
                    dialog.dismiss();
                }else {
                    Toast.makeText(context,
                            "Try Again"+s, Toast.LENGTH_LONG).show();
                }
            }

        });
        taskRead1.setExceptionHandler(new ExceptionHandler() {
            @Override
            public void handleException(Exception e) {
                if (e != null && e.getMessage() != null) {
                    Toast.makeText(context,
                            "" + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
        taskRead1.execute("https://voteforashish.000webhostapp.com/myTeam/deleteEvent.php");
    }

    public void updateEventData(final int position) {
        String[] names = profileUser.events.split(",", 0);
        String sql = "SELECT * FROM table_event WHERE event_ID IN (" + MakePlaceholders(names.length - 1) + ")  ORDER BY event_date";
        HashMap<String, String> getEvents = new HashMap<>();
        getEvents.put("events", profileUser.events);
        getEvents.put("sql", sql);
        PostResponseAsyncTask taskRead1 = new PostResponseAsyncTask(context, getEvents, false, new AsyncResponse() {
            @Override
            public void processFinish(String s1) {
                cardList.clear();
                cardList = new JsonConverter<Class_Event>().toArrayList(s1, Class_Event.class);
                if (cardList != null) {
                    if (cardList.size() > 0) {
                        parseEvents(cardList);
                        Toast.makeText(context,
                                "Event Data Refreshed", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(context,
                                "Error while refreshing Event Data, Swipe Down to refresh", Toast.LENGTH_LONG).show();
                        Intent home = new Intent(context,HomePage.class);
                        context.startActivity(home);
                    }
                } else {
                    Toast.makeText(context,
                            "Something went wrong, please refresh", Toast.LENGTH_LONG).show();
                }
            }

        });
        taskRead1.setExceptionHandler(new ExceptionHandler() {
            @Override
            public void handleException(Exception e) {
                if (e != null && e.getMessage() != null) {
                    Toast.makeText(context,
                            "" + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
        taskRead1.execute("https://voteforashish.000webhostapp.com/myTeam/getEvents.php");
    }
}
